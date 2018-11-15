package com.personal.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.personal.entity.Customer;
import com.personal.entity.CustomerBill;
import com.personal.mapper.CustomerBillMapper;
import com.personal.mapper.CustomerMapper;
import com.personal.service.CustomerBillService;
import com.personal.service.CustomerService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户账单关联 服务实现类
 * </p>
 *
 * @author ylw
 * @since 2018-05-13
 */
@Service
public class CustomerBillServiceImpl extends ServiceImpl<CustomerBillMapper, CustomerBill> implements CustomerBillService {

}
