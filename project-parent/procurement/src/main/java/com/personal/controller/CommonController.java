package com.personal.controller;

import com.msb.common.enume.UserTypeEnum;
import com.personal.common.json.JsonUtils;
import com.msb.common.random.RandomNum;
import com.msb.common.utils.base.StringUtils;
import com.msb.common.utils.constants.AppConstant;
import com.msb.common.utils.result.Result;
import com.msb.common.utils.sms.Sms;
import com.personal.communicate.HttpUtil;
import com.personal.config.redis.RedisService;
import com.personal.config.system.sms.SmsConfig;
import com.personal.config.token.TokenUtils;
import com.personal.entity.Voice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.msb.common.utils.result.CommonResultMsg.assignFieldNotNull;

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

    /**
     * 检查token是否过期
     * @param request
     * @return
     */
    @GetMapping("checkToken")
    public String checkToken(HttpServletRequest request){
        if(TokenUtils.checkToekn(UserTypeEnum.customer,request.getHeader("token"),redisService)){
            return "false";
        }else{
            return "true";
        }
    }

    /**
     * 语义解析
     * @param voice
     * @return
     */
    @PostMapping("/voice")
    public String sendSms(Voice voice){
        Map<String, String> map = new HashMap<>();
        map.put("text",voice.getText());
        map.put("userId",voice.getUserId());
        map.put("operationId",voice.getOperationId());
        return HttpUtil.doPost("http://112.125.89.15/bill/parse",map);
    }

    @GetMapping("test")
    public String test(){
        return "wo shi test!";
    }

    public static void main(String[] args) {
//        Voice voice = new Voice();
//        voice.setText("");
//        voice.setUserId("anonymous");
//        voice.setOperationId("web-5717548096468088");
//        Map<String, String> map = new HashMap<>();
//        map.put("text","苹果20公斤合格证书");
//        map.put("userId","anonymous");
//        map.put("operationId","web-5717548096468088");
//        System.out.print(HttpUtil.
//                doPost("http://112.125.89.15/bill/parse",map));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(1467302400000l);
        System.out.println(sdf.format(date));

        Calendar ca = Calendar.getInstance();
        // int year, int month, int date, int hourOfDay, int minute
        ca.set(2017,9,0,24,0,0);
//        ca.set(2016,4,0,24,0,0);
        System.out.println(sdf.format(ca.getTime()));
        System.out.println(ca.getTime().getTime());
    }

}
