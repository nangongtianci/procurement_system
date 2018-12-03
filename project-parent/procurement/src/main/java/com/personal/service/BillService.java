package com.personal.service;

import com.baomidou.mybatisplus.service.IService;
import com.personal.common.base.page.PageQueryParam;
import com.personal.common.utils.result.Result;
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
     * 扫描生成账单
     * @param bill
     * @return
     */
    boolean insertScan(Bill bill,String originId);

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
     * 账单多条件查询(无分页信息)
     * @param param
     * @return
     */
    List<Bill> selectByParam(BillQueryParam param);

    /**
     * 删除账单，并更新对等账单
     * @param id
     * @return
     */
    Result deleteByIdAndPeerUpdate(String cid, String id);

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

    /**
     * 分享账单
     * @param customerId
     * @param bill
     * @return
     */
    boolean shareBill(String customerId, Bill bill);
}
