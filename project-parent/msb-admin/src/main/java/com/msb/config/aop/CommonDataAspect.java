package com.msb.config.aop;

import com.msb.common.base.entity.BaseAdminEntity;
import com.msb.common.base.page.AbstractPageQueryParam;
import com.msb.common.cache.RedisService;
import com.msb.common.constant.SysConstant;
import com.msb.common.utils.base.ReflectionUtils;
import com.msb.common.utils.collections.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private RedisService redisService;

    /**
     * 拦截新增方法
     * @param pjp
     * @return
     * @throws Exception
     */
    @Around("execution(* com.msb.controller.*.*(..)) && @annotation(com.msb.common.annotation.InsertMethodFlag)")
    public Object insertData(ProceedingJoinPoint pjp) throws Throwable {
        try {
            Object[] args = pjp.getArgs();
            if(!ArrayUtils.isEmpty(args) && args.length>0){
                for(int i=0;i<args.length;i++){
                    if(args[i] instanceof BaseAdminEntity){
                        setInsertData(args[i]);
                    }
                }
            }
            return pjp.proceed(args);
        } catch (Throwable throwable) {
            throw throwable;
        }
    }

    /**
     * 拦截更新方法
     * @param pjp
     * @return
     * @throws Exception
     */
    @Around("execution(* com.msb.controller.*.*(..)) && @annotation(com.msb.common.annotation.UpdateMethodFlag)")
    public Object updateData(ProceedingJoinPoint pjp) throws Throwable{
        try {
            Object[] args = pjp.getArgs();
            if(!ArrayUtils.isEmpty(args) && args.length>0){
                for(int i=0;i<args.length;i++){
                    if(args[i] instanceof BaseAdminEntity){
                        setUpdateData(args[i]);
                    }
                }
            }
            return pjp.proceed(args);
        } catch (Throwable throwable) {
            throw throwable;
        }
    }


    /**
     * 分页查询方法
     * @param pjp
     * @return
     * @throws Exception
     */
    @Around("execution(* com.msb.controller.*.*(..)) && @annotation(com.msb.common.annotation.PageQueryMethodFlag)")
    public Object pageQueryData(ProceedingJoinPoint pjp) throws Throwable{
        try {
            Object[] args = pjp.getArgs();
            if(!ArrayUtils.isEmpty(args) && args.length>0){
                for(int i=0;i<args.length;i++){
                    if(args[i] instanceof AbstractPageQueryParam){
                        int pageSize = (int)ReflectionUtils.invokeGetter(args[i],"pageSize");
                        if(pageSize == SysConstant.PAGE_SIZE_DEFAULT){
                            ReflectionUtils.invokeSetter(args[i],"pageSize",SysConstant.PAGE_SIZE_DEFAULT);
                        }
                    }
                }
            }
            return pjp.proceed(args);
        } catch (Throwable throwable) {
            throw throwable;
        }
    }

    /**
     * 针对新增方法设置通用参数
     * @param obj
     * @throws Exception
     */
    private void setInsertData(Object obj) throws Exception {
        if(!(obj instanceof BaseAdminEntity)){
            return;
        }

        Field[] fields = ArrayUtils.concatAll(obj.getClass().getSuperclass().getDeclaredFields(),obj.getClass().getDeclaredFields());
        Date date = new Date();
        for(Field field : fields){
            field.setAccessible(true);

            if(field.getName().equalsIgnoreCase("createUser") || field.getName().equalsIgnoreCase("updateUser")){
                field.set(obj,"ylw");
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
        if(!(obj instanceof BaseAdminEntity)){
            return;
        }

        Field[] fields = ArrayUtils.concatAll(obj.getClass().getSuperclass().getDeclaredFields(),obj.getClass().getDeclaredFields());
        Date date = new Date();
        for(Field field : fields){
            field.setAccessible(true);

            if(field.getName().equalsIgnoreCase("updateTime")){
                field.set(obj,date);
            }

            if(field.getName().equalsIgnoreCase("createUser")){
                field.set(obj,"ylw");
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
