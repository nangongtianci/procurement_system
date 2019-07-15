package com.msb.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.msb.common.base.entity.BaseWeChatEntity;

/**
 * <p>
 * 账单
 * </p>
 *
 * @author ylw
 * @since 2019-06-05
 */
@TableName("t_bill")
public class Bill extends BaseWeChatEntity {

    private static final long serialVersionUID = 1L;
    /**
     * 用户主键
     */
    @TableField(exist = false)
    private Customer customer;

    /**
     * 客户主键
     */
    private String cid;
    /**
     * 父主键
     */
    private String pid;
    /**
     * 交易状态(买入，卖出，盘赢，盘损，其他费用)
     */
    @TableField("business_status")
    private String businessStatus;
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
     * 账单号
     */
    @TableField("bill_sn")
    private String billSn;
    /**
     * 其他费用
     */
    @TableField("other_cost")
    private BigDecimal otherCost;
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
    /**
     * 记账日期
     */
    @TableField("bill_date")
    private Date billDate;
    /**
     * 是否已领取虚拟币（否，是）
     */
    @TableField("is_receive")
    private String isReceive;
    /**
     * 创建人主键
     */
    @TableField("create_customer_id")
    private String createCustomerId;
    /**
     * 货品|商品
     */
    @TableField(exist = false)
    private BillGoods billGoods;
    /**
     * 子账单
     */
    @TableField(exist = false)
    private List<Bill> subBills;
    /**
     * 反馈信息
     * 格式：operationId：productName，......
     */
    @TableField(exist = false)
    private String feedBacks;
    /**
     * 源账单主键
     */
    @TableField(exist = false)
    private String sourceBillId;
    /**
     * 对等，不对等
     */
    @TableField(exist = false)
    private String isPeer;

    public String getIsPeer() {
        return isPeer;
    }

    public void setIsPeer(String isPeer) {
        this.isPeer = isPeer;
    }

    public String getSourceBillId() {
        return sourceBillId;
    }

    public void setSourceBillId(String sourceBillId) {
        this.sourceBillId = sourceBillId;
    }

    public List<Bill> getSubBills() {
        return subBills;
    }

    public void setSubBills(List<Bill> subBills) {
        this.subBills = subBills;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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

    public String getBillSn() {
        return billSn;
    }

    public void setBillSn(String billSn) {
        this.billSn = billSn;
    }

    public BigDecimal getOtherCost() {
        return otherCost;
    }

    public void setOtherCost(BigDecimal otherCost) {
        this.otherCost = otherCost;
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

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public String getIsReceive() {
        return isReceive;
    }

    public void setIsReceive(String isReceive) {
        this.isReceive = isReceive;
    }

    public String getCreateCustomerId() {
        return createCustomerId;
    }

    public void setCreateCustomerId(String createCustomerId) {
        this.createCustomerId = createCustomerId;
    }

    public BillGoods getBillGoods() {
        return billGoods;
    }

    public void setBillGoods(BillGoods billGoods) {
        this.billGoods = billGoods;
    }

    public String getFeedBacks() {
        return feedBacks;
    }

    public void setFeedBacks(String feedBacks) {
        this.feedBacks = feedBacks;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "customer=" + customer +
                ", cid='" + cid + '\'' +
                ", pid='" + pid + '\'' +
                ", businessStatus='" + businessStatus + '\'' +
                ", billStatus='" + billStatus + '\'' +
                ", expectPayDays=" + expectPayDays +
                ", remark='" + remark + '\'' +
                ", billSn='" + billSn + '\'' +
                ", otherCost=" + otherCost +
                ", totalPrice=" + totalPrice +
                ", actualTotalPrice=" + actualTotalPrice +
                ", billDate=" + billDate +
                ", isReceive='" + isReceive + '\'' +
                ", createCustomerId='" + createCustomerId + '\'' +
                ", billGoods=" + billGoods +
                ", subBills=" + subBills +
                ", feedBacks='" + feedBacks + '\'' +
                ", sourceBillId='" + sourceBillId + '\'' +
                ", isPeer='" + isPeer + '\'' +
                '}';
    }
}
