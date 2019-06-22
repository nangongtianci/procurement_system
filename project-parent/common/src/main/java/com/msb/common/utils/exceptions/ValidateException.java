package com.msb.common.utils.exceptions;


import com.msb.common.utils.exceptions.enums.ValidateExceptionEnum;

/**
 * @author ylw
 * @version V1.0
 * @Title: ValidateException.java
 * @Package com.ftms.cloud.common.exceptions
 * @Description: hibernate validate异常 8001
 * @date 2018-07-10 17:25:04
 */
public class ValidateException extends BizException {

    public ValidateException(int code, String msg) {
        super(code, msg);
    }

    /**
     * 构造方法
     *
     * @param validateExceptionEnum
     */
    public ValidateException(ValidateExceptionEnum validateExceptionEnum) {
        super(validateExceptionEnum.getCode(), validateExceptionEnum.getMsg());
    }
}
