package com.msb.config.aop;

import com.personal.common.base.marking.POJOSerializable;
import com.personal.common.utils.collections.ArrayUtils;
import com.personal.common.utils.exceptions.ValidateException;
import com.personal.common.utils.exceptions.enums.ValidateExceptionEnum;
import com.personal.common.utils.validate.BeanValidator;
import com.personal.common.utils.validate.ValidatorResult;
import com.personal.common.utils.validate.type.Delete;
import com.personal.common.utils.validate.type.Insert;
import com.personal.common.utils.validate.type.Select;
import com.personal.common.utils.validate.type.Update;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Aspect
@Order(2)
@Configuration
public class CommonVerifyAspect {
    /**
     * 拦截新增方法
     * @param pjp
     * @return
     * @throws Exception
     */
    @Around("execution(* com.msb.service.impl..*.*(..))")
    public Object autoVerify(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        if(!ArrayUtils.isEmpty(args) && args.length>0){
            String name = pjp.getSignature().getName();
            for(int i=0;i<args.length;i++){
                if(args[i] instanceof POJOSerializable){
                    ValidatorResult validateResult;
                    if(name.startsWith("insert") || name.startsWith("add")){
                        validateResult = BeanValidator.validateResult(args[i],Insert.class);
                    }else if(name.startsWith("update") || name.startsWith("modify")){
                        validateResult = BeanValidator.validateResult(args[i],Update.class);
                    }else if(name.startsWith("delete") || name.startsWith("del")){
                        validateResult = BeanValidator.validateResult(args[i],Delete.class);
                    }else{
                        validateResult = BeanValidator.validateResult(args[i],Select.class);
                    }
                    if (!validateResult.getFlag()){
                        throw new ValidateException(ValidateExceptionEnum.REQUEST_PARAM_FAILD.getCode(),validateResult.getErrorStr());
                    }
                }
            }
        }
        return pjp.proceed(args);
    }
}
