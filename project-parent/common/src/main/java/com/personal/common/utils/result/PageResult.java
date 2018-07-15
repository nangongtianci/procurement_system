package com.personal.common.utils.result;

import com.personal.common.base.marking.POJOSerializable;
/**
 * 分页对象
 * @author ylw
 * @date 18-7-15 上午10:57
 * @param
 * @return
 */
public class PageResult implements POJOSerializable {
    private static final long serialVersionUID = -8357050753655373228L;

    // 总条目
    private Integer total;

    // 当前页码
    private Integer pageNow;

    // 每页显示条数
    private Integer pageSize;

    // rows
    private Object rows;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPageNow() {
        return pageNow;
    }

    public void setPageNow(Integer pageNow) {
        this.pageNow = pageNow;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Object getRows() {
        return rows;
    }

    public void setRows(Object rows) {
        this.rows = rows;
    }
}
