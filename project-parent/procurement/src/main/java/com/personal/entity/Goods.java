package com.personal.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.personal.common.base.BaseEntity;

import java.math.BigDecimal;

/**
 * <p>
 * 订单商品信息
 * </p>
 *
 * @author ylw
 * @since 2018-05-13
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@TableName("t_goods")
public class Goods extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 品名
     */
    private String name;
    /**
     * 商品单价
     */
    private BigDecimal price;
    /**
     * 商品数量
     */
    private float number;
    /**
     * 单个商品总价
     */
    private BigDecimal amount;
    /**
     * 商品单位（吨，公斤，箱）
     */
    @TableField("weight_unit")
    private String weightUnit;
    /**
     * 安全信息检测信息
     */
    @TableField("security_detection_info")
    private String securityDetectionInfo;
    /**
     * 账单主键（一对多关系）
     */
    @TableField("bill_id")
    private String billId;
    /**
     * 创建人主键
     */
    @TableField("create_customer_id")
    private String createCustomerId;
    /**
     * 商品管理图片
     */
    @TableField("code_img_path")
    private String codeImgPath;
    /**
     * 代码管理主键
     */
    private String cmid;


    public float getNumber() {
        return number;
    }

    public void setNumber(float number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getSecurityDetectionInfo() {
        return securityDetectionInfo;
    }

    public void setSecurityDetectionInfo(String securityDetectionInfo) {
        this.securityDetectionInfo = securityDetectionInfo;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getCreateCustomerId() {
        return createCustomerId;
    }

    public void setCreateCustomerId(String createCustomerId) {
        this.createCustomerId = createCustomerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCodeImgPath() {
        return codeImgPath;
    }

    public void setCodeImgPath(String codeImgPath) {
        this.codeImgPath = codeImgPath;
    }

    public String getCmid() {
        return cmid;
    }

    public void setCmid(String cmid) {
        this.cmid = cmid;
    }

    @Override
    public String toString() {
        return "Goods{" +
        "id=" + super.getId() +
        ", name=" + name +
        ", price=" + price +
        ", number=" + number +
        ", amount" + amount +
        ", weightUnit=" + weightUnit +
        ", securityDetectionInfo=" + securityDetectionInfo +
        ", billId=" + billId +
        ", createCustomerId=" + createCustomerId +
        ", codeImgPath=" + codeImgPath+
        ", cmid=" + cmid +
        ", createTime=" + super.getCreateTime() +
        ", updateTime=" + super.getUpdateTime() +
        "}";
    }
}
