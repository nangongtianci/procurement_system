package com.msb.mapper;

import com.msb.entity.Customer;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.msb.entity.vo.UpAndDownStreamListVO;

import java.util.List;

/**
 * <p>
 * 客户信息 Mapper 接口
 * </p>
 *
 * @author ylw
 * @since 2019-05-27
 */
public interface CustomerMapper extends BaseMapper<Customer> {

    /**
     * 查询上下游列表
     * @param id
     * @param isTop
     * @return
     */
    List<UpAndDownStreamListVO> getUpAndDownStreamList(String id,String isTop);
}
