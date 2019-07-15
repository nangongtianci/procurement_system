package com.msb.service;

import com.msb.entity.ReceiptPaymentRecord;
import com.baomidou.mybatisplus.service.IService;
import com.msb.entity.vo.ReceiptPaymentRecordListVO;
import com.msb.requestParam.ReceiveOrPaymentQueryParam;

import java.util.List;

/**
 * <p>
 * 收付款记录 服务类
 * </p>
 *
 * @author ylw
 * @since 2019-07-02
 */
public interface ReceiptPaymentRecordService extends IService<ReceiptPaymentRecord> {

    /**
     * 查询收款|付款记录列表
     * @param param
     * @return
     */
    List<ReceiptPaymentRecordListVO> getListByParam(ReceiveOrPaymentQueryParam param);

    /**
     * 查询收款|付款记录个数
     * @param param
     * @return
     */
    int getCountByParam(ReceiveOrPaymentQueryParam param);
}
