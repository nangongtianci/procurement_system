package com.msb.common.utils.exceptions.valid;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.hibernate.validator.internal.util.CollectionHelper.newArrayList;

/**
 * 处理@valid错误信息
 * @author ylw
 * @date 18-7-10 下午3:39
 * @param
 * @return
 */
public class ValidErrorUtils {

    public static List<ValidErrorInfo> changgeValidError(BindingResult bindingResult){

        List<ValidErrorInfo> errorInfos = newArrayList();

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            ValidErrorInfo validErrorInfo = new ValidErrorInfo();
            validErrorInfo.setField(fieldError.getField());
            validErrorInfo.setErrMsg(fieldError.getDefaultMessage());
            errorInfos.add(validErrorInfo);
        }
        return errorInfos;

    }
}
