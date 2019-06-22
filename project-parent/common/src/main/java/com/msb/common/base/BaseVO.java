package com.msb.common.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.msb.common.base.marking.POJOSerializable;

import java.util.Date;

/**
 * VO基础类
 * @author ylw
 * @date 18-10-16 下午2:16
 * @param
 * @return
 */
public class BaseVO implements POJOSerializable {
    private static final long serialVersionUID = 6457456038764797130L;
    private String id;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
