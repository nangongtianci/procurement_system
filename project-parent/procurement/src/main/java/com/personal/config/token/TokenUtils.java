package com.personal.config.token;


import com.personal.common.enume.UserTypeEnum;
import com.personal.common.utils.constants.AppConstant;
import com.personal.common.utils.encode.MD5Util;
import com.personal.config.redis.RedisService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * token工具
 * @author ylw
 * @date 18-6-7 下午1:56
 * @param
 * @return
 */
public class TokenUtils {

    /**
     * 设置token
     * @param userTypeEnum
     * @param userId
     * @param redisService
     * @return
     */
    public static String setToken(UserTypeEnum userTypeEnum,String userId,RedisService redisService){
        String token = "";
        Date time = new Date();
        try {
            byte[] b = (time + DigestUtils.md5Hex(userId)).getBytes("utf-8");
            token = DigestUtils.md5Hex(b);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        redisService.set(getPre(userTypeEnum).concat(token),userId,AppConstant.TOKEN_EXP_TIME);

        // 配置此与用户唯一有效token
        redisService.set(AppConstant.TOEKN_RPEPIX
                + MD5Util.getStringMD5(userId+AppConstant.TOEKN_SALT),token,AppConstant.TOKEN_EXP_TIME);
        return token;
    }

    /**
     * 获取前缀
     * @param userTypeEnum
     * @return
     */
    public static String getPre(UserTypeEnum userTypeEnum){
        return "token".concat("_").concat("app")
                .concat("_").concat(userTypeEnum.getValue()).concat("_");
    }

    /**
     * 校验token有效期,如果为null返回false
     * @param userTypeEnum
     * @param redisService
     * @return
     */
    public static boolean checkToekn(UserTypeEnum userTypeEnum,String token,RedisService redisService){
        String userId = redisService.get(getPre(userTypeEnum).concat(token));
        if(StringUtils.isBlank(userId)){
            return false;
        }
        String theOnlyToken = redisService.get(AppConstant.TOEKN_RPEPIX+ MD5Util.getStringMD5(userId+AppConstant.TOEKN_SALT));
        if(StringUtils.isBlank(theOnlyToken)){
            return false;
        }
        return token.equalsIgnoreCase(theOnlyToken);
    }

    /**
     * getUid
     * @param userTypeEnum
     * @param token
     * @param redisService
     * @return
     */
    public static String getUid(UserTypeEnum userTypeEnum,String token,RedisService redisService){
        String s = redisService.get(getPre(userTypeEnum).concat(token));
        if(StringUtils.isNotBlank(s)){
            return s;
        }
        return "";
    }

    /**
     * 退出登录
     * @param userTypeEnum
     * @param token
     * @param redisService
     */
    public static void delToekn(UserTypeEnum userTypeEnum,String token,RedisService redisService){
        String userId = redisService.get(getPre(userTypeEnum).concat(token));
        if(StringUtils.isNotBlank(userId)){
            redisService.del(AppConstant.TOEKN_RPEPIX+ MD5Util.getStringMD5(userId+AppConstant.TOEKN_SALT));
        }
        redisService.del(getPre(userTypeEnum).concat(token));
    }

}
