package com.personal.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.personal.common.base.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 账单
 * </p>
 *
 * @author ylw
 * @since 2018-05-13
 */
@TableName("t_bill")
public class Bill extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 客户名称
     */
    @TableField("customer_name")
    private String customerName;
    /**
     * 客户电话
     */
    @TableField("customer_phone")
    private String customerPhone;
    /**
     * 客户身份证号
     */
    @TableField("customer_id_card")
    private String customerIdCard;
    /**
     * 客户所在县城
     */
    @TableField("city_name")
    private String cityName;
    /**
     * 客户所在市场
     */
    @TableField("market_name")
    private String marketName;
    /**
     * 客户单位名称
     */
    @TableField("customer_unit")
    private String customerUnit;
    /**
     * 账单号
     */
    @TableField("bill_sn")
    private String billSn;
    /**
     * 账单号类型
     */
    @TableField("bill_sn_type")
    private String billSnType;
    /**
     * 是否为对等账单（否，是）
     */
    @TableField("is_peer_bill")
    private String isPeerBill;
    /**
     * 记账日期
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField("bill_date")
    private Date billDate;
    /**
     * 交易状态
     */
    @TableField("business_status")
    private String businessStatus;
    /**
     * 总价格
     */
    @TableField("total_price")
    private BigDecimal totalPrice;
    /**
     * 是否已领取虚拟币
     */
    @TableField("is_receive")
    private String isReceive;
    /**
     * 邮费
     */
    private BigDecimal freight;
    /**
     * 实收总价格
     */
    @TableField("actual_total_price")
    private BigDecimal actualTotalPrice;
    /**
     * 账单状态（已付，已收，应付，应收）
     */
    @TableField("bill_status")
    private String billStatus;
    /**
     * 账期天数
     */
    @TableField("expect_pay_days")
    private Integer expectPayDays;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否公开溯源信息（否，是）
     */
    @TableField("is_source")
    private String isSource;
    /**
     * 交易地点
     */
    @TableField("transaction_address")
    private String transactionAddress;
    /**
     * 创建人主键
     */
    @TableField("create_customer_id")
    private String createCustomerId;

    /**
     * 父主键
     */
    private String pid;

    /**
     * 是否置顶 (默认不置顶)
     */
    @TableField("is_top")
    private int isTop = 0;

    /**
     * 商品
     */
    @TableField(exist = false)
    private List<Goods> goods;
    /**
     * 子账单
     */
    @TableField(exist = false)
    private List<Bill> subList;

    /**
     * 反馈信息
     * 格式：operationId：productName，......
     */
    @TableField(exist = false)
    private String feedBacks;

    public String getFeedBacks() {
        return feedBacks;
    }

    public void setFeedBacks(String feedBacks) {
        this.feedBacks = feedBacks;
    }

    public List<Goods> getGoods() {
        return goods;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }

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

    public String getBillSn() {
        return billSn;
    }

    public void setBillSn(String billSn) {
        this.billSn = billSn;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public List<Bill> getSubList() {
        return subList;
    }

    public void setSubList(List<Bill> subList) {
        this.subList = subList;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    @Override
    public String toString() {
        return "Bill{" +
        "id=" + super.getId() +
        ", customerName=" + customerName +
        ", customerPhone=" + customerPhone +
        ", customerIdCard=" + customerIdCard +
        ", cityName=" + cityName +
        ", marketName=" + marketName +
        ", customerUnit=" + customerUnit +
        ", billSn=" + billSn +
        ", isPeerBill=" + isPeerBill +
        ", billDate=" + billDate +
        ", businessStatus=" + businessStatus +
        ", totalPrice=" + totalPrice +
        ", billStatus=" + billStatus +
        ", expectPayDays=" + expectPayDays +
        ", remark=" + remark +
        ", isSource=" + isSource +
        ", isReceive=" + isReceive +
        ", freight=" + freight +
        ", actualTotalPrice=" + actualTotalPrice +
        ", transactionAddress=" + transactionAddress +
        ", createCustomerId=" + createCustomerId +
        ", pid=" + pid +
        ", isTop=" + isTop +
        ", createTime=" + super.getCreateTime() +
        ", updateTime=" + super.getUpdateTime() +
        "}";
    }
}
