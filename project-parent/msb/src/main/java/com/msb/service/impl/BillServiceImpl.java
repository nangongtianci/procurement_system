package com.msb.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.msb.common.base.page.PageQueryParam;
import com.msb.common.cache.RedisService;
import com.msb.common.cache.RedisUtils;
import com.msb.common.utils.base.GenerateOrderUtil;
import com.msb.common.utils.base.StringUtils;
import com.msb.common.utils.base.UUIDUtils;
import com.msb.common.utils.collections.ListUtils;
import com.msb.common.utils.communicate.HttpUtil;
import com.msb.common.utils.exceptions.BizException;
import com.msb.common.utils.exceptions.enums.BizExceptionEnum;
import com.msb.common.utils.result.Result;
import com.msb.entity.*;
import com.msb.entity.vo.BillProductsForIndexPageVO;
import com.msb.entity.vo.BillProductsForQueryPageVO;
import com.msb.entity.vo.BillStatisticsVO;
import com.msb.mapper.BillGoodsMapper;
import com.msb.mapper.BillMapper;
import com.msb.mapper.CustomerBillRelationMapper;
import com.msb.mapper.CustomerMapper;
import com.msb.requestParam.BillQueryParam;
import com.msb.service.BillService;
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
 * @since 2019-06-05
 */
