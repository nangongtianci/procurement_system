package com.personal.config.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.personal.common.enume.UserTypeEnum;
import com.personal.common.utils.base.StringUtils;
import com.personal.common.utils.constants.AppConstant;
import com.personal.common.utils.exceptions.enums.BizExceptionEnum;
import com.personal.common.utils.result.Result;
import com.personal.config.redis.RedisService;
import com.personal.config.token.TokenUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**  
 * @author ylw
 * @date 18-7-3 上午10:22
 * @param   
 * @return   
 */ 
public class CustomerTokenSurvivalTimeInterceptor extends HandlerInterceptorAdapter
{
    private RedisService redisService;
    public CustomerTokenSurvivalTimeInterceptor(){}
    public CustomerTokenSurvivalTimeInterceptor(RedisService redisService){
        this.redisService = redisService;
    }

    /**
     * @desc 重置token过期时间
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception
    {
        if(StringUtils.isBlank(request.getHeader("token"))
                || !TokenUtils.checkToekn(UserTypeEnum.customer,request.getHeader("token"),redisService)
                || !TokenUtils.resetTokenExpire(UserTypeEnum.customer,request.getHeader("token"),redisService)){
            response.setContentType("application/json; charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.print(JSONObject.toJSONString(Result.FAIL(BizExceptionEnum.REQUEST_EXP),
                    SerializerFeature.WriteMapNullValue,SerializerFeature.WriteDateUseDateFormat));
            writer.close();
            response.flushBuffer();
            return false;
        }
        return true;
    }
}
