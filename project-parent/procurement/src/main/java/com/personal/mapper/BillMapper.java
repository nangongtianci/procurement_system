package com.personal.mapper;

import com.personal.entity.Bill;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.personal.conditions.BillQueryParam;
import com.personal.entity.vo.BillStatisticsVO;

import java.util.List;
import java.util.Map;

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
     * 分页查询个数
     * @param param
     * @return
     */
    int selectCountByCondition(BillQueryParam param);

    /**
     * 账单多条件查询（默认级联商品）
     * @param param
     * @return
     */
    List<Bill> selectPageByParam(BillQueryParam param);

    /**
     * 账单多条件查询（不级联商品）
     * @param pageParam
     * @return
     */
    List<Bill> selectPageByParamNoCascadeGoods(BillQueryParam pageParam);

    /**
     * 账单多条件查询(无分页信息)-（默认级联商品）
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
     * 账单统计查询
     * @param param
     * @return
     */
    List<BillStatisticsVO> selectStatisticsForBill(Map<String,String> param);
}
