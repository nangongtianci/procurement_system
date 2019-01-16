package com.personal.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.personal.common.base.entity.BaseWeChatEntity;

/**
 * <p>
 * 代码管理表
 * </p>
 *
 * @author ylw
 * @since 2018-10-28
 */
@TableName("t_code_manage")
public class CodeManage extends BaseWeChatEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 代码编号
     */
    private Integer code;
    /**
     * 产品名称
     */
    private String name;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 产地
     */
    @TableField("product_place")
    private String productPlace;
    /**
     * 产品规格
     */
    @TableField("product_spec")
    private String productSpec;
    /**
     * 产品图片
     */
    @TableField("product_img_path")
    private String productImgPath;
    /**
     * 创建人主键
     */
    @TableField("create_customer_id")
    private String createCustomerId;
    /**
     * 状态
     */
    private String status;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getProductPlace() {
        return productPlace;
    }

    public void setProductPlace(String productPlace) {
        this.productPlace = productPlace;
    }

    public String getProductSpec() {
        return productSpec;
    }

    public void setProductSpec(String productSpec) {
        this.productSpec = productSpec;
    }

    public String getProductImgPath() {
        return productImgPath;
    }

    public void setProductImgPath(String productImgPath) {
        this.productImgPath = productImgPath;
    }

    public String getCreateCustomerId() {
        return createCustomerId;
    }

    public void setCreateCustomerId(String createCustomerId) {
        this.createCustomerId = createCustomerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CodeManage{" +
        ", code=" + code +
        ", name=" + name +
        ", price=" + price +
        ", productPlace=" + productPlace +
        ", productSpec=" + productSpec +
        ", productImgPath=" + productImgPath +
        ", createCustomerId=" + createCustomerId +
        ", status=" + status +
        "}";
    }
}
