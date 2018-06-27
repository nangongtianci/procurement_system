package com.personal.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.personal.common.base.BaseEntity;

/**
 * <p>
 * 用户与虚拟币关系
 * </p>
 *
 * @author ylw
 * @since 2018-05-22
 */
@TableName("t_customer_virtual_coin")
public class CustomerVirtualCoin extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 客户主键
     */
    @TableField("customer_id")
    private String customerId;
    /**
     * 虚拟币个数
     */
    @TableField("virtual_coin_count")
    private Integer virtualCoinCount;


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Integer getVirtualCoinCount() {
        return virtualCoinCount;
    }

    public void setVirtualCoinCount(Integer virtualCoinCount) {
        this.virtualCoinCount = virtualCoinCount;
    }

    @Override
    public String toString() {
        return "CustomerVirtualCoin{" +
        ", customerId=" + customerId +
        ", virtualCoinCount=" + virtualCoinCount +
        "}";
    }
}
