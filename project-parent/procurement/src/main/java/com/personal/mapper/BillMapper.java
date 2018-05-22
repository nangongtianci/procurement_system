package com.personal.mapper;

import com.personal.conditions.BillQueryParam;
import com.personal.entity.Bill;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 账单 Mapper 接口
 * </p>
 *
 * @author ylw
 * @since 2018-05-13
 */
public interface BillMapper extends BaseMapper<Bill> {
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
}
