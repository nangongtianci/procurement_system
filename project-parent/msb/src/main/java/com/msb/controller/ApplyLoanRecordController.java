package com.msb.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.msb.common.utils.constants.DictConstant;
import com.msb.entity.ApplyLoanRecord;
import com.msb.entity.Dict;
import com.msb.service.ApplyLoanRecordService;
import com.msb.common.annotation.InsertMethodFlag;
import com.msb.common.base.controller.BaseMsbController;
import com.msb.common.enume.ModuleEnum;
import com.msb.common.utils.base.StringUtils;
import com.msb.common.utils.result.Result;
import com.msb.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

import static com.msb.common.utils.result.CommonResultMsg.*;
import static com.msb.common.utils.result.RegUtils.*;

/**
 * <p>
 * 申请贷款记录 前端控制器
 * </p>
 *
 * @author ylw
 * @since 2019-05-28
 */
@RestController
@RequestMapping("/apply")
public class ApplyLoanRecordController extends BaseMsbController {

    @Autowired
    private ApplyLoanRecordService applyLoanRecordService;
    @Autowired
    private DictService dictService;

    /**
     * 申请贷款
     * @param applyLoanRecord
     * @return
     */
    @InsertMethodFlag
    @PostMapping
    public Result insert(HttpServletRequest request,@RequestBody ApplyLoanRecord applyLoanRecord){
        // String cid = getCid(request.getHeader("token"));
        String cid = "acdf18c5a364c138f09760221e7d4b7";
        applyLoanRecord.setCreateCustomerId(cid);

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

        if(StringUtils.isBlank(applyLoanRecord.getPurpose())){
            return Result.FAIL(assignFieldNotNull("资金用途"));
        }

        if(StringUtils.isBlank(applyLoanRecord.getMortgageThing())){
            return render(null,"请选择可抵押物!");
        }

        int bt = dictService.selectCount(new EntityWrapper<Dict>().
                where("pid={0} and code={1}", DictConstant.MORTGAGE_THING_CATEGORY_ID,applyLoanRecord.getMortgageThing()));
        if(bt<=0){
            return render(null,assignFieldIllegalValueRange("可抵押物"));
        }

        if(applyLoanRecordService.insert(applyLoanRecord)){
            return render(applyLoanRecord.getId());
        }
        return render(false);
    }

    /**
     * 查询统计个数
     * @return
     */
    @PostMapping("/counts")
    public Result selectCounts(){
        return render(applyLoanRecordService.selectCount(null));
    }

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    @PostMapping("/{id}")
    public Result selectById(@PathVariable String id){
        if(!matchesIds(id)){
            return render(assignModuleNameForPK(ModuleEnum.applyLoanRecord));
        }
        return render(applyLoanRecordService.selectById(id));
    }
}

