package com.personal.common.exception;
/**
 * 业务异常类
 * @author ylw
 * @date 18-3-22 下午1:14
 * @param
 * @return
 */
public class BizException extends Exception {
    public BizException(){}

    public BizException(String message){
        super(message);
    }
}
