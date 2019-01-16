package com.msb.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.personal.common.base.entity.BaseAdminEntity;

/**
 * <p>
 * 全局商品表
 * </p>
 *
 * @author ylw
 * @since 2019-01-16
 */
@TableName("t_global_goods")
public class GlobalGoods extends BaseAdminEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 商品名称
     */
    private String name;
    /**
     * 全局商品图片（暗含默认值的意思），逗号相连接
     */
    private String imgs;
    /**
     * 全局商品视频（暗含默认值的意思），逗号相连接
     */
    private String videos;
    /**
     * 商品单价
     */
    @TableField("unit_price")
    private BigDecimal unitPrice;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getVideos() {
        return videos;
    }

    public void setVideos(String videos) {
        this.videos = videos;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "GlobalGoods{" +
        ", name=" + name +
        ", imgs=" + imgs +
        ", videos=" + videos +
        ", unitPrice=" + unitPrice +
        "}";
    }
}
