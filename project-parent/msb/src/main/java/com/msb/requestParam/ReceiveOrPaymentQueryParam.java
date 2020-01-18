package com.msb.requestParam;

import com.msb.common.base.marking.POJOSerializable;
import com.msb.common.base.page.BasePageQueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 上下游账务查看账单多条件实体
 * @author ylw
 * @date 18-7-15 上午10:29
 * @param
 * @return
 */
@ApiModel(value="上下游账务查看账单多条件实体",description="上下游账务查看账单多条件实体")
public class ReceiveOrPaymentQueryParam extends BasePageQueryParam implements POJOSerializable {
    private static final long serialVersionUID = -2368778921048607015L;
    // 用户主键
    @ApiModelProperty(value="用户主键",name="createId",hidden = true)
    private String createId;
    // 客户主键
    @ApiModelProperty(value="客户主键",name="customerId",required = true)
    private String customerId;
    // 是否上游
    @ApiModelProperty(value="是否上游",name="isTop",required = true)
    private String isTop;

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getIsTop() {
        return isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }
}
