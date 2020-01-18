package com.msb.requestParam;

import com.msb.common.base.marking.POJOSerializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 收|付款请求参数
 * @author ylw
 * @date 18-7-15 上午10:29
 * @param
 * @return
 */
@ApiModel(value="付款请求参数",description="付款请求参数")
public class ReceiveOrPaymentParam implements POJOSerializable {
    private static final long serialVersionUID = -2368778921048607015L;
    // 账单主键列表
    @ApiModelProperty(value="账单主键列表",name="billIds")
    private String[] billIds;
    // 是否上游
    @ApiModelProperty(value="1:上游，0：下游",example = "1",name="isTop",required = true)
    private String isTop;
    // 金额
    @ApiModelProperty(value="金额",example = "100.0",name="amount",required = true)
    private BigDecimal amount;
    // 创建人主键
    @ApiModelProperty(value="创建人主键",name="createId",hidden = true)
    private String createId;

    public String[] getBillIds() {
        return billIds;
    }

    public void setBillIds(String[] billIds) {
        this.billIds = billIds;
    }

    public String getIsTop() {
        return isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }
}
