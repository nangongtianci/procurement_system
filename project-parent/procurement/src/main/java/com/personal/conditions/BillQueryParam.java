package com.personal.conditions;

import com.msb.common.base.BaseQueryParam;
import com.msb.common.base.marking.POJOSerializable;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 账单分页查询接口
 * @author ylw
 * @date 18-7-15 上午10:29
 * @param
 * @return
 */
public class BillQueryParam extends BaseQueryParam implements POJOSerializable{
    private static final long serialVersionUID = -2368778921048607015L;
    // 创建人主键
    private String createCustomerId;
    // 开始日期
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    // 结束日期
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    // 客户名称
    private String customerName;
    // 客户手机号
    private String customerPhone;
    // 交易品种
    private String goodsName;
    // 交易类型
    private String businessStatus;
    // 账单状态
    private String billStatus;

    public String getCreateCustomerId() {
        return createCustomerId;
    }

    public void setCreateCustomerId(String createCustomerId) {
        this.createCustomerId = createCustomerId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
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
}
