package com.personal.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.personal.common.base.BaseVO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 首页单独使用
 * @author ylw
 * @date 18-10-16 下午2:28
 * @param
 * @return
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class BillGoodsForIndexPageVO extends BaseVO{
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 客户电话
     */
    private String customerPhone;
    /**
     * 客户身份证号
     */
    private String customerIdCard;
    /**
     * 客户所在县城
     */
    private String cityName;
    /**
     * 客户所在市场
     */
    private String marketName;
    /**
     * 客户单位名称
     */
    private String customerUnit;
    /**
     * 账单号
     */
    private String billSn;
    /**
     * 账单号类型
     */
    private String billSnType;
    /**
     * 是否为对等账单（否，是）
     */
    private String isPeerBill;
    /**
     * 记账日期
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date billDate;
    /**
     * 交易状态
     */
    private String businessStatus;
    /**
     * 总价格
     */
    private BigDecimal totalPrice;
    /**
     * 是否已领取虚拟币
     */
    private String isReceive;
    /**
     * 邮费
     */
    private BigDecimal freight;
    /**
     * 实收总价格
     */
    private BigDecimal actualTotalPrice;
    /**
     * 账单状态（已付，已收，应付，应收）
     */
    private String billStatus;
    /**
     * 账期天数
     */
    private Integer expectPayDays;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否公开溯源信息（否，是）
     */
    private String isSource;
    /**
     * 交易地点
     */
    private String transactionAddress;
    /**
     * 创建人主键
     */
    private String createCustomerId;
    /**
     * 商品名称（逗号相链接）
     */
    private String goodsNames;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerIdCard() {
        return customerIdCard;
    }

    public void setCustomerIdCard(String customerIdCard) {
        this.customerIdCard = customerIdCard;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getCustomerUnit() {
        return customerUnit;
    }

    public void setCustomerUnit(String customerUnit) {
        this.customerUnit = customerUnit;
    }

    public String getBillSn() {
        return billSn;
    }

    public void setBillSn(String billSn) {
        this.billSn = billSn;
    }

    public String getBillSnType() {
        return billSnType;
    }

    public void setBillSnType(String billSnType) {
        this.billSnType = billSnType;
    }

    public String getIsPeerBill() {
        return isPeerBill;
    }

    public void setIsPeerBill(String isPeerBill) {
        this.isPeerBill = isPeerBill;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public String getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getIsReceive() {
        return isReceive;
    }

    public void setIsReceive(String isReceive) {
        this.isReceive = isReceive;
    }

    public BigDecimal getFreight() {
        return freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    public BigDecimal getActualTotalPrice() {
        return actualTotalPrice;
    }

    public void setActualTotalPrice(BigDecimal actualTotalPrice) {
        this.actualTotalPrice = actualTotalPrice;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public Integer getExpectPayDays() {
        return expectPayDays;
    }

    public void setExpectPayDays(Integer expectPayDays) {
        this.expectPayDays = expectPayDays;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsSource() {
        return isSource;
    }

    public void setIsSource(String isSource) {
        this.isSource = isSource;
    }

    public String getTransactionAddress() {
        return transactionAddress;
    }

    public void setTransactionAddress(String transactionAddress) {
        this.transactionAddress = transactionAddress;
    }

    public String getCreateCustomerId() {
        return createCustomerId;
    }

    public void setCreateCustomerId(String createCustomerId) {
        this.createCustomerId = createCustomerId;
    }

    public String getGoodsNames() {
        return goodsNames;
    }

    public void setGoodsNames(String goodsNames) {
        this.goodsNames = goodsNames;
    }
}
