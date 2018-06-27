package com.personal.config.aop;

import com.personal.common.base.BaseEntity;
import com.personal.common.utils.base.UUIDUtils;
import com.personal.common.utils.collections.ArrayUtils;
import com.personal.common.utils.exceptions.BizException;
import com.personal.common.utils.exceptions.enums.BizExceptionEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 自动装配通用数据
 * @author ylw  
 * @date 18-5-14 下午2:31
 * @param   
 * @return   
 */
@Aspect
@Order(1)
@Configuration
public class CommonDataAspect {
    /**
     * 拦截新增方法
     * @param pjp
     * @return
     * @throws Exception
     */
    @Around("execution(* com.personal.controller.*.*(..)) && @annotation(com.personal.common.annotation.InsertMethodFlag)")
    public Object insertData(ProceedingJoinPoint pjp) throws Exception{
        try {
            Object[] args = pjp.getArgs();
            if(!ArrayUtils.isEmpty(args) && args.length == 1 && args[0] instanceof BaseEntity) {
                setInsertData(args[0]);
            }
            return pjp.proceed(args);
        } catch (Throwable throwable) {
            throw new BizException(BizExceptionEnum.COMMON_PARAM_SET_ERROR);
        }
    }

    /**
     * 拦截更新方法
     * @param pjp
     * @return
     * @throws Exception
     */
    @Around("execution(* com.personal.controller.*.*(..)) && @annotation(com.personal.common.annotation.UpdateMethodFlag)")
    public Object updateData(ProceedingJoinPoint pjp) throws Exception{
        try {
            Object[] args = pjp.getArgs();
            if(!ArrayUtils.isEmpty(args) && args.length == 1 && args[0] instanceof BaseEntity) {
                setUpdateData(args[0]);
            }
            return pjp.proceed(args);
        } catch (Throwable throwable) {
            throw new BizException(BizExceptionEnum.COMMON_PARAM_SET_ERROR);
        }
    }

    /**
     * 针对新增方法设置通用参数
     * @param obj
     * @throws Exception
     */
    private void setInsertData(Object obj) throws Exception {
        if(!(obj instanceof BaseEntity)){
            return;
        }

        Field[] fields = ArrayUtils.concatAll(obj.getClass().getSuperclass().getDeclaredFields(),obj.getClass().getDeclaredFields());
        Date date = new Date();
        for(Field field : fields){
            field.setAccessible(true);

            if(field.getName().equalsIgnoreCase("id")){
                field.set(obj,UUIDUtils.getUUID());
            }

            if(field.getName().equalsIgnoreCase("createTIme") || field.getName().equalsIgnoreCase("updateTime")){
                field.set(obj,date);
            }

            if(field.getType().toString().equalsIgnoreCase("interface java.util.List")){
                if(field.get(obj) != null){
                    List<Object> list = (List<Object>) field.get(obj);
                    for(Object tmp : list){
                        setInsertData(tmp);
                    }
                }
            }

            if(field.getType().toString().equalsIgnoreCase("interface java.util.Set")){
                if(field.get(obj) != null){
                    Set<Object> set = (Set<Object>) field.get(obj);
                    for(Object tmp : set){
                        setInsertData(tmp);
                    }
                }
            }

            if(field.getType().toString().matches("^class \\[L[a-zA-Z0-9.]+")){
                if(field.get(obj) != null){
                    Object[] array = (Object[]) field.get(obj);
                    for(Object tmp : array){
                        setInsertData(tmp);
                    }
                }
            }
        }
    }

    /**
     * 针对更新方法设置通用参数
     * @param obj
     * @throws Exception
     */
    private void setUpdateData(Object obj) throws Exception {
        if(!(obj instanceof BaseEntity)){
            return;
        }

        Field[] fields = ArrayUtils.concatAll(obj.getClass().getSuperclass().getDeclaredFields(),obj.getClass().getDeclaredFields());
        Date date = new Date();
        for(Field field : fields){
            field.setAccessible(true);

            if(field.getName().equalsIgnoreCase("updateTime")){
                field.set(obj,date);
            }

            if(field.getType().toString().equalsIgnoreCase("interface java.util.List")){
                if(field.get(obj) != null){
                    List<Object> list = (List<Object>) field.get(obj);
                    for(Object tmp : list){
                        setUpdateData(tmp);
                    }
                }
            }

            if(field.getType().toString().equalsIgnoreCase("interface java.util.Set")){
                if(field.get(obj) != null){
                    Set<Object> set = (Set<Object>) field.get(obj);
                    for(Object tmp : set){
                        setUpdateData(tmp);
                    }
                }
            }

            if(field.getType().toString().matches("^class \\[L[a-zA-Z0-9.]+")){
                if(field.get(obj) != null){
                    Object[] array = (Object[]) field.get(obj);
                    for(Object tmp : array){
                        setUpdateData(tmp);
                    }
                }
            }
        }
    }
}
