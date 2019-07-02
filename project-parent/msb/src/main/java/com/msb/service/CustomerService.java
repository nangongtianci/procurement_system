package com.msb.service;

import com.msb.entity.Customer;
import com.baomidou.mybatisplus.service.IService;
import com.msb.entity.vo.UpAndDownStreamListVO;

import java.util.List;

/**
 * <p>
 * 客户信息 服务类
 * </p>
 *
 * @author ylw
 * @since 2019-05-27
 */
public interface CustomerService extends IService<Customer> {

    /**
     * 查询上下游列表
     * @param id
     * @param isTop
     * @return
     */
    List<UpAndDownStreamListVO> getUpAndDownStreamList(String id, String isTop);
}
