package com.personal.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.msb.common.base.entity.BaseWeChatEntity;

/**
 * <p>
 * 申请贷款记录
 * </p>
 *
 * @author ylw
 * @since 2018-05-13
 */
@TableName("t_apply_loan_record")
public class ApplyLoanRecord extends BaseWeChatEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 需求金额
     */
    @TableField("need_money")
    private BigDecimal needMoney;
    /**
     * 使用周期
     */
    @TableField("use_cycle")
    private Integer useCycle;
    /**
     * 可接受利率
     */
    @TableField("accept_rate")
    private Float acceptRate;
    /**
     * 可抵押物品（无，房子，车，货品）
     */
    @TableField("mortgage_thing")
    private String mortgageThing;
    /**
     * 资金用途
     */
    private String purpose;
    /**
     * 创建人主键
     */
    @TableField("create_customer_id")
    private String createCustomerId;


    public BigDecimal getNeedMoney() {
        return needMoney;
    }

    public void setNeedMoney(BigDecimal needMoney) {
        this.needMoney = needMoney;
    }

    public Integer getUseCycle() {
        return useCycle;
    }

    public void setUseCycle(Integer useCycle) {
        this.useCycle = useCycle;
    }

    public Float getAcceptRate() {
        return acceptRate;
    }

    public void setAcceptRate(Float acceptRate) {
        this.acceptRate = acceptRate;
    }

    public String getMortgageThing() {
        return mortgageThing;
    }

    public void setMortgageThing(String mortgageThing) {
        this.mortgageThing = mortgageThing;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getCreateCustomerId() {
        return createCustomerId;
    }

    public void setCreateCustomerId(String createCustomerId) {
        this.createCustomerId = createCustomerId;
    }

    @Override
    public String toString() {
        return "ApplyLoanRecord{" +
        "id=" + super.getId() +
        ", needMoney=" + needMoney +
        ", useCycle=" + useCycle +
        ", acceptRate=" + acceptRate +
        ", mortgageThing=" + mortgageThing +
        ", purpose=" + purpose +
        ", createCustomerId=" + createCustomerId +
        ", createTime=" + super.getCreateTime() +
        ", updateTime=" + super.getUpdateTime() +
        "}";
    }
}
