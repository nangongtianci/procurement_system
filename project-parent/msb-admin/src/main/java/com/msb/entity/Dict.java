package com.msb.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.msb.common.base.entity.BaseAdminEntity;

/**
 * <p>
 * 系统字典表
 * </p>
 *
 * @author ylw
 * @since 2019-01-16
 */
@TableName("t_dict")
public class Dict extends BaseAdminEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 父级字典id
     */
    private String pid;
    /**
     * 名称
     */
    private String name;
    /**
     * 编码
     */
    private String code;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        return "Dict{" +
        ", pid=" + pid +
        ", name=" + name +
        ", code=" + code +
        ", desc=" + desc +
        ", sort=" + sort +
        "}";
    }
}
