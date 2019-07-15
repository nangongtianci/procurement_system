package com.msb.service.impl;

import com.msb.entity.CustomerBillRelation;
import com.msb.entity.vo.BillInfoForUpAndDownStreamVO;
import com.msb.entity.vo.UpAndDownStreamListVO;
import com.msb.mapper.CustomerBillRelationMapper;
import com.msb.requestParam.BillQueryParam;
import com.msb.requestParam.ReceiveOrPaymentQueryParam;
import com.msb.service.CustomerBillRelationService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 客户账单关系表 服务实现类
 * </p>
 *
 * @author ylw
 * @since 2019-06-13
 */
@Service
public class CustomerBillRelationServiceImpl extends ServiceImpl<CustomerBillRelationMapper, CustomerBillRelation> implements CustomerBillRelationService {
    @Autowired
    private CustomerBillRelationMapper customerBillRelationMapper;

    @Override
    public UpAndDownStreamListVO getUpAndDownStream(ReceiveOrPaymentQueryParam param) {
        return customerBillRelationMapper.getUpAndDownStream(param);
    }

    @Override
    public List<BillInfoForUpAndDownStreamVO> getReceiveOrPaymentBillList(ReceiveOrPaymentQueryParam param) {
        return customerBillRelationMapper.getReceiveOrPaymentBillList(param);
    }

    @Override
    public int getReceiveOrPaymentBillCount(ReceiveOrPaymentQueryParam param) {
        return customerBillRelationMapper.getReceiveOrPaymentBillCount(param);
    }
}
