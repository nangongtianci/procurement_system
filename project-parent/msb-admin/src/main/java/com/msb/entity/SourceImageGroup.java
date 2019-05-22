package com.msb.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.personal.common.base.entity.BaseAdminEntity;
import com.personal.common.utils.validate.type.Insert;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 系统资源图片组表
 * </p>
 *
 * @author ylw
 * @since 2019-04-25
 */
@TableName("t_source_image_group")
public class SourceImageGroup extends BaseAdminEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 组名称
     */
    @NotBlank(message = "组名称不能为空!",groups = {Insert.class})
    private String name;
    /**
     * 组描述
     */
    private String desc;
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
        return "SourceImageGroup{" +
        ", name=" + name +
        ", desc=" + desc +
        ", sort=" + sort +
        "}";
    }
}
