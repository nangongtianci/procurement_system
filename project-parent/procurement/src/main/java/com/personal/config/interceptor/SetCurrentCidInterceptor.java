package com.personal.config.interceptor;

import com.msb.common.enume.UserTypeEnum;
import com.personal.config.redis.RedisService;
import com.personal.config.token.TokenUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**  
 * @author ylw
 * @date 18-7-3 上午10:22
 * @param   
 * @return   
 */ 
public class SetCurrentCidInterceptor extends HandlerInterceptorAdapter
{
    private RedisService redisService;
    public SetCurrentCidInterceptor(){}
    public SetCurrentCidInterceptor(RedisService redisService){
        this.redisService = redisService;
    }

    /**
     * @desc 重置token过期时间
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception
    {
        TokenUtils.getUid(UserTypeEnum.customer,request.getHeader("token"),redisService);
        return true;
    }
}
