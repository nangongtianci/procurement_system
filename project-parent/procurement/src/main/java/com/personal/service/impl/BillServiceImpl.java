package com.personal.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.msb.common.base.page.PageQueryParam;
import com.msb.common.cache.RedisUtils;
import com.msb.common.enume.BusinessStatusEnum;
import com.msb.common.enume.IsPeerBillEnum;
import com.msb.common.enume.MasterShareEnum;
import com.personal.common.json.JsonUtils;
import com.msb.common.utils.base.StringUtils;
import com.msb.common.utils.base.UUIDUtils;
import com.msb.common.utils.collections.Collections3;
import com.msb.common.utils.collections.ListUtils;
import com.msb.common.utils.result.Result;
import com.personal.communicate.HttpUtil;
import com.personal.conditions.BillQueryParam;
import com.personal.config.redis.RedisService;
import com.personal.entity.*;
import com.personal.entity.vo.BillGoodsForIndexPageVO;
import com.personal.entity.vo.BillStatisticsVO;
import com.personal.mapper.BillMapper;
import com.personal.mapper.CustomerBillMapper;
import com.personal.mapper.CustomerMapper;
import com.personal.service.BillService;
import com.personal.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

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
    public Result insertCascadeGoods(Bill bill) {
        try{
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

            HashMap<String,String> rt = new HashMap<>();
            if(BusinessStatusEnum.out.getValue().equalsIgnoreCase(bill.getBusinessStatus())){
                // 只有销售账单参与销售排行榜
                setRanking(bill.getCreateCustomerId());
                // 只有销售账单才会计算获得红包累计次数
                BigDecimal redPacket = computeRedPacket(bill.getCreateCustomerId());
                rt.put("redPacketTip","恭喜您得到了"+redPacket+"元红包，继续加油哦!");
            }


            if(StringUtils.isNotBlank(bill.getFeedBacks())){
                // 反馈语义信息
                feedBacks(bill.getCreateCustomerId(),bill.getFeedBacks());
            }
            rt.put("billId",bill.getId());
            return Result.OK().setData(rt);
        }catch (Exception e){
            return Result.FAIL();
        }
    }

    /**
     * 语义反馈
     * @param userId
     * @param feedBacks
     */
    private static void feedBacks(String userId,String feedBacks){
        String[] splitComma = feedBacks.split(",");

        List<Map<String,Object>> data = new ArrayList<>();
        String[] item;
        FeedBack feedBack = new FeedBack();
        Map<String,Object> param;
        for(int i = 0;i<splitComma.length;i++){
            item = splitComma[i].split(":");
            param = new HashMap<>();
            param.put("userId",userId);
            param.put("operationId",item[0]);
            feedBack.setProductName(item[1]);
            param.put("result",feedBack);
            data.add(param);
        }
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("contentType","application/json");
        HttpUtil.httpPost("http://112.125.89.15/bill/feedbacks", JsonUtils.toJson(data),headerMap);
    }

    @Transactional
    @Override
    public boolean insertScan(Bill bill,String originId) {
        if(billService.insertCascadeGoods(bill).getStatus() == 0){
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
                return true;
            }
        }
        return false;
    }

    /**
     * 排行榜
     * @param cid
     */
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

    /**
     * 计算红包
     * @param cid
     */
    private BigDecimal computeRedPacket(String cid){
        String ct = redisService.get(RedisUtils.redPacketKey(cid));
        if(StringUtils.isNotBlank(ct) && Integer.parseInt(ct)+1 < 5){ // 小于5，不会获取红包hongbao
            redisService.set(RedisUtils.redPacketKey(cid),(Integer.parseInt(ct)+1)+"");
            return null;
        }else{
            redisService.del(RedisUtils.redPacketKey(cid));
            // 随机赠送0.1-3.0元
            double min = 0.1; // 最小值
            double max = 3; // 总和
            int scl =  1; // 小数最大位数
            int pow = (int) Math.pow(max, scl);// 指定小数位
            BigDecimal bg = new BigDecimal(Math.floor((Math.random() * (max - min) + min) * pow) / pow);
            BigDecimal price = bg.setScale(1,BigDecimal.ROUND_DOWN);
            if(price.intValue() == 0){
                price = new BigDecimal(min);
            }

            Customer cs = new Customer();
            cs.setId(cid);
            cs.setRedPacket(price);
            customerMapper.updateById(cs);
            return price;
        }
    }

    @Transactional
    @Override
    public Result updateCascadeGoods(Bill bill) {
        try{
            List<Goods> exists = goodsService.selectList(new EntityWrapper<Goods>().where("bill_id={0}",bill.getId()));
            if(exists.size()>0){
                List<String> goodsIds = Collections3.extractToList(exists,"id");
                if(!goodsService.deleteBatchIds(goodsIds)){
                    return Result.FAIL();
                }
            }

            if(!ListUtils.isEmpty(bill.getGoods()) && !goodsService.insertBatch(bill.getGoods())){
                return Result.FAIL();
            }

            if(!updateById(bill)){
                return Result.FAIL();
            }

            if(BusinessStatusEnum.out.getValue().equalsIgnoreCase(bill.getBusinessStatus())){
                // 只有销售账单参与销售排行榜
                setRanking(bill.getCreateCustomerId());
                // 只有销售账单才会计算获得红包累计次数
                BigDecimal redPacket = computeRedPacket(bill.getCreateCustomerId());
                return Result.OK().setData("恭喜您得到了"+redPacket+"元红包，继续加油哦!");
            }
            return Result.OK();
        }catch (Exception e){
            return Result.FAIL();
        }
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
    public Result deleteByIdAndPeerUpdate(String cid,String id) {
        EntityWrapper<Bill> ew = new EntityWrapper<>();
        ew.setSqlSelect("bill_sn,is_peer_bill,business_status as businessStatus").where("id={0}",id);
        Bill bill = selectById(id);
        if(bill != null){
            // 首先判断是否为父账单
            EntityWrapper<Bill> isParent = new EntityWrapper<>();
            isParent.where("pid={0}",id);
            int rt = billMapper.selectCount(isParent);
            if(rt>0){
                return Result.FAIL("当前账单含有自账单，无法删除！");
            }

            // 主账单和共享账单区别（删除关联关系）
            EntityWrapper<CustomerBill> cb = new EntityWrapper<>();
            cb.where("bid={0}",id);
            int ct = customerBillMapper.selectCount(cb);
            if(ct > 1){
                return Result.FAIL("此账单存已分享，无法删除！");
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
            return Result.OK();
        }else{
            return Result.FAIL("账单不存在，删除失败！");
        }
    }

    @Override
    public List<BillStatisticsVO> selectStatisticsForBill(Map<String, Object> param) {
        return billMapper.selectStatisticsForBill(param);
    }
}
