package com.msb.common.utils.exceptions.valid;

/**
 * 非法错误信息记录
 * @author ylw
 * @date 18-7-10 下午3:39
 * @param
 * @return
 */
public class ValidErrorInfo {
    private String field;
    private String errMsg;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
