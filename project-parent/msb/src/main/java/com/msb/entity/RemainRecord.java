package com.msb.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.msb.common.base.entity.BaseWeChatEntity;

import java.math.BigDecimal;

/**
 * <p>
 * 留存记录
 * </p>
 *
 * @author ylw
 * @since 2019-07-02
 */
@TableName("t_remain_record")
public class RemainRecord extends BaseWeChatEntity {

    private static final long serialVersionUID = 1L;

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
     * 留存金额
     */
    @TableField("remain_amount")
    private BigDecimal remainAmount;


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


    public BigDecimal getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(BigDecimal remainAmount) {
        this.remainAmount = remainAmount;
    }

    @Override
    public String toString() {
        return "RemainRecord{" +
        ", createId=" + createId +
        ", isPayment=" + isPayment +
        ", remainAmount=" + remainAmount +
        "}";
    }
}
