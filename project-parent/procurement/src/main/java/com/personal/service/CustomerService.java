package com.personal.service;

import com.baomidou.mybatisplus.service.IService;
import com.personal.entity.Customer;

/**
 * <p>
 * 客户信息 服务类
 * </p>
 *
 * @author ylw
 * @since 2018-05-13
 */
public interface CustomerService extends IService<Customer> {

    /**
     * 注册用户并初始化默认账单(注册时候调用)
     * @param customer
     * @return
     */
    void insertCustomerAndInitDefaultBill(Customer customer);
}
