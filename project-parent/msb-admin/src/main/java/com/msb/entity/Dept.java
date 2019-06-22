package com.msb.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.msb.common.base.entity.BaseAdminEntity;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author ylw
 * @since 2019-01-16
 */
@TableName("t_dept")
public class Dept extends BaseAdminEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 父部门主键
     */
    private String pid;
    /**
     * 简称
     */
    @TableField("simple_name")
    private String simpleName;
    /**
     * 全称
     */
    @TableField("full_name")
    private String fullName;
    /**
     * 描述
     */
    private String desc;
    /**
     * 排序
     */
    private Integer sort;


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "Dept{" +
        ", pid=" + pid +
        ", simpleName=" + simpleName +
        ", fullName=" + fullName +
        ", desc=" + desc +
        ", sort=" + sort +
        "}";
    }
}
