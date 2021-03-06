package com.msb.requestParam;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.msb.common.base.BaseQueryParam;
import com.msb.common.base.marking.POJOSerializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 账单分页查询接口
 * @author ylw
 * @date 18-7-15 上午10:29
 * @param
 * @return
 */
@ApiModel(value="账单多条件查询参数",description="账单多条件查询参数")
public class BillQueryParam extends BaseQueryParam implements POJOSerializable{
    private static final long serialVersionUID = -2368778921048607015L;
    // 创建人主键
    @ApiModelProperty(value="创建人主键，不用传，默认当前登录用户主键",name="createCustomerId")
    private String createCustomerId;
    // 开始日期
    @ApiModelProperty(value="开始日期",name="startDate",example = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startDate;
    // 结束日期
    @ApiModelProperty(value="结束日期",name="endDate",example = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endDate;
    // 客户名称
    @ApiModelProperty(value="客户名称",name="customerName")
    private String customerName;
    // 客户手机号
    @ApiModelProperty(value="客户手机号",name="customerPhone")
    private String customerPhone;
    // 交易品种
    @ApiModelProperty(value="交易品种",name="goodsName")
    private String goodsName;
    // 交易类型
    @ApiModelProperty(value="交易类型",name="businessStatus")
    private String businessStatus;
    // 账单状态
    @ApiModelProperty(value="账单状态",name="billStatus")
    private String billStatus;
    // 是否为商品
    @ApiModelProperty(value="是否为商品",name="isGoods")
    private String isGoods;

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

    public String getIsGoods() {
        return isGoods;
    }

    public void setIsGoods(String isGoods) {
        this.isGoods = isGoods;
    }
}
