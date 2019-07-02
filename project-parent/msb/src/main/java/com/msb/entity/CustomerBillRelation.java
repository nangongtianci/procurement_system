package com.msb.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.msb.common.base.entity.BaseWeChatEntity;

import java.math.BigDecimal;

/**
 * <p>
 * 客户账单关系表
 * </p>
 *
 * @author ylw
 * @since 2019-06-27
 */
@TableName("t_customer_bill_relation")
public class CustomerBillRelation extends BaseWeChatEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 创建人主键（自己）
     */
    @TableField("create_id")
    private String createId;
    /**
     * 来自哪个客户（分享,代卖账单创建人主键）
     */
    @TableField("source_id")
    private String sourceId;
    /**
     * 客户主键（对方）
     */
    @TableField("customer_id")
    private String customerId;
    /**
     * 新建，扫描，卖货，代卖，分享
     */
    @TableField("relation_type")
    private String relationType;
    /**
     * 账单主键
     */
    @TableField("bill_id")
    private String billId;
    /**
     * 通过哪个账单生成而来
     */
    @TableField("source_bill_id")
    private String sourceBillId;
    /**
     * 交易状态,默认买入：0
     */
    @TableField("business_status")
    private String businessStatus;
    /**
     * 账单状态,默认：应付:2
     */
    @TableField("bill_status")
    private String billStatus;
    /**
     * 总价格
     */
    @TableField("total_price")
    private BigDecimal totalPrice;
    /**
     * 实收总价格
     */
    @TableField("actual_total_price")
    private BigDecimal actualTotalPrice;


    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getSourceBillId() {
        return sourceBillId;
    }

    public void setSourceBillId(String sourceBillId) {
        this.sourceBillId = sourceBillId;
    }

    public String getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getActualTotalPrice() {
        return actualTotalPrice;
    }

    public void setActualTotalPrice(BigDecimal actualTotalPrice) {
        this.actualTotalPrice = actualTotalPrice;
    }

    @Override
    public String toString() {
        return "CustomerBillRelation{" +
        ", createId=" + createId +
        ", sourceId=" + sourceId +
        ", customerId=" + customerId +
        ", relationType=" + relationType +
        ", billId=" + billId +
        ", sourceBillId=" + sourceBillId +
        ", businessStatus=" + businessStatus +
        ", billStatus=" + billStatus +
        ", totalPrice=" + totalPrice +
        ", actualTotalPrice=" + actualTotalPrice +
        "}";
    }
}
