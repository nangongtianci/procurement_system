package com.personal.common.utils.validate;


import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;


/**
 * 校验类
 * @author ylw
 * @date 18-7-10 下午5:42
 * @param
 * @return
 */
public class BeanValidator {
    private static Logger logger = LoggerFactory.getLogger(BeanValidator.class);
    static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private BeanValidator() {

    }

    /**
     * 校验
     * @param t
     * @param groups
     * @return
     */
    public static <T> ValidatorResult validateResult(T t, Class<?>... groups) {
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> vs = validator.validate(t, groups);

        Map<String, String> errorMap = new HashMap<>();

        for (ConstraintViolation<T> v : vs) {
            Class<?> annClazz = v.getConstraintDescriptor().getAnnotation().annotationType();
            // key:msg  value:field
            errorMap.put(v.getMessage(), v.getPropertyPath().toString());
            logger.error("<br>::::hibernate validate ERROR:" + v.getRootBean().getClass().getName() + "." + v.getPropertyPath().toString()
                    + " violate rule:@" + annClazz.getSimpleName() + ",message:" + v.getMessage());
        }

        // 合并相同字段，不同的错误信息
        Multimap<String, String> joinMap = ArrayListMultimap.create();
        Iterator<Map.Entry<String, String>> it = errorMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String key = entry.getKey();
            String value = entry.getValue();
            joinMap.put(value, key); //key value对调位置
        }
        List<ValidatorBean> errors = new ArrayList<>(); // 遍历joinMap，存入list

        String errorStr = "";
        Set<String> keySet = joinMap.keySet();
        for (String key : keySet) {
            ValidatorBean bean = new ValidatorBean();
            bean.setFiled(key);
            Collection<String> collection = joinMap.get(key);
            String errMsg = "";
            for (String msg : collection) {
                errMsg = errMsg + msg + "！";
            }
            bean.setErrorMsg(errMsg);
            errorStr = errorStr + errMsg;
            errors.add(bean);
        }

        ValidatorResult vr = new ValidatorResult();
        vr.setFlag(errorMap.size() == 0);
        vr.setErrorObjs(errors);
        vr.setErrorStr(errorStr);
        return vr;
    }

    /**
     * 返回boolean用于判断
     * @param t
     * @param groups
     * @return
     */
    public static <T> boolean isValidated(T t, Class<?>... groups) {
        return validateResult(t,groups).getErrorObjs().size() == 0;
    }
}
