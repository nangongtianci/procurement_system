package com.msb.entity.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.msb.common.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 上下游列表 (上下游关系)
 * @author ylw
 * @date 18-10-16 下午2:28
 * @param
 * @return
 */
@ApiModel(value="上下游列表实例",description="上下游列表实例")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class UpAndDownStreamListVO extends BaseVO{
    /**
     * 用户名称
     */
    @ApiModelProperty(value="用户名称",name="name",required = true)
    private String name;
    /**
     * 用户头像
     */
    @ApiModelProperty(value="用户头像",name="icon",required = true)
    private String icon;
    /**
     * 用户手机号
     */
    @ApiModelProperty(value="用户手机号",name="phone",required = true)
    private String phone;
    /**
     * 交易次数
     */
    @ApiModelProperty(value="交易次数",name="ct",required = true)
    private int ct;
    /**
     * 应收
     */
    @ApiModelProperty(value="应收",name="receivable")
    private BigDecimal receivable;
    /**
     * 应付
     */
    @ApiModelProperty(value="应付",name="payable")
    private BigDecimal payable;
    /**
     * 实收总价
     */
    @ApiModelProperty(value="实收总价",name="actualTotalPrice",required = true)
    private BigDecimal actualTotalPrice;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCt() {
        return ct;
    }

    public void setCt(int ct) {
        this.ct = ct;
    }

    public BigDecimal getReceivable() {
        return receivable;
    }

    public void setReceivable(BigDecimal receivable) {
        this.receivable = receivable;
    }

    public BigDecimal getPayable() {
        return payable;
    }

    public void setPayable(BigDecimal payable) {
        this.payable = payable;
    }

    public BigDecimal getActualTotalPrice() {
        return actualTotalPrice;
    }

    public void setActualTotalPrice(BigDecimal actualTotalPrice) {
        this.actualTotalPrice = actualTotalPrice;
    }

    @Override
    public String toString() {
        return "UpAndDownStreamListVO{" +
                "name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", phone='" + phone + '\'' +
                ", ct=" + ct +
                ", receivable=" + receivable +
                ", payable=" + payable +
                ", actualTotalPrice=" + actualTotalPrice +
                '}';
    }
}
