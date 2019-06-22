package com.msb.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.msb.common.utils.constants.DictConstant;
import com.msb.common.utils.token.TokenUtils;
import com.msb.entity.Customer;
import com.msb.entity.Dict;
import com.msb.requestParam.LoginParam;
import com.msb.service.CustomerService;
import com.msb.common.annotation.UpdateMethodFlag;
import com.msb.common.base.controller.BaseMsbController;
import com.msb.common.cache.RedisService;
import com.msb.common.constant.SysConstant;
import com.msb.common.enume.ModuleEnum;
import com.msb.common.enume.UserTypeEnum;
import com.msb.common.utils.base.StringUtils;
import com.msb.common.utils.base.UUIDUtils;
import com.msb.common.utils.communicate.HttpUtil;
import com.msb.common.utils.encode.AESUtil;
import com.msb.common.utils.result.Result;
import com.msb.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.msb.common.utils.result.CommonResultMsg.assignFieldIllegalValueRange;
import static com.msb.common.utils.result.CommonResultMsg.assignModuleNameForPK;
import static com.msb.common.utils.result.RegUtils.matchesIds;

/**
 * <p>
 * 客户信息 前端控制器
 * </p>
 *
 * @author ylw
 * @since 2019-05-27
 */
@RestController
@RequestMapping("/customer")
public class CustomerController extends BaseMsbController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private DictService dictService;

    /**
     * 用户详情
     * @param id
     * @return
     */
    @PostMapping("/{id}")
    public Result getById(@PathVariable String id){
        if(!matchesIds(id)){
            return render(assignModuleNameForPK(ModuleEnum.customer));
        }
        return render(customerService.selectById(id));
    }

    /**
     * 更新用户
     * @param customer
     * @return
     */
    @UpdateMethodFlag
    @PostMapping("update")
    public Result update(@RequestBody Customer customer){
        if(!matchesIds(customer.getId())){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.customer));
        }

        int ct = customerService.selectCount(new EntityWrapper<Customer>().
                where("phone={0} and id!={1}",customer.getPhone(),customer.getId()));
        if(ct>0){
            return render("手机号已存在!");
        }

        if(StringUtils.isNotBlank(customer.getSex())){
            int bt = dictService.selectCount(new EntityWrapper<Dict>().
                    where("pid={0} and code={1}", DictConstant.SEX_CATEGORY_ID,customer.getSex()));
            if(bt<=0){
                return render(null,assignFieldIllegalValueRange("性别"));
            }
        }
        return render(customerService.updateById(customer));
    }


    /**
     * 获取openId和sessionKey信息
     * @param code
     * @return
     */
    @PostMapping(value = "/obtain/auth/info/{code}")
    public Result obtainAuthInfo(@PathVariable String code){
        if(StringUtils.isBlank(code)){
            return render("code 不能为空!");
        }

        Map<String,String> params = new HashMap<>();
        params.put("appid",SysConstant.WX_APPID);
        params.put("secret",SysConstant.WX_SECRET);
        params.put("js_code",code);
        params.put("grant_type",SysConstant.GRANT_TYPE);
        String sr = HttpUtil.doGet("https://api.weixin.qq.com/sns/jscode2session", params);
        return render(JSONObject.parse(sr));
    }

    /**
     * 登录
     * @param loginParam
     * @return
     */
    @PostMapping(value = "/login")
    public Result login(@RequestBody LoginParam loginParam){
        if(StringUtils.isBlank(loginParam.getEncryptedData()) ||
           StringUtils.isBlank(loginParam.getIv()) ||
           StringUtils.isBlank(loginParam.getSessionKey()) ||
           StringUtils.isBlank(loginParam.getNickName()) ||
           StringUtils.isBlank(loginParam.getIcon())){
            return render(null,"加密数据","加密算法的初始向量","sessionKey","昵称","头像","不能为空！");
        }

        try {
            String result = AESUtil.wxDecrypt(loginParam.getEncryptedData(),loginParam.getSessionKey(),loginParam.getIv());
            if (StringUtils.isNotBlank(result)) {
                String phone = (String)getParamByKey(result,"phoneNumber");
                Customer customer = customerService.selectOne(new EntityWrapper<Customer>().where("phone={0}",phone));
                if(Objects.isNull(customer)){ // 注册
                    customer = new Customer();
                    customer.setId(UUIDUtils.getUUID());
                    customer.setPhone(phone);
                    customer.setName(loginParam.getNickName());
                    customer.setIcon(loginParam.getIcon());
                    customer.setSex(loginParam.getSex());
                    customer.setIsAgreeProtocol("1");
                    customer.setStatus("1");
                    customer.setLoginTime(new Date());
                    customer.setCreateTime(new Date());
                    customer.setUpdateTime(new Date());
                    if(!customerService.insert(customer)){
                        return render(null,"注册失败！");
                    }
                }

                // 返回登录信息
                Map<String,Object> map = new HashMap<>();
                map.put("token", TokenUtils.setToken(UserTypeEnum.customer,customer.getId(),redisService));
                map.put("customer",customer);
                return render(map);
            } else {
                return render(null,"解密失败!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return render(null,e.getMessage());
        }
    }


    /**
     * 注销
     * @param token
     * @return
     */
    @PostMapping("logout")
    public Result logout(String token){
        TokenUtils.delToekn(UserTypeEnum.customer,token,redisService);
        return render("退出成功！");
    }
}

