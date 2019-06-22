package com.msb.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.msb.common.base.controller.BaseMsbController;
import com.msb.common.enume.ModuleEnum;
import com.msb.common.utils.base.StringUtils;
import com.msb.common.utils.constants.DictConstant;
import com.msb.common.utils.result.Result;
import com.msb.entity.Bill;
import com.msb.entity.BillGoods;
import com.msb.entity.Dict;
import com.msb.service.BillGoodsService;
import com.msb.service.BillService;
import com.msb.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;

import static com.msb.common.utils.result.CommonResultMsg.assignFieldIllegalValueRange;
import static com.msb.common.utils.result.CommonResultMsg.assignModuleNameForPK;
import static com.msb.common.utils.result.RegUtils.matchesIds;

/**
 * <p>
 * 订单商品信息 前端控制器
 * </p>
 *
 * @author ylw
 * @since 2019-06-05
 */
@RestController
@RequestMapping("/billGoods")
public class BillGoodsController extends BaseMsbController {
    @Autowired
    private BillGoodsService billGoodsService;
    @Autowired
    private BillService billService;
    @Autowired
    private DictService dictService;

    @PostMapping("/{billId}")
    public Result getById(@PathVariable String billId){
        Bill bill = billService.selectById(billId);
        if(!"0".equalsIgnoreCase(bill.getBusinessStatus())){
            return render(null,"非买入账单！");
        }

        BillGoods billGoods = billGoodsService.selectOne(new EntityWrapper<BillGoods>().where("bill_id={0}",billId));
        if(Objects.isNull(billGoods)){
            return render(null,"查询商品不存在！");
        }

        if("0".equalsIgnoreCase(billGoods.getIsGoods())){
            return render(null,"对不起，非商品！");
        }
        return render(billGoods);
    }

    @PostMapping("update")
    public Result update(@RequestBody BillGoods billGoods){
        if(StringUtils.isBlank(billGoods.getId())){
            return render(null,"商品主键");
        }

        if(StringUtils.isNotBlank(billGoods.getWeightUnit())){
            int ct = dictService.selectCount(new EntityWrapper<Dict>().
                    where("pid={0} and code={1}", DictConstant.WEIGHT_UNIT_CATEGORY_ID,billGoods.getWeightUnit()));
            if(ct<=0){
                return render(null,assignFieldIllegalValueRange("重量单位"));
            }
        }

        if(!Objects.isNull(billGoods.getGoodsStartCount()) && billGoods.getGoodsStartCount()<=0){
            return render(null,"起购量不能小于1！");
        }

        return render(billGoodsService.updateById(billGoods));
    }
}

