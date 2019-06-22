package com.msb.service.impl;

import com.msb.entity.Customer;
import com.msb.mapper.CustomerMapper;
import com.msb.service.CustomerService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户信息 服务实现类
 * </p>
 *
 * @author ylw
 * @since 2019-05-27
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

}
