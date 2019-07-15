package com.msb.mapper;

import com.msb.entity.ReceiptPaymentRecord;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.msb.entity.vo.ReceiptPaymentRecordListVO;
import com.msb.requestParam.ReceiveOrPaymentQueryParam;

import java.util.List;

/**
 * <p>
 * 收付款记录 Mapper 接口
 * </p>
 *
 * @author ylw
 * @since 2019-07-02
 */
public interface ReceiptPaymentRecordMapper extends BaseMapper<ReceiptPaymentRecord> {

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