@Service
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements BillService {

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private BillGoodsMapper billGoodsMapper;
    @Autowired
    private BillMapper billMapper;
    @Autowired
    private CustomerBillRelationMapper customerBillRelationMapper;
    @Autowired
    private RedisService redisService;

    @Transactional
    @Override
    public Result add(Bill bill) {
        setBillGoodsDefaultValue(bill);
        setCustomerDefaultValue(bill);
        setBillDefaultValue(bill);

        billGoodsMapper.insert(bill.getBillGoods());
        billMapper.insert(bill);

        // 客户账单关系（新建|卖货）
        CustomerBillRelation cbr = new CustomerBillRelation();
        cbr.setId(UUIDUtils.getUUID());
        cbr.setCreateId(bill.getCreateCustomerId());
        cbr.setCustomerId(bill.getCid());
        cbr.setBillId(bill.getId());

        // 业务字段
        cbr.setBusinessStatus(bill.getBusinessStatus());
        cbr.setBillStatus(bill.getBillStatus());
        cbr.setTotalPrice(bill.getTotalPrice());
        cbr.setActualTotalPrice(bill.getActualTotalPrice());

        if(StringUtils.isNotBlank(bill.getPid())){
            cbr.setRelationType("2"); // 卖货
            cbr.setSourceBillId(bill.getPid());

            CustomerBillRelation condition = new CustomerBillRelation();
            condition.setBillId(bill.getPid());
            CustomerBillRelation isShare = customerBillRelationMapper.selectOne(condition);
            if("4".equalsIgnoreCase(isShare.getRelationType())){ // 卖货为分享账单
                Bill source = billMapper.selectById(bill.getPid());
                cbr.setSourceId(source.getCreateCustomerId());
            }else{
                cbr.setSourceId(bill.getCreateCustomerId());
            }
        }else{
            cbr.setSourceId(bill.getCreateCustomerId());
            cbr.setRelationType("0"); // 新建
        }
        customerBillRelationMapper.insert(cbr);
        return Result.OK().setData(extra(bill));
    }

    @Transactional
    @Override
    public Result scan(Bill bill) {
        billMapper.insert(bill);
        billGoodsMapper.insert(bill.getBillGoods());

        CustomerBillRelation cbr = new CustomerBillRelation();
        cbr.setId(UUIDUtils.getUUID());
        cbr.setCreateId(bill.getCreateCustomerId());
        cbr.setSourceId(bill.getCid());
        cbr.setCustomerId(bill.getCid());
        cbr.setBillId(bill.getId());
        cbr.setRelationType("1"); // 扫描
        cbr.setSourceBillId(bill.getSourceBillId());
        // 业务字段
        cbr.setBusinessStatus(bill.getBusinessStatus());
        cbr.setBillStatus(bill.getBillStatus());
        cbr.setTotalPrice(bill.getTotalPrice());
        cbr.setActualTotalPrice(bill.getActualTotalPrice());
        cbr.setIsPeer("1"); // 对等
        customerBillRelationMapper.insert(cbr);

        CustomerBillRelation peer = new CustomerBillRelation();
        peer.setIsPeer("1");
        int ct = customerBillRelationMapper.update(peer,new EntityWrapper<CustomerBillRelation>().
                where("create_id={0} and customer_id={1} and bill_id={2}",bill.getCid(),bill.getCreateCustomerId(),bill.getSourceBillId()));
        if(ct != 1){
            throw new BizException(BizExceptionEnum.INVALID_CLIENT_ID.DATA_UPDATED_ERROR);
        }
        return Result.OK().setData(extra(bill));
    }

    /**
     * 新增账单，扫描账单，扩展功能点
     * @param bill
     * @return
     */
    private HashMap<String,String> extra(Bill bill){
        HashMap<String,String> rt = new HashMap<>();
        if("1".equalsIgnoreCase(bill.getBusinessStatus())
                && !Objects.isNull(bill.getTotalPrice())
                && bill.getTotalPrice().compareTo(new BigDecimal(0))>0
        ){ // 卖出
            // 只有销售账单参与销售排行榜
            setRanking(bill.getCreateCustomerId());
            // 只有销售账单才会计算获得红包累计次数
            BigDecimal redPacket = computeRedPacket(bill.getCreateCustomerId()).setScale(2,BigDecimal.ROUND_HALF_DOWN);
            rt.put("redPacketTip","恭喜您得到了"+redPacket+"元红包，继续加油哦!");
        }

        if(StringUtils.isNotBlank(bill.getFeedBacks())){ // 反馈语义信息
            feedBacks(bill.getCreateCustomerId(),bill.getFeedBacks());
        }
        rt.put("billId",bill.getId());
        return rt;
    }

    private void setBillGoodsDefaultValue(Bill bill){
        BillGoods billGoods = bill.getBillGoods();
        billGoods.setBillId(bill.getId());
        billGoods.setCreateCustomerId(bill.getCreateCustomerId());
        billGoods.setIsGoods("0"); // 货品
        if(StringUtils.isBlank(billGoods.getWeightUnit())){
            billGoods.setWeightUnit("1"); // 默认元/斤
        }

        if(Objects.isNull(billGoods.getPrice())){
            billGoods.setPrice(new BigDecimal(0));
        }

        if("0".equalsIgnoreCase(bill.getBusinessStatus())
                && !Objects.isNull(bill.getTotalPrice())
                && bill.getTotalPrice().compareTo(new BigDecimal(0))>0){
            // 账单为买入，并且总价大于零，则货品自动升级为商品！
            billGoods.setIsGoods("1"); // 商品
            billGoods.setGoodsIsShow("1"); // 上架

            if(Objects.isNull(billGoods.getGoodsFixPrice()) || billGoods.getGoodsFixPrice().compareTo(new BigDecimal(0))<=0){
                // 商品价格默认上浮百分之五
                billGoods.setGoodsFixPrice(billGoods.getPrice().add(billGoods.getPrice().multiply(new BigDecimal(0.05))));
            }

            if(Objects.isNull(billGoods.getGoodsStartCount()) || billGoods.getGoodsStartCount()<=0){
                billGoods.setGoodsStartCount(1); // 商品默认起购数量
            }

            if(Objects.isNull(billGoods.getGoodsCostRatio()) || billGoods.getGoodsCostRatio()<=0){
                billGoods.setGoodsCostRatio(5.0f);
            }
        }
    }

    private void setCustomerDefaultValue(Bill bill){
        Customer ct = new Customer();
        ct.setPhone(bill.getCustomer().getPhone());
        ct = customerMapper.selectOne(ct);
        if(Objects.isNull(ct)){
            customerMapper.insert(bill.getCustomer());
        }else{
            bill.setCustomer(ct);
        }
        bill.getCustomer().setStatus("0");
        bill.getCustomer().setIsAgreeProtocol("1");
    }

    private void setBillDefaultValue(Bill bill){
        // 客户主键
        bill.setCid(bill.getCustomer().getId());
        // 生成账单号
        bill.setBillSn(GenerateOrderUtil.nextSN());
        // 是否已领取虚拟币
        bill.setIsReceive("0"); // 未领取
    }

    @Transactional
    @Override
    public Result update(Bill bill) {
        if(!Objects.isNull(bill.getCustomer()) && StringUtils.isNotBlank(bill.getCustomer().getId())){
            bill.getCustomer().setName(null);
            customerMapper.updateById(bill.getCustomer());
        }

        if(!Objects.isNull(bill.getBillGoods()) && StringUtils.isNotBlank(bill.getBillGoods().getId())){
            billGoodsMapper.updateById(bill.getBillGoods());
        }

        billMapper.updateById(bill);
        return Result.OK();
    }

    @Override
    public List<Bill> getBillsByPidLinkGoods(String pid) {
        return billMapper.getBillsByPidLinkGoods(pid);
    }

    @Override
    public Result getBillDetailById(String id) {
        // 查询账单信息
        Bill bill = billMapper.selectById(id);
        if(Objects.isNull(bill)){
            return Result.FAIL("账单不存在！");
        }

        CustomerBillRelation conditionCBR = new CustomerBillRelation();
        conditionCBR.setCreateId(bill.getCreateCustomerId());
        conditionCBR.setBillId(bill.getId());
        conditionCBR.setCustomerId(bill.getCid());
        CustomerBillRelation cbr = customerBillRelationMapper.selectOne(conditionCBR);
        if(Objects.isNull(cbr)){
            return Result.FAIL("账单不存在！");
        }

        bill.setIsPeer(cbr.getIsPeer());
        bill.setRelationType(cbr.getRelationType());

        // 查询用户信息
        bill.setCustomer(customerMapper.selectById(bill.getCid()));

        // 查询货品|商品信息
        BillGoods conditionBG = new BillGoods();
        conditionBG.setBillId(bill.getId());
        bill.setBillGoods(billGoodsMapper.selectOne(conditionBG));

        Map<String,Object> rt = new HashMap<>();
        if("0".equalsIgnoreCase(bill.getBusinessStatus())){ // 买入
            List<Bill> bills = billMapper.getBillsByPidLinkGoods(bill.getId());
            bill.setSubBills(bills);
            // 统计信息
            rt.put("stat",billMapper.selectStat(bill.getId()));
        }

        rt.put("bill",bill);
        return Result.OK(rt);
    }

    @Transactional
    @Override
    public Result shareBill(String cid,Bill bill) {
        CustomerBillRelation cbr = new CustomerBillRelation();
        cbr.setId(UUIDUtils.getUUID());
        cbr.setCreateId(cid); // 输入手机号主键
        cbr.setSourceId(bill.getCreateCustomerId());
        cbr.setCustomerId(bill.getCid());
        cbr.setBillId(bill.getId());
        cbr.setRelationType("4"); // 分享

        // 业务字段
        cbr.setBusinessStatus(bill.getBusinessStatus());
        cbr.setBillStatus(bill.getBillStatus());
        cbr.setTotalPrice(bill.getTotalPrice());
        cbr.setActualTotalPrice(bill.getActualTotalPrice());
        customerBillRelationMapper.insert(cbr);
        return Result.OK();
    }

    @Override
    public int getCounts(BillQueryParam param) {
        return billMapper.getCounts(param);
    }

    @Override
    public List<BillProductsForQueryPageVO> getBillsByParams(BillQueryParam param) {
        return billMapper.getBillsByParams(param);
    }

    @Override
    public List<BillProductsForQueryPageVO> getPageForQueryPage(BillQueryParam param) {
        return billMapper.getPageForQueryPage(param);
    }

    @Override
    public String getTotalForQueryPage(BillQueryParam param) {
        return billMapper.getTotalForQueryPage(param);
    }

    @Override
    public List<BillProductsForIndexPageVO> getPageForBillIndexPage(PageQueryParam param) {
        return billMapper.getPageForBillIndexPage(param);
    }

    @Override
    public List<BillStatisticsVO> getStatisticsForBill(Map<String, Object> param) {
        return billMapper.getStatisticsForBill(param);
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
        HttpUtil.httpPost("http://39.100.68.112/bill/feedbacks", com.personal.common.json.JsonUtils.toJson(data),headerMap);
    }

    /**
     * 排行榜
     * @param cid
     */
    private void setRanking(String cid){
        BigDecimal sellPrice = new BigDecimal(0);
        Customer customer = customerMapper.selectById(cid);
        List<CustomerBillRelation> customerBillList = customerBillRelationMapper.
                selectList(new EntityWrapper<CustomerBillRelation>().where("create_id={0}",cid));
        if(!ListUtils.isEmpty(customerBillList)){
//            List<String> billIds = Collections3.extractToList(customerBillList,"bid");
//            EntityWrapper<Bill> ew = new EntityWrapper<>();
//            ew.setSqlSelect("actual_total_price as actualTotalPrice");
//            ew.where("business_status={0}","1").and().in("id",billIds);
//            List<Bill> atps = billMapper.selectList(ew);
//            if(!ListUtils.isEmpty(atps)){
                for(CustomerBillRelation tmp : customerBillList){
                    sellPrice = sellPrice.add(tmp.getActualTotalPrice());
                }
                String key = RedisUtils.zsetKey();
                redisService.zsadd(key,customer.getId()+":"+customer.getPhone()+":"+customer.getName(),sellPrice.doubleValue());
//            }
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

            Customer cs = customerMapper.selectById(cid);
            cs.setRedPacket(price.add(cs.getRedPacket()));
            customerMapper.updateById(cs);
            return price;
        }
    }


}
