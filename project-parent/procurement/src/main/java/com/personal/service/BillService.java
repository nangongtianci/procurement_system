package com.personal.service;

import com.personal.conditions.BillQueryParam;
import com.personal.entity.Bill;
import com.baomidou.mybatisplus.service.IService;

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
     * 根据主键查询，级联查询商品
     * @param id
     * @return
     */
    Bill selectByIdCascadeGoods(String id);

    /**
     * 根据客户主键查询账单列表（默认今天）
     * @param customerId
     * @return
     */
    List<Bill> selectListByCustomerIdCascadeGoods(String customerId);

    /**
     * 账单多条件查询
     * @param billQueryParam
     * @return
     */
    List<Bill> selectListByMultiConditions(BillQueryParam billQueryParam);

    /**
     * 删除账单，并更新对等账单
     * @param id
     * @return
     */
    boolean deleteByIdAndPeerUpdate(String id);
}
