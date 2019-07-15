package com.msb.entity.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.msb.common.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 上下游账务查看 账单列表
 * @author ylw
 * @date 18-10-16 下午2:28
 * @param
 * @return
 */
@ApiModel(value="上下游账务查看<账单列表>",description="上下游账务查看<账单列表>")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class BillInfoForUpAndDownStreamVO extends BaseVO{
    /**
     * 品名
     */
    @ApiModelProperty(value="品名",name="name",required = true)
    private String name;
    /**
     * 数量
     */
    @ApiModelProperty(value="数量",name="number",required = true)
    private float number;
    /**
     * 重量
     */
    @ApiModelProperty(value="重量",name="weight",required = true)
    private int weight;
    /**
     * 重量单位
     */
    @ApiModelProperty(value="重量单位",name="weightUnit",required = true)
    private int weightUnit;
    /**
     * 单价
     */
    @ApiModelProperty(value="单价",name="price",required = true)
    private BigDecimal price;
    /**
     * 其他费用
     */
    @ApiModelProperty(value="其他费用",name="otherCost",required = true)
    private BigDecimal otherCost;
    /**
     * 应付或应收金额
     */
    @ApiModelProperty(value="应付或应收金额",name="receiveOrPaymentPrice",required = true)
    private BigDecimal receiveOrPaymentAmount;
    /**
     * 是否对等
     */
    @ApiModelProperty(value="是否对等",name="isPeer",required = true)
    private String isPeer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getNumber() {
        return number;
    }

    public void setNumber(float number) {
        this.number = number;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getOtherCost() {
        return otherCost;
    }

    public void setOtherCost(BigDecimal otherCost) {
        this.otherCost = otherCost;
    }

    public BigDecimal getReceiveOrPaymentAmount() {
        return receiveOrPaymentAmount;
    }

    public void setReceiveOrPaymentAmount(BigDecimal receiveOrPaymentAmount) {
        this.receiveOrPaymentAmount = receiveOrPaymentAmount;
    }

    public String getIsPeer() {
        return isPeer;
    }

    public void setIsPeer(String isPeer) {
        this.isPeer = isPeer;
    }

    public int getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(int weightUnit) {
        this.weightUnit = weightUnit;
    }

    @Override
    public String toString() {
        return "BillInfoForUpAndDownStreamVO{" +
                "name='" + name + '\'' +
                ", number=" + number +
                ", weight=" + weight +
                ", weightUnit=" + weightUnit +
                ", price=" + price +
                ", otherCost=" + otherCost +
                ", receiveOrPaymentAmount=" + receiveOrPaymentAmount +
                ", isPeer='" + isPeer + '\'' +
                '}';
    }
}
