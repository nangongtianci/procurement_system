package com.personal.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.personal.common.annotation.InsertMethodFlag;
import com.personal.common.enume.IsAgreeProtocolEnum;
import com.personal.common.enume.LoginStatusEnum;
import com.personal.common.enume.ModuleEnum;
import com.personal.common.enume.UserTypeEnum;
import com.personal.common.utils.base.StringUtils;
import com.personal.common.utils.base.UUIDUtils;
import com.personal.common.utils.collections.ArrayUtils;
import com.personal.common.utils.constants.AppConstant;
import com.personal.common.utils.encode.MD5Util;
import com.personal.common.utils.file.FileUploadUtils;
import com.personal.common.utils.result.Result;
import com.personal.config.redis.RedisService;
import com.personal.config.system.file.FileConfig;
import com.personal.config.token.TokenUtils;
import com.personal.entity.Customer;
import com.personal.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.personal.common.utils.result.CommonResultMsg.*;
import static com.personal.common.utils.result.RegUtils.*;

/**
 * <p>
 * 客户信息 前端控制器
 * </p>
 *
 * @author ylw
 * @since 2018-05-13
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private FileConfig fileConfig;
    @Autowired
    private RedisService redisService;

    /**
     * 登录
     * @param customer
     * @return
     */
    @PostMapping("login")
    public Result login(Customer customer){
        if(!matchesMobilePhone(customer.getPhone())){
            return Result.FAIL(assignFieldIllegal("手机号或密码"));
        }

        if(StringUtils.isBlank(customer.getPassword())){
            return Result.FAIL(assignFieldIllegal("手机号或密码"));
        }

        EntityWrapper<Customer> ew = new EntityWrapper();
        ew.setSqlSelect("id","password","secret_key as secretKey");
        ew.where("phone={0}",customer.getPhone());
        Customer rt = customerService.selectOne(ew);
        if(rt == null || StringUtils.isBlank(rt.getPassword())
                || StringUtils.isBlank(rt.getSecretKey())){
            return Result.FAIL("账号不存在！");
        }

        if(!rt.getPassword().equals(MD5Util.getStringMD5(customer.getPassword()+rt.getSecretKey()))){
            return Result.FAIL("密码错误！");
        }

        rt.setStatus(LoginStatusEnum.yes.getValue());
        rt.setUpdateTime(new Date());
        if(customerService.updateById(rt)){
            Map<String,String> map = new HashMap<>();
            map.put("customerId",rt.getId());
            map.put("token", TokenUtils.setToken(UserTypeEnum.customer,rt.getId(),redisService));
            map.put("customerName",rt.getName());
            return Result.OK().setData(map);
        }
        return Result.FAIL("登录失败！");
    }

    /**
     * 退成成功
     * @param token
     * @return
     */
    @PostMapping("logout")
    public Result logout(String token){
        TokenUtils.delToekn(UserTypeEnum.customer,token,redisService);
        return Result.OK().setData("退出成功！");
    }

    /**
     * 找回密码
     * @param customer
     * @return
     */
    @PostMapping("retrieve/password")
    public Result retrievePassword(Customer customer){
        if(StringUtils.isBlank(customer.getCheckCode())){
            return Result.FAIL(assignFieldNotNull("验证码"));
        }else{
            String checkCode = redisService.get(AppConstant.CHECK_CODE_RPEPIX+customer.getPhone());
            if(StringUtils.isBlank(checkCode)){
                return Result.FAIL("验证码已过期！");
            }else if(!checkCode.equalsIgnoreCase(customer.getCheckCode())){
                return Result.FAIL("验证码错误！");
            }
        }

        if(!matchesMobilePhone(customer.getPhone())){
            return Result.FAIL(assignFieldIllegal("手机号"));
        }

        EntityWrapper<Customer> ew = new EntityWrapper();
        ew.where("phone={0}",customer.getPhone());
        Customer exists = customerService.selectOne(ew);
        if(exists == null){
            return Result.FAIL("账号不存在！");
        }

        if(StringUtils.isBlank(customer.getPassword())){
            return Result.FAIL(assignFieldNotNull("密码"));
        }

        exists.setPassword(MD5Util.getStringMD5(customer.getPassword()+exists.getSecretKey()));
        if(!customerService.updateById(exists)){
            return Result.FAIL("找回密码失败！");
        }
        return Result.OK();
    }

    /**
     * 快捷注册
     * @return
     */
    @PostMapping("/direct")
    public Result insertDirect(){
        Customer customer = new Customer();
        customer.setId(UUIDUtils.getUUID());
        customer.setIsAgreeProtocol(IsAgreeProtocolEnum.yes.getValue());
        customer.setSecretKey(UUIDUtils.getUUID());
        customer.setPassword(MD5Util.getStringMD5("123456"+customer.getSecretKey()));
        customer.setStatus(LoginStatusEnum.yes.getValue());
        customer.setName("自动注册信息请及时更改");
        customer.setPhone("13811111111");
        customer.setIdCard("123456789012345");
        customer.setCreateTime(new Date());
        customer.setUpdateTime(new Date());
        if(customerService.insert(customer)){
            Map<String,String> tmp = new HashMap<>();
            tmp.put("customerId",customer.getId());
            tmp.put("customerName","自动注册信息请及时更改");
            return Result.OK(tmp);
        }
        return Result.FAIL();
    }

    /**
     * 上传文件
     * @param files
     * @return
     */
    @PostMapping("/upload")
    public Result uploadImg(@RequestParam("idCard") MultipartFile[] files){
        try {
            if(ArrayUtils.isEmpty(files)){
                return Result.FAIL("请选择需要上传的图片！");
            }else{
                StringBuilder sb = new StringBuilder();
                for(int i=0;i<files.length;i++){
                    if(files[i].isEmpty()){
                        return Result.FAIL("上传图片失败！");
                    }

                    if(i < files.length-1){
                        sb.append(FileUploadUtils.saveImg(files[i], fileConfig.getImgPath(),fileConfig.getStaticBrowsePath())).append(",");
                    }else{
                        sb.append(FileUploadUtils.saveImg(files[i], fileConfig.getImgPath(),fileConfig.getStaticBrowsePath()));
                    }
                }
                return Result.OK().setData(sb.toString());
            }
        } catch (IOException e) {
            return Result.FAIL("上传图片失败");
        }
    }


    /**
     * 注册账号（完整信息）
     * @param customer
     * @return
     */
    @InsertMethodFlag
    @PostMapping("/register")
    public Result insert(Customer customer){
        if(StringUtils.isBlank(customer.getIsAgreeProtocol()) || IsAgreeProtocolEnum.no.getValue()
                .equalsIgnoreCase(IsAgreeProtocolEnum.getByValue(customer.getIsAgreeProtocol()).getValue())){
            return Result.FAIL("必须同意用户协议！");
        }

        if(StringUtils.isBlank(customer.getCheckCode())){
            return Result.FAIL(assignFieldNotNull("验证码"));
        }else{
            String checkCode = redisService.get(AppConstant.CHECK_CODE_RPEPIX+customer.getPhone());
            if(StringUtils.isBlank(checkCode)){
                return Result.FAIL("验证码已过期！");
            }else if(!checkCode.equalsIgnoreCase(customer.getCheckCode())){
                return Result.FAIL("验证码错误！");
            }
        }

        if(StringUtils.isBlank(customer.getPassword())){
            return Result.FAIL(assignFieldNotNull("密码"));
        }

        if(!matchesPositiveIntegerSection(customer.getName(),
                ConditionEnum.countDefaultValue.getValue(),
                ConditionEnum.nameLen.getValue())){
            return Result.FAIL(assignFieldSection("名称",
                    ConditionEnum.countDefaultValue.getValue(),ConditionEnum.nameLen.getValue()));
        }

        if(!matchesMobilePhone(customer.getPhone())){
            return Result.FAIL(assignFieldIllegal("手机号"));
        }

        if(StringUtils.isNotBlank(customer.getIdCard()) && !matchesIdCard(customer.getIdCard())){
            return Result.FAIL(assignFieldIllegal("身份证"));
        }

        int exist = customerService.selectCount(new EntityWrapper<Customer>().where("phone={0}",customer.getPhone()));
        if(exist>0){
            return Result.FAIL(301,"此手机号已经注册！");
        }

        customer.setSecretKey(UUIDUtils.getUUID());
        customer.setPassword(MD5Util.getStringMD5(customer.getPassword()+customer.getSecretKey()));
        if(customerService.insert(customer)){
            return Result.OK().setData(customer.getId());
        }
        return Result.FAIL();
    }


    /**
     * 更新用户
     * @param customer
     * @return
     */
    @PutMapping
    public Result update(Customer customer){
        if(!matchesIds(customer.getId())){ // 主键
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.customer));
        }

        if(customer.getIsAgreeProtocol() != null && IsAgreeProtocolEnum.no.getValue()
                .equalsIgnoreCase(customer.getIsAgreeProtocol())){
            return Result.FAIL("必须同意用户协议！");
        }

        if(customer.getName() != null && !matchesPositiveIntegerSection(customer.getName(),
                ConditionEnum.countDefaultValue.getValue(),
                ConditionEnum.nameLen.getValue())){
            return Result.FAIL(assignFieldSection("名称",
                    ConditionEnum.countDefaultValue.getValue(),ConditionEnum.nameLen.getValue()));
        }

        if(customer.getPhone() != null && !matchesMobilePhone(customer.getPhone())){
            return Result.FAIL(assignFieldIllegal("手机号"));
        }

        if(customer.getIdCard() != null && !matchesIdCard(customer.getIdCard())){
            return Result.FAIL(assignFieldIllegal("身份证"));
        }

        customer.setPassword(null);
        customer.setSecretKey(null);
        if(customerService.updateById(customer)){
            return Result.OK();
        }
        return Result.FAIL();
    }

    /**
     * 根据主键查询
     * @return
     */
    @GetMapping
    public Result selectById(HttpServletRequest request){
        String customerId = TokenUtils.getUid(UserTypeEnum.customer,request.getHeader("token"),redisService);
        return Result.OK(customerService.selectById(customerId));
    }
}

