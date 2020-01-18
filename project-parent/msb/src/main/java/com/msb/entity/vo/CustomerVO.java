package com.msb.entity.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.msb.common.base.BaseVO;

import java.math.BigDecimal;

/**
 * 客户VO
 * @author ylw
 * @date 18-7-23 下午2:17
 * @param
 * @return
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class CustomerVO extends BaseVO{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
