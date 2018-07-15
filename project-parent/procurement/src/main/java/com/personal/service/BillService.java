package com.personal.service;

import com.personal.entity.Bill;
import com.baomidou.mybatisplus.service.IService;
import com.personal.conditions.BillQueryParam;

import java.util.List;

/**
 * <p>
 * 账单 服务类
 * </p>
 *
 * @author ylw
 * @since 2018-05-13
 */
public interface BillService extends IService<Bill> {
    /**
     * 新增账单并新增与之关联的商品
     * @param bill
     * @return
     */
    boolean insertCascadeGoods(Bill bill);

    /**
     * 更新账单并更新与之关联的商品
     * @param bill
     * @return
     */
    boolean updateCascadeGoods(Bill bill);

    /**
     * 根据主键查询，级联查询商品
     * @param id
     * @return
     */
    Bill selectByIdCascadeGoods(String id);

    /**
     * 分页查询个数
     * @param param
     * @return
     */
    int selectCountByCondition(BillQueryParam param);

    /**
     * 账单多条件查询
     * @param param
     * @return
     */
    List<Bill> selectPageByParam(BillQueryParam param);

    /**
     * 账单多条件查询（不级联商品）
     * @param param
     * @return
     */
    List<Bill> selectPageByParamNoCascadeGoods(BillQueryParam param);

    /**
     * 账单多条件查询(无分页信息)
     * @param param
     * @return
     */
    List<Bill> selectByParam(BillQueryParam param);

    /**
     * 账单多条件查询(无分页信息)-（不级联查询商品）
     * @param param
     * @return
     */
    List<Bill> selectByParamNoCascadeGoods(BillQueryParam param);

    /**
     * 删除账单，并更新对等账单
     * @param id
     * @return
     */
    boolean deleteByIdAndPeerUpdate(String id);
}
