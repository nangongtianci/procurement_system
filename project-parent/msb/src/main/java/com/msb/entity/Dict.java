package com.msb.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.msb.common.base.entity.BaseWeChatEntity;

/**
 * <p>
 * 系统字典表
 * </p>
 *
 * @author ylw
 * @since 2019-05-27
 */
@TableName("t_dict")
public class Dict extends BaseWeChatEntity {

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
    /**
     * 创建人
     */
    @TableField("create_user")
    private String createUser;
    /**
     * 更新人
     */
    @TableField("update_user")
    private String updateUser;


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

    @Override
    public String toString() {
        return "Dict{" +
        ", pid=" + pid +
        ", name=" + name +
        ", code=" + code +
        ", desc=" + desc +
        ", sort=" + sort +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        "}";
    }
}
