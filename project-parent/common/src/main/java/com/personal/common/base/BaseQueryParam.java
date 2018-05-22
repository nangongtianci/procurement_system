package com.personal.common.base;

import java.io.Serializable;

/**
 * 多条件查询基类
 * @author ylw
 * @date 18-5-18 下午3:18
 * @param
 * @return
 */
public class BaseQueryParam implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
