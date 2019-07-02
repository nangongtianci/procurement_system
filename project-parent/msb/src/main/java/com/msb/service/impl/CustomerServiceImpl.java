package com.msb.service.impl;

import com.msb.entity.Customer;
import com.msb.entity.vo.UpAndDownStreamListVO;
import com.msb.mapper.CustomerMapper;
import com.msb.service.CustomerService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public List<UpAndDownStreamListVO> getUpAndDownStreamList(String id, String isTop) {
        return customerMapper.getUpAndDownStreamList(id,isTop);
    }
}
