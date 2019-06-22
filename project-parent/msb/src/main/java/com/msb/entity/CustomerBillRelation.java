package com.msb.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.msb.common.base.entity.BaseWeChatEntity;

/**
 * <p>
 * 客户账单关系表
 * </p>
 *
 * @author ylw
 * @since 2019-06-13
 */
@TableName("t_customer_bill_relation")
public class CustomerBillRelation extends BaseWeChatEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 客户主键
     */
    private String cid;
    /**
     * 账单主键
     */
    private String bid;
    /**
     * 新建，扫描，卖货，代卖
     */
    @TableField("relation_type")
    private String relationType;
    /**
     * 来自哪个客户
     */
    private String pid;
    /**
     * 通过哪个账单生成而来
     */
    private String pbid;


    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPbid() {
        return pbid;
    }

    public void setPbid(String pbid) {
        this.pbid = pbid;
    }

    @Override
    public String toString() {
        return "CustomerBillRelation{" +
                "cid='" + cid + '\'' +
                ", bid='" + bid + '\'' +
                ", relationType='" + relationType + '\'' +
                ", pid='" + pid + '\'' +
                ", pbid='" + pbid + '\'' +
                '}';
    }
}
