package com.msb.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.msb.common.base.entity.BaseAdminEntity;

/**
 * <p>
 * 全局商品分类表
 * </p>
 *
 * @author ylw
 * @since 2019-01-16
 */
@TableName("t_global_goods_category")
public class GlobalGoodsCategory extends BaseAdminEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 父类id
     */
    private String pid;
    /**
     * 分类名称
     */
    private String name;
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
        return "GlobalGoodsCategory{" +
        ", pid=" + pid +
        ", name=" + name +
        ", desc=" + desc +
        ", sort=" + sort +
        "}";
    }
}
