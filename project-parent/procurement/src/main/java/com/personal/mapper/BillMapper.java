package com.personal.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.msb.common.base.page.PageQueryParam;
import com.personal.conditions.BillQueryParam;
import com.personal.entity.Bill;
import com.personal.entity.vo.BillGoodsForIndexPageVO;
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
     * 查询子账单列表，级联商品
     * @author ylw
     * @date 18-11-11 上午11:33
     * @param pid
     * @return java.util.List<com.personal.entity.Bill>
     */
    List<Bill> selectSubBillByPidCascadeGoods(String pid);

    /**
     * 分页查询个数
     * @param param
     * @return
     */
    int selectCountByCondition(BillQueryParam param);

    /**
     * 账单多条件查询(无分页信息)-（默认级联商品）
     * @param param
     * @return
     */
    List<Bill> selectByParam(BillQueryParam param);

    /**
     * 账单统计查询
     * @param param
     * @return
     */
    List<BillStatisticsVO> selectStatisticsForBill(Map<String,Object> param);



    /**------------------最新接口 2018-11-23-----------------------**/

    /**
     * 账单首页分页查询（级联商品名称，创建时间降序排序）
     * @param param
     * @return
     */
    List<BillGoodsForIndexPageVO> selectByParamForIndexPage(PageQueryParam param);
}
