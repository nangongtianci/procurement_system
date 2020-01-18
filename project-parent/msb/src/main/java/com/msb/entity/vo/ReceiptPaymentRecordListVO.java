package com.msb.entity.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.msb.common.base.BaseVO;
import io.swagger.annotations.ApiModel;

import java.math.BigDecimal;

/**
 * 收付款列表查询结果信息
 * @author ylw
 * @date 18-10-16 下午2:28
 * @param
 * @return
 */
@ApiModel(value="收付款列表查询结果信息",description="收付款列表查询结果信息")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ReceiptPaymentRecordListVO extends BaseVO{
    /**
     * 收款|付款
     */
    private String isPayment;
    /**
     * 金额
     */
    private BigDecimal amount;

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
}
