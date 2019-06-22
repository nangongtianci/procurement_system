package com.msb.query;

import com.msb.common.base.BaseQueryParam;

public class ResourceImageQueryParam extends BaseQueryParam {
    /**
     * 资源图片组主键
     */
    private String gid;
    /**
     * 图片名称
     */
    private String name;
    /**
     * 排序
     */
    private Integer sort;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

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
