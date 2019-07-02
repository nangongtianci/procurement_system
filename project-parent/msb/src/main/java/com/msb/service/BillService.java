package com.msb.service;

import com.baomidou.mybatisplus.service.IService;
import com.msb.common.base.page.PageQueryParam;
import com.msb.common.utils.result.Result;
import com.msb.entity.Bill;
import com.msb.entity.vo.BillProductsForIndexPageVO;
import com.msb.entity.vo.BillProductsForQueryPageVO;
import com.msb.entity.vo.BillStatisticsVO;
import com.msb.requestParam.BillQueryParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 账单 服务类
 * </p>
 *
 * @author ylw
 * @since 2019-06-05
 */
public interface BillService extends IService<Bill> {
    /**
     * 新增账单
     * @param bill
     * @return
     */
    Result add(Bill bill);

    /**
     * 扫描账单并新增与之关联的商品
     * @param bill
     * @return
     */
    Result scan(Bill bill);

    /**
     * 更新账单
     * @param bill
     * @return
     */
    Result update(Bill bill);

    /**
     * 根据pid获取账单信息，并级联商品
     * @param pid
     * @return
     */
    List<Bill> getBillsByPidLinkGoods(String pid);

    /**
     * 分享账单
     * @param cid
     * @param bill
     * @return
     */
    Result shareBill(String cid,Bill bill);


    /********************** 查询 ************************/
    /**
     * 分页查询个数
     * @param param
     * @return
     */
    int getCounts(BillQueryParam param);

    /**
     * 账单多条件查询无分页（级联商品名称，创建时间降序排序,更新时间降序）
     * @param param
     * @return
     */
    List<BillProductsForQueryPageVO> getBillsByParams(BillQueryParam param);

    /**
     * 账单多条件查询含分页(查询界面使用)（级联商品名称，创建时间降序排序,更新时间降序）
     * @param param
     * @return
     */
    List<BillProductsForQueryPageVO> getPageForQueryPage(BillQueryParam param);

    /**
     * 账单首页分页查询（级联商品名称，创建时间降序排序）
     * @param param
     * @return
     */
    List<BillProductsForIndexPageVO> getPageForBillIndexPage(PageQueryParam param);

    /**
     * 账单统计查询
     * @param param
     * @return
     */
    List<BillStatisticsVO> getStatisticsForBill(Map<String,Object> param);
}
