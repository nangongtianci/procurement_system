package com.msb.service;

import com.baomidou.mybatisplus.service.IService;
import com.msb.entity.CustomerBillRelation;
import com.msb.entity.vo.BillInfoForUpAndDownStreamVO;
import com.msb.entity.vo.UpAndDownStreamListVO;
import com.msb.requestParam.ReceiveOrPaymentQueryParam;

import java.util.List;

/**
 * <p>
 * 客户账单关系表 服务类
 * </p>
 *
 * @author ylw
 * @since 2019-06-13
 */
public interface CustomerBillRelationService extends IService<CustomerBillRelation> {

    /**
     * 指定用户上下游
     * @param param
     * @return
     */
    UpAndDownStreamListVO getUpAndDownStream(ReceiveOrPaymentQueryParam param);

    /**
     * 查询应收或应付账单列表信息
     * @param param
     * @return
     */
    List<BillInfoForUpAndDownStreamVO> getReceiveOrPaymentBillList(ReceiveOrPaymentQueryParam param);

    /**
     * 查询应收或应付账单个数
     * @param param
     * @return
     */
    int getReceiveOrPaymentBillCount(ReceiveOrPaymentQueryParam param);
}
