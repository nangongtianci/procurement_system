package com.msb.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.msb.common.base.entity.BaseWeChatEntity;

import java.math.BigDecimal;

/**
 * <p>
 * 账单收付款对应关系
 * </p>
 *
 * @author ylw
 * @since 2019-07-02
 */
@TableName("t_receipt_payment_record_bill")
public class ReceiptPaymentRecordBill extends BaseWeChatEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 首付款记录主键
     */
    @TableField("rp_id")
    private String rpId;
    /**
     * 账单主键
     */
    @TableField("bill_id")
    private String billId;
    /**
     * 创建人主键（自己）
     */
    @TableField("create_id")
    private String createId;
    /**
     * 0:收款，1：付款
     */
    @TableField("is_payment")
    private String isPayment;
    /**
     * 拆分后金额（单个账单还款金额）
     */
    @TableField("split_amount")
    private BigDecimal splitAmount;


    public String getRpId() {
        return rpId;
    }

    public void setRpId(String rpId) {
        this.rpId = rpId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getIsPayment() {
        return isPayment;
    }

    public void setIsPayment(String isPayment) {
        this.isPayment = isPayment;
    }

    public BigDecimal getSplitAmount() {
        return splitAmount;
    }

    public void setSplitAmount(BigDecimal splitAmount) {
        this.splitAmount = splitAmount;
    }

    @Override
    public String toString() {
        return "ReceiptPaymentRecordBill{" +
        ", rpId=" + rpId +
        ", billId=" + billId +
        ", createId=" + createId +
        ", isPayment=" + isPayment +
        ", splitAmount=" + splitAmount +
        "}";
    }
}
