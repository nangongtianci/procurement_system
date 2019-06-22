package com.msb.controller;


import com.msb.entity.Employee;
import com.msb.service.EmployeeService;
import com.msb.common.base.controller.BaseMsbAdminController;
import com.msb.common.utils.base.StringUtils;
import com.msb.common.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 员工表 前端控制器
 * </p>
 *
 * @author ylw
 * @since 2019-01-16
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController extends BaseMsbAdminController{

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("add")
    public Result add(@RequestBody Employee employee){
        if(StringUtils.isBlank(employee.getAccount()) || StringUtils.isBlank(employee.getPassword())){
            return render("用户名，密码不能为空！");
        }

        if(StringUtils.isBlank(employee.getName())){
            return render("用户名不能为空!");
        }

        return render(employeeService.insert(employee));
    }
}

