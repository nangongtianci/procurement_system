package com.msb.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.msb.common.base.entity.BaseWeChatEntity;

/**
 * <p>
 * 客户关系表
 * </p>
 *
 * @author ylw
 * @since 2019-06-05
 */
@TableName("t_customer_relation")
public class CustomerRelation extends BaseWeChatEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 上游主键
     */
    @TableField("up_id")
    private String upId;
    /**
     * 下游主键
     */
    @TableField("down_id")
    private String downId;
    /**
     * 关系笔数
     */
    @TableField("relation_count")
    private Integer relationCount;
    /**
     * 创建人主键
     */
    @TableField("create_customer_id")
    private String createCustomerId;


    public String getUpId() {
        return upId;
    }

    public void setUpId(String upId) {
        this.upId = upId;
    }

    public String getDownId() {
        return downId;
    }

    public void setDownId(String downId) {
        this.downId = downId;
    }

    public Integer getRelationCount() {
        return relationCount;
    }

    public void setRelationCount(Integer relationCount) {
        this.relationCount = relationCount;
    }

    public String getCreateCustomerId() {
        return createCustomerId;
    }

    public void setCreateCustomerId(String createCustomerId) {
        this.createCustomerId = createCustomerId;
    }

    @Override
    public String toString() {
        return "CustomerRelation{" +
        ", upId=" + upId +
        ", downId=" + downId +
        ", relationCount=" + relationCount +
        ", createCustomerId=" + createCustomerId +
        "}";
    }
}
