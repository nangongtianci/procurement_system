package com.personal.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.personal.common.enume.IsReceiveEnum;
import com.personal.common.enume.ModuleEnum;
import com.personal.common.utils.base.UUIDUtils;
import com.personal.common.utils.result.Result;
import com.personal.entity.Bill;
import com.personal.entity.CustomerVirtualCoin;
import com.personal.service.BillService;
import com.personal.service.CustomerVirtualCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static com.personal.common.utils.result.CommonResultMsg.assignModuleNameForPK;
import static com.personal.common.utils.result.RegUtils.matchesIds;

/**
 * <p>
 * 用户与虚拟币关系 前端控制器
 * </p>
 *
 * @author ylw
 * @since 2018-05-22
 */
@RestController
@RequestMapping("/customer/virtualCoin")
public class CustomerVirtualCoinController {

    @Autowired
    private CustomerVirtualCoinService customerVirtualCoinService;
    @Autowired
    private BillService billService;

    /**
     * 领取虚拟货币
     * @param customerId
     * @return
     */
    @GetMapping("/{billId}/{customerId}")
    public Result addOrUpdate(@PathVariable String billId,@PathVariable String customerId){
        if(!matchesIds(customerId)){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.customer));
        }

        if(!matchesIds(billId)){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.bill));
        }

        EntityWrapper<Bill> ew = new EntityWrapper<>();
        ew.setSqlSelect("is_receive");
        ew.where("id={0}",billId);
        if(!IsReceiveEnum.yes.getValue().equalsIgnoreCase((String)billService.selectObj(ew))){
            return Result.FAIL("已领取过了，无法在次领取！");
        }

        Date date = new Date();
        CustomerVirtualCoin result = customerVirtualCoinService
                .selectOne(new EntityWrapper<CustomerVirtualCoin>().where("customer_id={0}",customerId));
        Bill update = new Bill();
        update.setId(billId);
        update.setIsReceive(IsReceiveEnum.no.getValue());
        if(result != null){ // 更新
            result.setVirtualCoinCount(result.getVirtualCoinCount()+1);
            if(customerVirtualCoinService.updateById(result)){
                // 更新账单状态
                if(billService.updateById(update)){
                    return Result.OK();
                }
            }
        }else{ // 新增
            CustomerVirtualCoin customerVirtualCoin = new CustomerVirtualCoin();
            customerVirtualCoin.setId(UUIDUtils.getUUID());
            customerVirtualCoin.setCustomerId(customerId);
            customerVirtualCoin.setVirtualCoinCount(1);
            customerVirtualCoin.setCreateTime(date);
            customerVirtualCoin.setUpdateTime(date);
            if(customerVirtualCoinService.insert(customerVirtualCoin)){
                // 更新账单状态
                if(billService.updateById(update)){
                    return Result.OK();
                }
            }
        }
        return Result.FAIL();
    }

    /**
     * 查询用户已经拥有虚拟币个数
     * @return
     */
    @GetMapping("/count/{customerId}")
    public Result selectVirtualCoinCountByCustomerId(@PathVariable String customerId){
        if(!matchesIds(customerId)){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.customer));
        }
        return Result.OK().setData(customerVirtualCoinService
                .selectOne(new EntityWrapper<CustomerVirtualCoin>().where("customer_id={0}",customerId)).getVirtualCoinCount());
    }
}

