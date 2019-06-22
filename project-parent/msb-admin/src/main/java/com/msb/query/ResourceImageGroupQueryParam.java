package com.msb.query;

import com.msb.common.base.BaseQueryParam;

public class ResourceImageGroupQueryParam extends BaseQueryParam {
    /**
     * 图片名称
     */
    private String name;
    /**
     * 排序
     */
    private Integer sort;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
