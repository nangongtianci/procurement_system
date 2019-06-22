package com.msb.common.utils.base;

import com.msb.common.utils.exceptions.BizException;
import com.msb.common.utils.exceptions.enums.BizExceptionEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
/**  
 * @author ylw
 * @date 18-6-7 下午5:03
 * @param   
 * @return   
 */ 
public class AuthorizationUtils {

    public static String getUid(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String uid = request.getHeader("uid");
        if(StringUtils.isBlank(uid)){
            throw new BizException(BizExceptionEnum.UID_IS_NULL);
        }
        return uid;
    }

    public static String getBodyJson(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return (String) request.getAttribute("body");
    }
}
