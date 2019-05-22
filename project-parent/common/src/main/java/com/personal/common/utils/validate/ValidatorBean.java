package com.personal.common.utils.validate;

import java.io.Serializable;

/**
 * 校验结果
 * @author ylw
 * @date 18-7-10 下午5:37
 * @param
 * @return
 */
public class ValidatorBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字段名
     */
    private String filed;

    /**
     * 错误信息
     */
    private String errorMsg;

    public String getFiled() {
        return filed;
    }

    public void setFiled(String filed) {
        this.filed = filed;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
