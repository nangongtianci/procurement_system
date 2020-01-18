package com.msb.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.msb.common.base.entity.BaseWeChatEntity;

import java.math.BigDecimal;

/**
 * <p>
 * 收付款记录
 * </p>
 *
 * @author ylw
 * @since 2019-07-02
 */
@TableName("t_receipt_payment_record")
public class ReceiptPaymentRecord extends BaseWeChatEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 创建人主键（自己）
     */
    @TableField("create_id")
    private String createId;
    /**
     * 客户主键（对方）
     */
    @TableField("customer_id")
    private String customerId;
    /**
     * 0:收款，1：付款
     */
    @TableField("is_payment")
    private String isPayment;
    /**
     * 收款|付款金额
     */
    private BigDecimal amount;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ReceiptPaymentRecord{" +
                "createId='" + createId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", isPayment='" + isPayment + '\'' +
                ", amount=" + amount +
                '}';
    }
}
