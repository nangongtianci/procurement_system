package com.personal.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.personal.common.base.BaseEntity;
import org.springframework.data.annotation.Transient;

import java.util.Date;

/**
 * <p>
 * 客户信息
 * </p>
 *
 * @author ylw
 * @since 2018-05-13
 */
@TableName("t_customer")
public class Customer extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户姓名
     */
    private String name;
    /**
     * 密码
     */
    private String password;
    /**
     * 秘钥
     */
    @TableField("secret_key")
    private String secretKey;
    /**
     * 常用手机号
     */
    private String phone;
    /**
     * 身份证
     */
    @TableField("id_card")
    private String idCard;
    /**
     * 公司名称(单位名称)
     */
    @TableField("company_name")
    private String companyName;
    /**
     * 市场名称
     */
    @TableField("market_name")
    private String marketName;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 证件路径,2张图片以逗号相分割
     */
    @TableField("id_card_path")
    private String idCardPath;
    /**
     * 是否同意协议（yes:同意，no:不同意）
     */
    @TableField("is_agree_protocol")
    private String isAgreeProtocol;
    /**
     * 登录时间
     */
    @TableField("login_time")
    private Date loginTime;
    /**
     * 是否在线(yes:在线,no:不在线)
     */
    private String status;
    /**
     * 扩展字段
     */
    /**校验码**/
    @TableField(exist = false)
    private String checkCode;
//    @Transient
//    private String checkCodeToken;

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdCardPath() {
        return idCardPath;
    }

    public void setIdCardPath(String idCardPath) {
        this.idCardPath = idCardPath;
    }

    public String getIsAgreeProtocol() {
        return isAgreeProtocol;
    }

    public void setIsAgreeProtocol(String isAgreeProtocol) {
        this.isAgreeProtocol = isAgreeProtocol;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Customer{" +
        "id=" + super.getId() +
        ", name=" + name +
        ", password=" + password +
        ", secretKey=" + secretKey +
        ", phone=" + phone +
        ", idCard=" + idCard +
        ", companyName=" + companyName +
        ", marketName=" + marketName +
        ", email=" + email +
        ", idCardPath=" + idCardPath +
        ", isAgreeProtocol=" + isAgreeProtocol +
        ", loginTime=" + loginTime +
        ", status=" + status +
        ", createTime=" + super.getCreateTime() +
        ", updateTime=" + super.getUpdateTime() +
        "}";
    }
}
