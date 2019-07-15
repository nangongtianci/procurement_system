package com.msb.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.msb.common.base.controller.BaseMsbAdminController;
import com.msb.common.base.validate.ValidateUtils;
import com.msb.common.enume.ModuleEnum;
import com.msb.common.enume.UserTypeEnum;
import com.msb.common.utils.base.StringUtils;
import com.msb.common.utils.base.UUIDUtils;
import com.msb.common.utils.constants.DictConstant;
import com.msb.common.utils.encode.MD5Util;
import com.msb.common.utils.result.PaginationUtils;
import com.msb.common.utils.result.Result;
import com.msb.common.utils.token.TokenUtils;
import com.msb.entity.Dict;
import com.msb.entity.Employee;
import com.msb.query.EmployeeQueryParam;
import com.msb.service.DictService;
import com.msb.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.msb.common.utils.result.CommonResultMsg.*;
import static com.msb.common.utils.result.RegUtils.matchesIds;

/**
 * <p>
 * 员工表 前端控制器
 * </p>
 *
 * @author ylw
 * @since 2019-01-16
 */
@Api(value = "员工controller",description = "员工控制器")
@RestController
@RequestMapping("/employee")
public class EmployeeController extends BaseMsbAdminController{

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private DictService dictService;

    @PostMapping("add")
    public Result add(@RequestBody Employee employee){
        if(StringUtils.isBlank(employee.getAccount()) || StringUtils.isBlank(employee.getPassword())){
            return render("用户名，密码不能为空！");
        }

        if(StringUtils.isBlank(employee.getName())){
            return render("用户名不能为空!");
        }

        employee.setSalt(UUIDUtils.getUUID());
        employee.setCreateUser("6ed7a9860f3d42eeb585a36fff8f9c49");
        employee.setUpdateUser("6ed7a9860f3d42eeb585a36fff8f9c49");
        return render(employeeService.insert(employee));
    }

    @PostMapping("delete/{id}")
    public Result delete(@PathVariable String id){
        if(!matchesIds(id)){
            return render(null,assignModuleNameForPK(ModuleEnum.employee));
        }
        return render(employeeService.deleteById(id));
    }

    @ApiOperation(value = "更新员工",notes = "账号无法修改")
    @PostMapping("update")
    public Result update(@RequestBody Employee employee){
        if(!matchesIds(employee.getId())){
            return render(null,assignModuleNameForPK(ModuleEnum.employee));
        }

        if(StringUtils.isNotBlank(employee.getAccount())){
            return render(null,"账号无法修改!");
        }

        if(StringUtils.isNotBlank(employee.getEmail()) && !ValidateUtils.isEmail(employee.getEmail())){
            return render(null,"邮箱格式不正确！");
        }

        if(StringUtils.isNotBlank(employee.getPhone()) && !ValidateUtils.isMobile(employee.getPhone())){
            return render(null,"手机号格式不正确！");
        }

        if(StringUtils.isNotBlank(employee.getStatus())){
            int ct = dictService.selectCount(new EntityWrapper<Dict>().
                    where("pid={0} and code={1}", DictConstant.YES_NO_CATEGORY_ID,employee.getStatus()));
            if(ct<=0){
                return render(null,assignFieldIllegalValueRange("用户状态"));
            }
        }
        employee.setUpdateUser("6ed7a9860f3d42eeb585a36fff8f9c49");
        return render(employeeService.updateById(employee));
    }

    @ApiOperation(value = "根据主键查询详情",notes = "根据主键查询详情")
    @PostMapping("/{id}")
    public Result getById(@PathVariable String id){
        if(!matchesIds(id)){
            return render(null,assignModuleNameForPK(ModuleEnum.employee));
        }
        return render(employeeService.selectById(id));
    }

    @ApiOperation(value = "员工分页查询",notes = "启用状态可以不传，不传则查询全部！")
    @PostMapping("page")
    public Result getPage(@RequestBody EmployeeQueryParam param){
        Page<Employee> page = new Page<>();
        if(StringUtils.isNotBlank(param.getStatus())){
            int ct = dictService.selectCount(new EntityWrapper<Dict>().
                    where("pid={0} and code={1}", DictConstant.YES_NO_CATEGORY_ID,param.getStatus()));
            if(ct<=0){
                return render(null,assignFieldIllegalValueRange("用户状态"));
            }

            EntityWrapper<Employee> ew = new EntityWrapper<>();
            ew.where("status={0}",param.getStatus());
            employeeService.selectPage(page,ew);
        }else{
            employeeService.selectPage(page);
        }
        return render(PaginationUtils.getResultObj(page));
    }

    /**
     * 登录
     * @param param
     * @return
     */
    @ApiOperation(value = "登录")
    @PostMapping(value = "/login")
    public Result login(@RequestBody @ApiParam(name="登录参数",value="传入json格式",required=true) String param){
        Object account = getParamByKey(param,"account");
        Object password = getParamByKey(param,"password");
        if(Objects.isNull(account) || StringUtils.isBlank(account.toString()) ||
           Objects.isNull(password) || StringUtils.isBlank(password.toString())){
            return render(null,assignFieldNotNull("账号密码不正确！"));
        }

        Employee employee = employeeService.
                selectOne(new EntityWrapper<Employee>().where("account={0}",account));
        if(Objects.isNull(employee)){
            return render(null,"账号不存在！");
        }

        if("0".equalsIgnoreCase(employee.getStatus())){
            return render(null,"该用户已被禁用!");
        }

        String pwd = MD5Util.getStringMD5(password+employee.getSalt());
        if(!employee.getPassword().equalsIgnoreCase(pwd)){
            return render(null,"用户名密码不正确!");
        }

        Map<String,Object> map = new HashMap<>();
        map.put("token", TokenUtils.setToken(UserTypeEnum.employee,employee.getId(),redisService));
        map.put("employee",employee);
        return render(map);
    }
}

