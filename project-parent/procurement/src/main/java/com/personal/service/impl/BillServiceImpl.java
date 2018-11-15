package com.personal.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.personal.common.base.page.PageQueryParam;
import com.personal.common.enume.IsPeerBillEnum;
import com.personal.common.enume.MasterShareEnum;
import com.personal.common.utils.base.UUIDUtils;
import com.personal.common.utils.collections.Collections3;
import com.personal.common.utils.collections.ListUtils;
import com.personal.conditions.BillQueryParam;
import com.personal.entity.Bill;
import com.personal.entity.CustomerBill;
import com.personal.entity.Goods;
import com.personal.entity.vo.BillGoodsForIndexPageVO;
import com.personal.entity.vo.BillStatisticsVO;
import com.personal.mapper.BillMapper;
import com.personal.mapper.CustomerBillMapper;
import com.personal.service.BillService;
import com.personal.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 账单 服务实现类
 * </p>
 *
 * @author ylw
 * @since 2018-05-13
 */
@Transactional
@Service
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements BillService {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private BillMapper billMapper;
    @Autowired
    private CustomerBillMapper customerBillMapper;

    @Transactional
    @Override
    public boolean insertCascadeGoods(Bill bill) {
        insert(bill);
        goodsService.insertBatch(bill.getGoods());
        CustomerBill customerBill =  new CustomerBill();
        customerBill.setId(UUIDUtils.getUUID());
        customerBill.setBid(bill.getId());
        customerBill.setCid(bill.getCreateCustomerId());
        customerBill.setMasterShare(MasterShareEnum.master.getValue());
        customerBill.setCreateTime(new Date());
        customerBill.setUpdateTime(new Date());
        customerBillMapper.insert(customerBill);
        return true;
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
    public List<Bill> selectSubBillByPidCascadeGoods(String pid) {
        return billMapper.selectSubBillByPidCascadeGoods(pid);
    }

    @Override
    public int selectCountByCondition(BillQueryParam param) {
        return billMapper.selectCountByCondition(param);
    }


    @Override
    public List<BillGoodsForIndexPageVO> selectByParamForIndexPage(PageQueryParam param) {
        return billMapper.selectByParamForIndexPage(param);
    }

    @Override
    public boolean shareBill(String customerId, Bill bill) {
        CustomerBill cb = new CustomerBill();
        cb.setId(UUIDUtils.getUUID());
        cb.setCid(customerId);
        cb.setBid(bill.getId());
        cb.setMasterShare(MasterShareEnum.share.getValue());
        cb.setCreateTime(new Date());
        cb.setUpdateTime(new Date());
        customerBillMapper.insert(cb);
        return true;
    }

    @Override
    public List<Bill> selectByParam(BillQueryParam param) {
        return billMapper.selectByParam(param);
    }

    @Transactional
    @Override
    public boolean deleteByIdAndPeerUpdate(String cid,String id) {
        EntityWrapper<Bill> ew = new EntityWrapper<>();
        ew.setSqlSelect("bill_sn,is_peer_bill").where("id={0}",id);
        Bill bill = selectById(id);
        if(bill != null){
            // 首先判断是否为父账单
            EntityWrapper<Bill> isParent = new EntityWrapper<>();
            isParent.where("pid={0}",id);
            int rt = billMapper.selectCount(isParent);
            if(rt>0){
                return false;
            }

            // 主账单和共享账单区别（删除关联关系）
            EntityWrapper<CustomerBill> cb = new EntityWrapper<>();
            cb.where("bid={0}",id);
            int ct = customerBillMapper.selectCount(cb);
            if(ct > 1){
                return false;
            }
            EntityWrapper<CustomerBill> delcb = new EntityWrapper<>();
            delcb.where("cid={0} and bid={1}",cid,id);
            customerBillMapper.delete(delcb);

            // 是否为对等账单
            if(IsPeerBillEnum.yes.getValue().equalsIgnoreCase(bill.getIsPeerBill())){ // 是对等账单
                deleteById(id);
                Bill update = new Bill();
                update.setIsPeerBill(IsPeerBillEnum.no.getValue());
                update(update,new EntityWrapper<Bill>().where("bill_sn={0}",bill.getBillSn()));
            }else{ // 非对等账单
                deleteById(id);
            }
            return true;
        }else{
            return false;
        }
    }

    @Override
    public List<BillStatisticsVO> selectStatisticsForBill(Map<String, Object> param) {
        return billMapper.selectStatisticsForBill(param);
    }
}
