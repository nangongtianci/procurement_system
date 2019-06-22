package com.msb.common.utils.exceptions.enums;

/**
 * 校验异常枚举
 * @author ylw
 * @date 18-7-10 下午3:37
 * @param
 * @return
 */
public enum ValidateExceptionEnum {
    INSERT_FAILD(8001, "Insert校验失败"),
    UPDATE_FAILD(8002, "Update校验失败"),
    DELETE_FAILD(8003, "Delete校验失败"),
    SELECT_FAILD(8004, "Select校验失败"),
    REQUEST_PARAM_FAILD(8005, "参数校验失败");

    private int code;
    private String msg;

    /**
     * 构造方法
     *
     * @param code
     * @param msg
     */
    ValidateExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
