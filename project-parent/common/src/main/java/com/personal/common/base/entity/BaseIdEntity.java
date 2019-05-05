package com.personal.common.base.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.personal.common.base.marking.POJOSerializable;
import com.personal.common.utils.validate.type.Update;

import javax.validation.constraints.NotBlank;

public class BaseIdEntity implements POJOSerializable {
    private static final long serialVersionUID = 6457456038764797130L;

    @NotBlank(message = "主键不能为空!",groups = {Update.class})
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
