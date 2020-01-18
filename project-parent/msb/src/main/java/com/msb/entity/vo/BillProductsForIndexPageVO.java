package com.msb.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.msb.common.base.BaseVO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 账单货品试图 (账单首页提供)
 * @author ylw
 * @date 18-10-16 下午2:28
 * @param
 * @return
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class BillProductsForIndexPageVO extends BaseVO{
    /**
     * 账单主键
     */
    private String billId;
    /**
     * 商品名称
     */
    private String goodsName;
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
     * 账单状态（已付，已收，应付，应收）
     */
    private String billStatus;
    /**
     * 总价格
     */
    private BigDecimal totalPrice;
    /**
     * 实收总价格
     */
    private BigDecimal actualTotalPrice;
    /**
     * 货品图片路径
     */
    private String productImgPath;
    /**
     * 是否置顶
     */
    private int isTop;
    /**
     * 是否对等
     */
    private int isPeer;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
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

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
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

    public String getProductImgPath() {
        return productImgPath;
    }

    public void setProductImgPath(String productImgPath) {
        this.productImgPath = productImgPath;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public int getIsPeer() {
        return isPeer;
    }

    public void setIsPeer(int isPeer) {
        this.isPeer = isPeer;
    }

    @Override
    public String toString() {
        return "BillProductsForIndexPageVO{" +
                "billId='" + billId + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", billDate=" + billDate +
                ", businessStatus='" + businessStatus + '\'' +
                ", billStatus='" + billStatus + '\'' +
                ", totalPrice=" + totalPrice +
                ", actualTotalPrice=" + actualTotalPrice +
                ", productImgPath='" + productImgPath + '\'' +
                ", isTop=" + isTop +
                ", isPeer=" + isPeer +
                '}';
    }
}
