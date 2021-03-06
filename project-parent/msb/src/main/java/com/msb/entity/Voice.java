package com.msb.entity;

import com.msb.common.base.entity.BaseWeChatEntity;

/**
 * 语音识别参数
 * @author ylw
 * @date 18-11-19 下午1:11
 * @param
 * @return
 */
public class Voice extends BaseWeChatEntity {
    private String text;
    private String userId;
    private String operationId;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
}
