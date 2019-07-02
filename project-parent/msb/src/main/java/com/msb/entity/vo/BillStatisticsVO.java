package com.msb.entity.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.msb.common.base.BaseVO;

import java.math.BigDecimal;

/**
 * 账单统计
 * @author ylw
 * @date 18-7-23 下午2:17
 * @param
 * @return
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class BillStatisticsVO extends BaseVO{
    /**
     * 交易笔数
     */
    private int ct;
    /**
     * 合计
     */
    private BigDecimal atp;
    /**
     * 名称
     */
    private String name;

    public int getCt() {
        return ct;
    }

    public void setCt(int ct) {
        this.ct = ct;
    }

    public BigDecimal getAtp() {
        return atp;
    }

    public void setAtp(BigDecimal atp) {
        this.atp = atp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BillStatisticsVO{" +
                "ct=" + ct +
                ", atp=" + atp +
                ", name='" + name + '\'' +
                '}';
    }
}
