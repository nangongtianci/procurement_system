package com.msb.common.base.entity;

import com.baomidou.mybatisplus.annotations.TableField;

public class BaseAdminEntity extends BaseTimeEntity {
    @TableField("create_user")
    private String createUser;
    @TableField("update_user")
    private String updateUser;

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
