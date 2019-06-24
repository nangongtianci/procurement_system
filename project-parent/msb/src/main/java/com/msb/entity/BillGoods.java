package com.msb.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.msb.common.base.entity.BaseWeChatEntity;

/**
 * <p>
 * 订单商品信息
 * </p>
 *
 * @author ylw
 * @since 2019-06-05
 */
@TableName("t_bill_goods")
public class BillGoods extends BaseWeChatEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 账单主键
     */
    @TableField("bill_id")
    private String billId;
    /**
     * 品名
     */
    private String name;
    /**
     * 货品数量
     */
    private Float number;
    /**
     * 货品单价
     */
    private BigDecimal price;
    /**
     * 重量
     */
    private Integer weight = 0;
    /**
     * 商品单位（元/斤，元/箱）
     */
    @TableField("weight_unit")
    private String weightUnit;
    /**
     * 货品品质
     */
    private String quality;
    /**
     * 货品图片路径
     */
    @TableField("product_img_path")
    private String productImgPath;
    /**
     * 货品，商品
     */
    @TableField("is_goods")
    private String isGoods;
    /**
     * 下架，上架
     */
    @TableField("goods_is_show")
    private String goodsIsShow;
    /**
     * 商品定价
     */
    @TableField("goods_fix_price")
    private BigDecimal goodsFixPrice;
    /**
     * 商品启购量
     */
    @TableField("goods_start_count")
    private Integer goodsStartCount = 1;
    /**
     * 商品描述
     */
    @TableField("goods_desc")
    private String goodsDesc;
    /**
     * 商品品牌
     */
    @TableField("goods_brand")
    private String goodsBrand;
    /**
     * 商品规格
     */
    @TableField("goods_spec")
    private String goodsSpec;
    /**
     * 商品产地
     */
    @TableField("goods_product_place")
    private String goodsProductPlace;
    /**
     * 商品货址
     */
    @TableField("goods_address")
    private String goodsAddress;
    /**
     * 商品运费
     */
    @TableField("goods_freight")
    private BigDecimal goodsFreight;
    /**
     * 商品加费比例
     */
    @TableField("goods_cost_ratio")
    private Float goodsCostRatio;
    /**
     * 商品图片路径
     */
    @TableField("goods_img_path")
    private String goodsImgPath;
    /**
     * 商品视频路径
     */
    @TableField("goods_video_path")
    private String goodsVideoPath;
    /**
     * 创建人主键
     */
    @TableField("create_customer_id")
    private String createCustomerId;


    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getNumber() {
        return number;
    }

    public void setNumber(Float number) {
        this.number = number;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getProductImgPath() {
        return productImgPath;
    }

    public void setProductImgPath(String productImgPath) {
        this.productImgPath = productImgPath;
    }

    public String getIsGoods() {
        return isGoods;
    }

    public void setIsGoods(String isGoods) {
        this.isGoods = isGoods;
    }

    public String getGoodsIsShow() {
        return goodsIsShow;
    }

    public void setGoodsIsShow(String goodsIsShow) {
        this.goodsIsShow = goodsIsShow;
    }

    public BigDecimal getGoodsFixPrice() {
        return goodsFixPrice;
    }

    public void setGoodsFixPrice(BigDecimal goodsFixPrice) {
        this.goodsFixPrice = goodsFixPrice;
    }

    public Integer getGoodsStartCount() {
        return goodsStartCount;
    }

    public void setGoodsStartCount(Integer goodsStartCount) {
        this.goodsStartCount = goodsStartCount;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public String getGoodsBrand() {
        return goodsBrand;
    }

    public void setGoodsBrand(String goodsBrand) {
        this.goodsBrand = goodsBrand;
    }

    public String getGoodsSpec() {
        return goodsSpec;
    }

    public void setGoodsSpec(String goodsSpec) {
        this.goodsSpec = goodsSpec;
    }

    public String getGoodsProductPlace() {
        return goodsProductPlace;
    }

    public void setGoodsProductPlace(String goodsProductPlace) {
        this.goodsProductPlace = goodsProductPlace;
    }

    public String getGoodsAddress() {
        return goodsAddress;
    }

    public void setGoodsAddress(String goodsAddress) {
        this.goodsAddress = goodsAddress;
    }

    public BigDecimal getGoodsFreight() {
        return goodsFreight;
    }

    public void setGoodsFreight(BigDecimal goodsFreight) {
        this.goodsFreight = goodsFreight;
    }

    public Float getGoodsCostRatio() {
        return goodsCostRatio;
    }

    public void setGoodsCostRatio(Float goodsCostRatio) {
        this.goodsCostRatio = goodsCostRatio;
    }

    public String getGoodsImgPath() {
        return goodsImgPath;
    }

    public void setGoodsImgPath(String goodsImgPath) {
        this.goodsImgPath = goodsImgPath;
    }

    public String getGoodsVideoPath() {
        return goodsVideoPath;
    }

    public void setGoodsVideoPath(String goodsVideoPath) {
        this.goodsVideoPath = goodsVideoPath;
    }

    public String getCreateCustomerId() {
        return createCustomerId;
    }

    public void setCreateCustomerId(String createCustomerId) {
        this.createCustomerId = createCustomerId;
    }

    @Override
    public String toString() {
        return "BillGoods{" +
        ", billId=" + billId +
        ", name=" + name +
        ", number=" + number +
        ", price=" + price +
        ", weight=" + weight +
        ", weightUnit=" + weightUnit +
        ", quality=" + quality +
        ", productImgPath=" + productImgPath +
        ", isGoods=" + isGoods +
        ", goodsIsShow=" + goodsIsShow +
        ", goodsFixPrice=" + goodsFixPrice +
        ", goodsStartCount=" + goodsStartCount +
        ", goodsDesc=" + goodsDesc +
        ", goodsBrand=" + goodsBrand +
        ", goodsSpec=" + goodsSpec +
        ", goodsProductPlace=" + goodsProductPlace +
        ", goodsAddress=" + goodsAddress +
        ", goodsFreight=" + goodsFreight +
        ", goodsCostRatio=" + goodsCostRatio +
        ", goodsImgPath=" + goodsImgPath +
        ", goodsVideoPath=" + goodsVideoPath +
        ", createCustomerId=" + createCustomerId +
        "}";
    }
}
