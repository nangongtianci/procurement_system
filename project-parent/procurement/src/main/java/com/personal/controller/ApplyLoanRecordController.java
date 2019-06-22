package com.personal.controller;


import com.msb.common.annotation.InsertMethodFlag;
import com.msb.common.enume.ModuleEnum;
import com.msb.common.enume.MortgageThingEnum;
import com.msb.common.utils.base.StringUtils;
import com.msb.common.utils.result.Result;
import com.personal.entity.ApplyLoanRecord;
import com.personal.service.ApplyLoanRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static com.msb.common.utils.result.CommonResultMsg.*;
import static com.msb.common.utils.result.RegUtils.*;

/**
 * <p>
 * 申请贷款记录 前端控制器
 * </p>
 *
 * @author ylw
 * @since 2018-05-13
 */
@RestController
@RequestMapping("/apply")
public class ApplyLoanRecordController {

    @Autowired
    private ApplyLoanRecordService applyLoanRecordService;

    /**
     * 申请贷款
     * @param applyLoanRecord
     * @return
     */
    @InsertMethodFlag
    @PostMapping
    public Result insert(ApplyLoanRecord applyLoanRecord){
        if(applyLoanRecord.getNeedMoney() == null ||
                applyLoanRecord.getNeedMoney().compareTo(new BigDecimal(0)) <= 0){
            return Result.FAIL(assignFieldNameForMoney("所需金额", new BigDecimal(0)));
        }

        if(applyLoanRecord.getUseCycle() <= 0){
            return Result.FAIL(assignFieldNameForInteger("使用周期",0));
        }

        if(applyLoanRecord.getAcceptRate() <= 0){
            return Result.FAIL(assignFieldNameForMoney("所需利率", new BigDecimal(0)));
        }

        if(MortgageThingEnum.getByValue(applyLoanRecord.getMortgageThing()) == null){
            return Result.FAIL(assignFieldIllegalValueRange("可抵押物品"));
        }

        if(StringUtils.isBlank(applyLoanRecord.getPurpose())){
            return Result.FAIL(assignFieldNotNull("资金用途"));
        }

        if(!matchesIds(applyLoanRecord.getCreateCustomerId())){ // 用户主键
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.customer));
        }

        if(applyLoanRecordService.insert(applyLoanRecord)){
            return Result.OK();
        }
        return Result.FAIL();
    }

    /**
     * 查询统计个数
     * @return
     */
    @GetMapping("/counts")
    public Result selectCounts(){
        return Result.OK(applyLoanRecordService.selectCount(null));
    }

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result selectById(@PathVariable String id){
        if(!matchesIds(id)){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.applyLoanRecord));
        }
        return Result.OK(applyLoanRecordService.selectById(id));
    }
}

