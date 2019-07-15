package com.msb.query;

import com.msb.common.base.page.BasePageQueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Map;
@ApiModel(value="员工多条件分页查询",description="员工多条件分页查询")
public class EmployeeQueryParam extends BasePageQueryParam {
    @ApiModelProperty(value="状态(字典),0禁用，1启用",name="status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
