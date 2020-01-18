package com.msb.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.msb.common.base.page.PageQueryParam;
import com.msb.entity.Bill;
import com.msb.entity.vo.BillProductsForIndexPageVO;
import com.msb.entity.vo.BillProductsForQueryPageVO;
import com.msb.entity.vo.BillStatisticsVO;
import com.msb.requestParam.BillQueryParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 账单 Mapper 接口
 * </p>
 *
 * @author ylw
 * @since 2019-06-05
 */
public interface BillMapper extends BaseMapper<Bill> {
    /**
     * 根据pid获取账单信息，并级联商品
     * @param pid
     * @return
     */
    List<Bill> getBillsByPidLinkGoods(String pid);

    /**
     * 分页查询个数
     * @param param
     * @return
     */
    int getCounts(BillQueryParam param);

    /**
     * 查询子账单统计信息
     * @param pid
     * @return
     */
    Map<String,Object> selectStat(String pid);

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
     * 账单多条件查询合计(查询界面使用)
     * @param param
     * @return
     */
    String getTotalForQueryPage(BillQueryParam param);

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
