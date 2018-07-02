package com.personal.controller;

import com.personal.common.json.JsonUtils;
import com.personal.common.random.RandomNum;
import com.personal.common.utils.base.StringUtils;
import com.personal.common.utils.base.UUIDUtils;
import com.personal.common.utils.constants.AppConstant;
import com.personal.common.utils.result.Result;
import com.personal.common.utils.sms.Sms;
import com.personal.communicate.HttpUtil;
import com.personal.config.system.sms.SmsConfig;
import com.personal.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.personal.common.utils.result.CommonResultMsg.assignFieldNotNull;

@RestController
@RequestMapping("/common")
public class CommonController {
    @Autowired
    private SmsConfig smsConfig;
    @Autowired
    private RedisService redisService;

    @GetMapping("/send/sms/{mobile}")
    public Result sendSms(@PathVariable String mobile){
        if(StringUtils.isBlank(mobile)){
            return Result.FAIL(assignFieldNotNull("手机号"));
        }

        if(StringUtils.isNotBlank(redisService.get(AppConstant.CHECK_CODE_RPEPIX+mobile))){
            return Result.FAIL("规定时间内不能重新获取验证码！");
        }

        String checkCode = RandomNum.createSmsAuthCode(6);
        Map<String, String> param  = new HashMap<>();
        param.put("acctno",smsConfig.getAcctno());
        param.put("passwd",smsConfig.getPasswd());
        param.put("mobile",mobile);
        param.put("msg", String.format(smsConfig.getTpl(),checkCode));
        Sms sms = JsonUtils.toBean(HttpUtil.doGet(smsConfig.getUrl(),param),Sms.class);
        if(sms != null && sms.getRetCode() == 0){
            redisService.set(AppConstant.CHECK_CODE_RPEPIX+mobile,checkCode,AppConstant.CHECK_CODE_EXP_TIME);
            return Result.OK();
        }
        return Result.FAIL("发送失败！");
    }

    @GetMapping("test")
    public String test(){
        return "wo shi test!";
    }

}
