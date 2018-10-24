package com.personal.service;

import com.baomidou.mybatisplus.service.IService;
import com.personal.common.base.page.PageQueryParam;
import com.personal.conditions.BillQueryParam;
import com.personal.entity.Bill;
import com.personal.entity.vo.BillGoodsForIndexPageVO;
import com.personal.entity.vo.BillStatisticsVO;

import java.util.List;
import java.util.Map;

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

    /**
     * 账单统计查询
     * @param param
     * @return
     */
    List<BillStatisticsVO> selectStatisticsForBill(Map<String,Object> param);


    /**------------------改造过的，单一页面使用接口-----------------------**/
    /**
     * 账单首页分页查询（级联商品名称，创建时间降序排序）
     * @param param
     * @return
     */
    List<BillGoodsForIndexPageVO> selectByParamForIndexPage(PageQueryParam param);
}
