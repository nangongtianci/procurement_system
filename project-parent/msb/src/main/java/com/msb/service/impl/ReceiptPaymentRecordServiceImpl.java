package com.msb.service.impl;

import com.msb.entity.ReceiptPaymentRecord;
import com.msb.entity.vo.ReceiptPaymentRecordListVO;
import com.msb.mapper.ReceiptPaymentRecordMapper;
import com.msb.requestParam.ReceiveOrPaymentQueryParam;
import com.msb.service.ReceiptPaymentRecordService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * <p>
 * 收付款记录 服务实现类
 * </p>
 *
 * @author ylw
 * @since 2019-07-02
 */
@Service
public class ReceiptPaymentRecordServiceImpl extends ServiceImpl<ReceiptPaymentRecordMapper, ReceiptPaymentRecord> implements ReceiptPaymentRecordService {
    @Autowired
    private ReceiptPaymentRecordMapper receiptPaymentRecordMapper;

    @Override
    public List<ReceiptPaymentRecordListVO> getListByParam(ReceiveOrPaymentQueryParam param) {
        return receiptPaymentRecordMapper.getListByParam(param);
    }

    @Override
    public int getCountByParam(ReceiveOrPaymentQueryParam param) {
        return receiptPaymentRecordMapper.getCountByParam(param);
    }
}
