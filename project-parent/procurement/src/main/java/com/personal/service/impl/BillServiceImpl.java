package com.personal.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.personal.common.enume.IsPeerBillEnum;
import com.personal.common.utils.collections.Collections3;
import com.personal.common.utils.collections.ListUtils;
import com.personal.conditions.BillQueryParam;
import com.personal.entity.Bill;
import com.personal.entity.Goods;
import com.personal.mapper.BillMapper;
import com.personal.service.BillService;
import com.personal.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 账单 服务实现类
 * </p>
 *
 * @author ylw
 * @since 2018-05-13
 */
@Service
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements BillService {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private BillMapper billMapper;

    @Transactional
    @Override
    public boolean insertCascadeGoods(Bill bill) {
        if(insert(bill) && goodsService.insertBatch(bill.getGoods())){
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean updateCascadeGoods(Bill bill) {
        List<Goods> exists = goodsService.selectList(new EntityWrapper<Goods>().where("bill_id={0}",bill.getId()));
        if(exists.size()>0){
            List<String> goodsIds = Collections3.extractToList(exists,"id");
            if(!goodsService.deleteBatchIds(goodsIds)){
                return false;
            }
        }

        if(!ListUtils.isEmpty(bill.getGoods()) && !goodsService.insertBatch(bill.getGoods())){
            return false;
        }

        if(!updateById(bill)){
            return false;
        }
        return true;
    }

    @Override
    public Bill selectByIdCascadeGoods(String id) {
        return billMapper.selectByIdCascadeGoods(id);
    }

    @Override
    public List<Bill> selectListByCustomerIdCascadeGoods(String customerId) {
        return billMapper.selectListByCustomerIdCascadeGoods(customerId);
    }

    @Override
    public List<Bill> selectListByMultiConditions(BillQueryParam billQueryParam) {
        return billMapper.selectListByMultiConditions(billQueryParam);
    }

    @Transactional
    @Override
    public boolean deleteByIdAndPeerUpdate(String id) {
        EntityWrapper<Bill> ew = new EntityWrapper<>();
        ew.setSqlSelect("bill_sn,is_peer_bill").where("id={0}",id);
        Bill bill = selectById(id);
        if(bill != null){
            if(IsPeerBillEnum.yes.getValue().equalsIgnoreCase(bill.getIsPeerBill())){ // 是对等账单
                if(deleteById(id)){
                    Bill update = new Bill();
                    update.setIsPeerBill(IsPeerBillEnum.no.getValue());
                    if(update(update,new EntityWrapper<Bill>().where("bill_sn={0}",bill.getBillSn()))){
                        return true;
                    }
                }
            }else{ // 非对等账单
                if(deleteById(id)){
                    return true;
                }
            }
        }else{
            return false;
        }
        return false;
    }
}
