package com.personal.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.personal.common.annotation.InsertMethodFlag;
import com.personal.common.annotation.UpdateMethodFlag;
import com.personal.common.base.BaseController;
import com.personal.common.enume.ModuleEnum;
import com.personal.common.enume.StatusEnum;
import com.personal.common.enume.UserTypeEnum;
import com.personal.common.utils.base.StringUtils;
import com.personal.common.utils.result.Result;
import com.personal.config.redis.RedisService;
import com.personal.config.token.TokenUtils;
import com.personal.entity.CodeManage;
import com.personal.service.CodeManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.personal.common.utils.result.CommonResultMsg.*;
import static com.personal.common.utils.result.RegUtils.*;

/**
 * <p>
 * 代码管理表 前端控制器
 * </p>
 *
 * @author ylw
 * @since 2018-10-28
 */
@RestController
@RequestMapping("/code/manage")
public class CodeManageController extends BaseController{

    @Autowired
    private CodeManageService codeManageService;
    @Autowired
    private RedisService redisService;

    @InsertMethodFlag
    @PostMapping("/add")
    public Result add(HttpServletRequest request, CodeManage codeManage){
        String customerId = TokenUtils.getUid(UserTypeEnum.customer,request.getHeader("token"),redisService);
        codeManage.setCreateCustomerId(customerId);
        if(!matchesIds(codeManage.getCreateCustomerId())){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.customer));
        }

        if(!matchesChar(codeManage.getName(),12)){
            return Result.FAIL(assignFieldSection(codeManage.getName(),
                    ConditionEnum.countDefaultValue,12));
        }

        if(!matchesAmount(codeManage.getPrice(),new BigDecimal(0))){
            return Result.FAIL(assignFieldNameForMoney("单价",new BigDecimal(0)));
        }

        if(StringUtils.isNotBlank(codeManage.getProductSpec()) && !matchesChar(codeManage.getProductSpec(),8)){
            return Result.FAIL(assignFieldSection(codeManage.getProductSpec(),
                    ConditionEnum.countDefaultValue,8));
        }

        if(StringUtils.isNotBlank(codeManage.getProductPlace()) && !matchesChar(codeManage.getProductPlace(),12)){
            return Result.FAIL(assignFieldSection(codeManage.getProductPlace(),
                    ConditionEnum.countDefaultValue,12));
        }

        EntityWrapper<CodeManage> ew = new EntityWrapper<>();
        ew.setSqlSelect("max(code) as maxCode");
        int code = 1;
        Object obj = codeManageService.selectObj(ew);
        if(obj != null && obj instanceof Integer){
           code =  (int)obj + 1;
        }
        codeManage.setCode(code);
        codeManage.setStatus(StatusEnum.no.getValue());
        codeManage.setCreateTime(new Date());
        codeManage.setUpdateTime(new Date());
        return Result.OK(codeManageService.insert(codeManage));
    }


    @UpdateMethodFlag
    @PostMapping("/update")
    public Result update(CodeManage codeManage) {
        if(!matchesIds(codeManage.getId())){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.codeManage));
        }

        if(StringUtils.isNotBlank(codeManage.getName())
                && !matchesChar(codeManage.getName(),12)){
            return Result.FAIL(assignFieldSection(codeManage.getName(),
                    ConditionEnum.countDefaultValue,12));
        }

        if(StringUtils.isNotBlank(codeManage.getName())
                && !matchesAmount(codeManage.getPrice(),new BigDecimal(0))){
            return Result.FAIL(assignFieldNameForMoney("单价",new BigDecimal(0)));
        }

        if(StringUtils.isNotBlank(codeManage.getProductSpec()) && !matchesChar(codeManage.getProductSpec(),8)){
            return Result.FAIL(assignFieldSection(codeManage.getProductSpec(),
                    ConditionEnum.countDefaultValue,8));
        }

        if(StringUtils.isNotBlank(codeManage.getProductPlace()) && !matchesChar(codeManage.getProductPlace(),12)){
            return Result.FAIL(assignFieldSection(codeManage.getProductPlace(),
                    ConditionEnum.countDefaultValue,12));
        }
        return Result.OK(codeManageService.updateById(codeManage));
    }

    @GetMapping("list")
    public Result selectPageForIndex(HttpServletRequest request){
        String customerId = TokenUtils.getUid(UserTypeEnum.customer,request.getHeader("token"),redisService);
        EntityWrapper<CodeManage> ew = new EntityWrapper<>();
        ew.where("create_customer_id={0}",customerId);
        List<String> order = new ArrayList();
        order.add("create_time");
        ew.orderAsc(order);
        return Result.OK(codeManageService.selectList(ew));
    }
}

