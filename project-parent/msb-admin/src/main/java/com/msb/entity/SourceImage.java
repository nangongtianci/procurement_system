package com.msb.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.msb.common.base.entity.BaseAdminEntity;
import com.msb.common.utils.validate.type.Insert;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 系统资源图片表
 * </p>
 *
 * @author ylw
 * @since 2019-04-25
 */
@TableName("t_source_image")
public class SourceImage extends BaseAdminEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 资源图片组主键
     */
    @NotBlank(message = "请选择分组!",groups = {Insert.class})
    private String gid;
    /**
     * 图片名称
     */
    @NotBlank(message = "图片名称不能为空!",groups = {Insert.class})
    private String name;
    /**
     * 图片描述
     */
    private String desc;
    /**
     * 文件
     */
//    @NotNull(message = "请选择文件!",groups = {Insert.class})
//    @TableField(exist = false)
//    private MultipartFile file;
    /**
     * 图片路径
     */
    @URL(message="无效的URL地址")
    @NotBlank(message = "URL不能为空！")
    private String url;
    /**
     * 排序
     */
    private Integer sort;

//    public MultipartFile getFile() {
//        return file;
//    }
//
//    public void setFile(MultipartFile file) {
//        this.file = file;
//    }

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "SourceImage{" +
        ", gid=" + gid +
        ", name=" + name +
        ", desc=" + desc +
        ", url=" + url +
        ", sort=" + sort +
        "}";
    }
}
