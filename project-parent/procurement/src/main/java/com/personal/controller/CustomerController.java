package com.personal.controller;


import com.personal.common.annotation.CommonDataAnnotation;
import com.personal.common.enume.IsAgreeProtocolEnum;
import com.personal.common.enume.LoginStatusEnum;
import com.personal.common.enume.ModuleEnum;
import com.personal.common.utils.base.StringUtils;
import com.personal.common.utils.base.UUIDUtils;
import com.personal.common.utils.collections.ArrayUtils;
import com.personal.common.utils.file.FileUploadUtils;
import com.personal.common.utils.result.Result;
import com.personal.config.system.FileConfig;
import com.personal.entity.Customer;
import com.personal.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * 快捷注册
     * @return
     */
    @PostMapping("/direct")
    public Result insertDirect(){
        Customer customer = new Customer();
        customer.setId(UUIDUtils.getUUID());
        customer.setIsAgreeProtocol(IsAgreeProtocolEnum.yes.getValue());
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
    @CommonDataAnnotation
    @PostMapping
    public Result insert(Customer customer){
        if(IsAgreeProtocolEnum.yes.getValue()
                .equalsIgnoreCase(IsAgreeProtocolEnum.getByValue(customer.getIsAgreeProtocol()).getValue())){
            return Result.FAIL("必须同意用户协议！");
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

        if(!matchesIdCard(customer.getIdCard())){
            return Result.FAIL(assignFieldIllegal("身份证"));
        }

        if(customerService.insertOrUpdate(customer)){
            return Result.OK();
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

        if(customerService.updateById(customer)){
            return Result.OK();
        }
        return Result.FAIL();
    }

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result selectById(@PathVariable String id){
        if(!matchesIds(id)){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.customer));
        }
        return Result.OK(customerService.selectById(id));
    }
}

