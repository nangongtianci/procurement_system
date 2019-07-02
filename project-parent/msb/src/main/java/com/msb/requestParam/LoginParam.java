package com.msb.requestParam;

import com.msb.common.base.marking.POJOSerializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 登录信息
 */
@ApiModel(value="登录参数",description="登录参数")
public class LoginParam implements POJOSerializable {
    /**
     * 明文,加密数据
     */
    @ApiModelProperty(value="明文,加密数据",name="encryptedData",required = true)
    private String encryptedData;
    /**
     * 加密算法的初始向量
     */
    @ApiModelProperty(value="加密算法的初始向量",name="iv",required = true)
    private String iv;
    /**
     * sessionKey
     */
    @ApiModelProperty(value="sessionKey",name="sessionKey",required = true)
    private String sessionKey;
    /**
     * 头像
     */
    @ApiModelProperty(value="头像",name="icon",required = true)
    private String icon;
    /**
     * 昵称
     */
    @ApiModelProperty(value="昵称",name="nickName",required = true,example = "南宫天赐")
    private String nickName;
    /**
     * 性别
     */
    @ApiModelProperty(value="性别",name="sex",required = true,example = "1")
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
