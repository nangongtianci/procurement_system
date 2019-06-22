package com.msb.common.utils.validate;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

/**
 * 校验结果
 * @author ylw
 * @date 18-7-10 下午5:37
 * @param
 * @return
 */
public class ValidatorResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 校验是否通过标记
     */
    private Boolean flag = true;

    private List<ValidatorBean> errorObjs = Lists.newArrayList();

    private String errorStr;

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public List<ValidatorBean> getErrorObjs() {
        return errorObjs;
    }

    public void setErrorObjs(List<ValidatorBean> errorObjs) {
        this.errorObjs = errorObjs;
    }

    public String getErrorStr() {
        return errorStr;
    }

    public void setErrorStr(String errorStr) {
        this.errorStr = errorStr;
    }
}
