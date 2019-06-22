package com.msb.entity.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 账单查询视图 (提供账单查询接口使用)
 * @author ylw
 * @date 18-10-16 下午2:28
 * @param
 * @return
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class BillProductsForQueryPageVO extends BillProductsForIndexPageVO{
    /**
     * 用户名称
     */
    private String customerName;
    /**
     * 用户手机号
     */
    private String customerPhone;

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

    @Override
    public String toString() {
        return "BillProductsForQueryPageVO{" +
                "customerName='" + customerName + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                '}';
    }
}
