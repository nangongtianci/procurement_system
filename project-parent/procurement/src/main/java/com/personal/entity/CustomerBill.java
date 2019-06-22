package com.personal.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.msb.common.base.entity.BaseWeChatEntity;

/**
 * <p>
 * 客户账单关联表
 * </p>
 *
 * @author ylw
 * @since 2018-05-13
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@TableName("t_customer_bill_link")
public class CustomerBill extends BaseWeChatEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户主键
     */
    private String cid;
    /**
     * 账单主键
     */
    private String bid;
    /**
     * 主账单，共享账单
     */
    @TableField("master_share")
    private String masterShare;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getMasterShare() {
        return masterShare;
    }

    public void setMasterShare(String masterShare) {
        this.masterShare = masterShare;
    }

    @Override
    public String toString() {
        return "CustomerBill{" +
                "cid='" + cid + '\'' +
                ", bid='" + bid + '\'' +
                ", masterShare='" + masterShare + '\'' +
                '}';
    }
}
