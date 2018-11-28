package com.personal.common.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * 跟控制器
 * @author ylw
 * @date 18-6-7 下午5:00
 * @param
 * @return
 */
public class BaseController implements Serializable{
    private static final long serialVersionUID = 1L;

//    protected String getCid()
//    {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        //return TokenUtils.getUid(UserTypeEnum.customer,request.getHeader("token"),redisService);
//        return null;
//    }


}
