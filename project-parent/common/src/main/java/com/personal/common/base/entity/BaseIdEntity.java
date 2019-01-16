package com.personal.common.base.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.personal.common.base.marking.POJOSerializable;

public class BaseIdEntity implements POJOSerializable {
    private static final long serialVersionUID = 6457456038764797130L;
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
