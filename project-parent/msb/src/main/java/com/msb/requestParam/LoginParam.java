package com.msb.requestParam;

import com.msb.common.base.marking.POJOSerializable;

/**
 * 登录信息
 */
public class LoginParam implements POJOSerializable {
    /**
     * 明文,加密数据
     */
    private String encryptedData;
    /**
     * 加密算法的初始向量
     */
    private String iv;
    /**
     * sessionKey
     */
    private String sessionKey;
    /**
     * 头像
     */
    private String icon;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 性别
     */
    private String sex;

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
