package com.personal.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.personal.common.base.page.PageQueryParam;
import com.personal.common.cache.RedisUtils;
import com.personal.common.enume.BusinessStatusEnum;
import com.personal.common.enume.IsPeerBillEnum;
import com.personal.common.enume.MasterShareEnum;
import com.personal.common.utils.base.UUIDUtils;
import com.personal.common.utils.collections.Collections3;
import com.personal.common.utils.collections.ListUtils;
import com.personal.common.utils.result.Result;
import com.personal.conditions.BillQueryParam;
import com.personal.config.redis.RedisService;
import com.personal.entity.Bill;
import com.personal.entity.Customer;
import com.personal.entity.CustomerBill;
import com.personal.entity.Goods;
import com.personal.entity.vo.BillGoodsForIndexPageVO;
import com.personal.entity.vo.BillStatisticsVO;
import com.personal.mapper.BillMapper;
import com.personal.mapper.CustomerBillMapper;
import com.personal.mapper.CustomerMapper;
import com.personal.service.BillService;
import com.personal.service.CustomerService;
import com.personal.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private BillService billService;
    @Autowired
    private CustomerBillMapper customerBillMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private RedisService redisService;

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
        if(BusinessStatusEnum.out.getValue().equalsIgnoreCase(bill.getBusinessStatus())){
            // 只有销售账单参与销售排行榜
            setRanking(bill.getCreateCustomerId());
        }
        return true;
    }

    @Transactional
    @Override
    public boolean insertScan(Bill bill,String originId) {
        if(billService.insertCascadeGoods(bill)){
            Customer curCustomer = customerMapper.selectById(bill.getCreateCustomerId());
            // 更新原始账单为对等账单
            Bill original = new Bill();
            original.setId(originId);
            original.setIsPeerBill(IsPeerBillEnum.yes.getValue());
            original.setCustomerName(curCustomer.getName());
            original.setCustomerPhone(curCustomer.getPhone());
            original.setCustomerIdCard(curCustomer.getIdCard());
            original.setCustomerUnit(curCustomer.getCompanyName());
            original.setMarketName(curCustomer.getMarketName());
            if(billService.updateById(original)){
                CustomerBill cb = new CustomerBill();
                cb.setId(UUIDUtils.getUUID());
                cb.setCid(bill.getCreateCustomerId());
                cb.setBid(bill.getId());
                cb.setCreateTime(bill.getCreateTime());
                cb.setUpdateTime(bill.getUpdateTime());
                customerBillMapper.insert(cb);
                if(BusinessStatusEnum.out.getValue().equalsIgnoreCase(bill.getBusinessStatus())){
                    // 只有销售账单参与销售排行榜
                    setRanking(bill.getCreateCustomerId());
                }
                return true;
            }
        }
        return false;
    }

    private void setRanking(String cid){
        BigDecimal sellPrice = new BigDecimal(0);
        Customer customer = customerMapper.selectById(cid);
        List<CustomerBill> customerBillList =  customerBillMapper.selectList(new EntityWrapper<CustomerBill>().where("cid={0}",cid));
        if(!ListUtils.isEmpty(customerBillList)){
            List<String> billIds = Collections3.extractToList(customerBillList,"bid");
            EntityWrapper<Bill> ew = new EntityWrapper<>();
            ew.setSqlSelect("actual_total_price as actualTotalPrice");
            ew.where("business_status={0}",BusinessStatusEnum.out.getValue()).and().in("id",billIds);
            List<Bill> atps = billMapper.selectList(ew);
            if(!ListUtils.isEmpty(atps)){
                for(Bill tmp : atps){
                   sellPrice = sellPrice.add(tmp.getActualTotalPrice());
                }
                String key = RedisUtils.zsetKey();
                redisService.zsadd(key,customer.getId()+":"+customer.getPhone()+":"+customer.getName(),
                        sellPrice.doubleValue());
            }
        }
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

        if(BusinessStatusEnum.out.getValue().equalsIgnoreCase(bill.getBusinessStatus())){
            // 只有销售账单参与销售排行榜
            setRanking(bill.getCreateCustomerId());
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
        ew.setSqlSelect("bill_sn,is_peer_bill,business_status as businessStatus").where("id={0}",id);
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

            if(BusinessStatusEnum.out.getValue().equalsIgnoreCase(bill.getBusinessStatus())){
                // 只有销售账单参与销售排行榜
                setRanking(bill.getCreateCustomerId());
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
