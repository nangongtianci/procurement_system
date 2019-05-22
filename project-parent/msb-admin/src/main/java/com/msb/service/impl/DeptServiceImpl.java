package com.msb.service.impl;

import com.msb.entity.Dept;
import com.msb.mapper.DeptMapper;
import com.msb.service.DeptService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author ylw
 * @since 2019-01-16
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

}
