package com.msb.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.msb.common.annotation.PageQueryMethodFlag;
import com.msb.common.base.controller.BaseMsbController;
import com.msb.common.enume.ModuleEnum;
import com.msb.common.utils.base.StringUtils;
import com.msb.common.utils.constants.DictConstant;
import com.msb.common.utils.result.PaginationUtils;
import com.msb.common.utils.result.Result;
import com.msb.entity.Bill;
import com.msb.entity.BillGoods;
import com.msb.entity.Customer;
import com.msb.entity.Dict;
import com.msb.requestParam.BillQueryParam;
import com.msb.service.BillGoodsService;
import com.msb.service.BillService;
import com.msb.service.CustomerService;
import com.msb.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
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
    @Autowired
    private CustomerService customerService;

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

        Customer customer = customerService.selectById(billGoods.getCreateCustomerId());
        Map<String,Object> map = new HashMap<>();
        map.put("customer",customer);
        map.put("billGoods",billGoods);
        return render(map);
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

    /**
     * 商品分页查询
     * @param param
     * @return
     */
    @PageQueryMethodFlag
    @PostMapping("/page")
    public Result selectPageForIndex(HttpServletRequest request, @RequestBody BillQueryParam param){
        if(StringUtils.isBlank(param.getCreateCustomerId())){
            param.setCreateCustomerId(getCid(request.getHeader("token")));
        }

        if(StringUtils.isBlank(param.getIsGoods())){
            param.setIsGoods("1");
        }

        Page<BillGoods> condition = new Page<>(param.getPageNow(),param.getPageSize());
        Page<BillGoods> billGoodsPage = billGoodsService.selectPage(condition,
                new EntityWrapper<BillGoods>().where("create_customer_id={0} and is_goods={1}",param.getCreateCustomerId(),param.getIsGoods()));
        return PaginationUtils.getResultObj(billGoodsPage);
    }
}

