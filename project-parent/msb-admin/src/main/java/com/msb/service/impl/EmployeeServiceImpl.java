package com.msb.service.impl;

import com.msb.entity.Employee;
import com.msb.mapper.EmployeeMapper;
import com.msb.service.EmployeeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 员工表 服务实现类
 * </p>
 *
 * @author ylw
 * @since 2019-01-16
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
