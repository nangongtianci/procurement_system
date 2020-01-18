package com.msb.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.msb.common.utils.base.StringUtils;
import com.msb.common.utils.base.UUIDUtils;
import com.msb.common.utils.collections.ArrayUtils;
import com.msb.common.utils.collections.Collections3;
import com.msb.common.utils.exceptions.BizException;
import com.msb.common.utils.exceptions.enums.BizExceptionEnum;
import com.msb.common.utils.result.Result;
import com.msb.entity.*;
import com.msb.entity.vo.UpAndDownStreamListVO;
import com.msb.mapper.*;
import com.msb.requestParam.ReceiveOrPaymentParam;
import com.msb.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 客户信息 服务实现类
 * </p>
 *
 * @author ylw
 * @since 2019-05-27
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private CustomerBillRelationMapper customerBillRelationMapper;
    @Autowired
    private BillMapper billMapper;
    // 留存记录
    @Autowired
    private RemainRecordMapper remainRecordMapper;
    // 收付款记录
    @Autowired
    private ReceiptPaymentRecordMapper receiptPaymentRecordMapper;
    // 收付款记录与账单对应关系
    @Autowired
    private ReceiptPaymentRecordBillMapper receiptPaymentRecordBillMapper;

    @Override
    public List<UpAndDownStreamListVO> getUpAndDownStreamList(String id, String isTop) {
        return customerMapper.getUpAndDownStreamList(id,isTop);
    }

    @Transactional
    @Override
    public Result billing(ReceiveOrPaymentParam param) {
        ReceiptPaymentRecord rpr = new ReceiptPaymentRecord(); // 收付款记录
        rpr.setId(UUIDUtils.getUUID());
        rpr.setCreateId(param.getCreateId());

        EntityWrapper<CustomerBillRelation> ew = new EntityWrapper<>();

        RemainRecord condition = new RemainRecord();
        condition.setCreateId(param.getCreateId());

        BigDecimal remainAmount = new BigDecimal(0); // 留存金额
        BigDecimal totalAmount ; // 本次总还款金额=留存+当前输入金额

        StringBuilder sb = new StringBuilder("账单主键：%s");
        String billStatus,billPeerStatus;
        if("1".equalsIgnoreCase(param.getIsTop())){ // 上游(买入)
            ew.where("business_status={0} and bill_status={1} and is_peer={2} and create_id={3}",0,2,1,param.getCreateId());
            condition.setIsPayment("1");
            sb.append(",不属于买入账单范围内！");
            rpr.setIsPayment("1"); // 付款
            billStatus = "0"; // 已付
            billPeerStatus = "1"; // 已收
        } else { // 下游（卖出）
            ew.where("business_status={0} and bill_status={1} and is_peer={2} and create_id={3}",1,3,1,param.getCreateId());
            condition.setIsPayment("0");
            sb.append(",不属于卖出账单范围内！");
            rpr.setIsPayment("0"); // 收款
            billStatus = "1"; // 已收
            billPeerStatus = "0"; // 已付
        }

        ew.orderBy("create_time",true);

        List<CustomerBillRelation> bills = customerBillRelationMapper.selectList(ew);
        if(bills.size()<=0){
            return Result.FAIL("[应收或应付]且对等账单不存在！");
        }

        RemainRecord rr = remainRecordMapper.selectOne(condition);
        if(!Objects.isNull(rr)){
            remainAmount = remainAmount.add(rr.getRemainAmount());
        }

        totalAmount = param.getAmount().add(remainAmount);
        rpr.setAmount(param.getAmount()); // 收付款记录金额
        rpr.setCustomerId(bills.get(0).getCustomerId());
        receiptPaymentRecordMapper.insert(rpr);

        ReceiptPaymentRecordBill rprb;
        if(!ArrayUtils.isEmpty(param.getBillIds())){ // 指定销账
            String billIds = Collections3.extractToString(bills,"billId",",");
            for(String tmp : param.getBillIds()){
                if(billIds.indexOf(tmp) == -1){
                    return Result.FAIL(String.format(sb.toString(),tmp));
                }
            }

            List<Bill> assignBills = billMapper.selectBatchIds(Lists.newArrayList(param.getBillIds()));
            for(Bill tmp : assignBills){
                if(totalAmount.compareTo(new BigDecimal(0)) > 0){
                    if(totalAmount.compareTo(tmp.getActualTotalPrice()) >= 0) { // 总额足够还款
                        rprb = new ReceiptPaymentRecordBill();
                        rprb.setId(UUIDUtils.getUUID());
                        rprb.setBillId(tmp.getId());
                        rprb.setCreateId(param.getCreateId());
                        rprb.setIsPayment(rpr.getIsPayment());
                        rprb.setRpId(rpr.getId());
                        rprb.setSplitAmount(tmp.getActualTotalPrice());
                        totalAmount = totalAmount.subtract(tmp.getActualTotalPrice());
                        receiptPaymentRecordBillMapper.insert(rprb);
                        // 更新状态 (应付->已付，应收->已收)

                        // 更新自己
                        tmp.setBillStatus(billStatus);
                        billMapper.updateById(tmp);
                        CustomerBillRelation cbr = new CustomerBillRelation();
                        cbr.setBillStatus(billStatus);
                        int cbrCT = customerBillRelationMapper.update(cbr,
                                new EntityWrapper<CustomerBillRelation>().
                                        where("create_id={0} and bill_id={1} and is_peer='1'",param.getCreateId(),tmp.getId()));
                        if(cbrCT != 1){
                            throw new BizException(BizExceptionEnum.INVALID_CLIENT_ID.DATA_UPDATED_ERROR);
                        }

                        // 更新对等账单
                        CustomerBillRelation cbrQ = new CustomerBillRelation();
                        cbrQ.setCustomerId(param.getCreateId());
                        cbrQ.setSourceBillId(tmp.getId());
                        cbrQ.setIsPeer("1");
                        CustomerBillRelation peerBillCustomer = customerBillRelationMapper.selectOne(cbrQ);
                        if(Objects.isNull(peerBillCustomer)){
                            throw new BizException(BizExceptionEnum.INVALID_CLIENT_ID.DATA_UPDATED_ERROR);
                        }
                        peerBillCustomer.setBillStatus(billPeerStatus);
                        customerBillRelationMapper.updateById(peerBillCustomer);

                        Bill peerBill = new Bill();
                        peerBill.setId(peerBillCustomer.getBillId());
                        peerBill.setBillStatus(billPeerStatus);
                        billMapper.updateById(peerBill);
                    }
                }else{
                    break;
                }
            }
        }else{ // 自动销账（根据账单创建时间）
            for(CustomerBillRelation tmp : bills){
                if(totalAmount.compareTo(new BigDecimal(0)) > 0){
                    if(totalAmount.compareTo(tmp.getActualTotalPrice()) >= 0) { // 总额足够还款
                        rprb = new ReceiptPaymentRecordBill();
                        rprb.setId(UUIDUtils.getUUID());
                        rprb.setBillId(tmp.getBillId());
                        rprb.setCreateId(param.getCreateId());
                        rprb.setIsPayment(rpr.getIsPayment());
                        rprb.setRpId(rpr.getId());
                        rprb.setSplitAmount(tmp.getActualTotalPrice());
                        totalAmount = totalAmount.subtract(tmp.getActualTotalPrice());
                        receiptPaymentRecordBillMapper.insert(rprb);
                        // 更新状态 (应付->已付，应收->已收)

                        // 更新自己
                        Bill bill = new Bill();
                        bill.setId(tmp.getBillId());
                        bill.setBillStatus(billStatus);
                        billMapper.updateById(bill);
                        tmp.setBillStatus(billStatus);
                        customerBillRelationMapper.updateById(tmp);

                        // 更新对等账单
                        bill.setId(tmp.getSourceBillId());
                        int cbrCT;
                        CustomerBillRelation cbrQ = new CustomerBillRelation();
                        if (StringUtils.isBlank(bill.getId())) { // 新建账单无上游ID
                            cbrQ.setCustomerId(param.getCreateId());
                            cbrQ.setSourceBillId(tmp.getBillId());
                            cbrQ.setIsPeer("1");
                        }else if(StringUtils.isNotBlank(bill.getId()) && "2".equalsIgnoreCase(tmp.getRelationType())){ // 卖货
                            cbrQ.setCreateId(tmp.getCustomerId());
                            cbrQ.setSourceBillId(tmp.getBillId());
                            cbrQ.setCustomerId(tmp.getCreateId());
                            cbrQ.setIsPeer("1");
                        }else{
                            cbrQ.setCreateId(tmp.getSourceId());
                            cbrQ.setBillId(tmp.getSourceBillId());
                            cbrQ.setIsPeer("1");
                        }

                        CustomerBillRelation peer = customerBillRelationMapper.selectOne(cbrQ);
                        if (Objects.isNull(peer)) {
                            throw new BizException(BizExceptionEnum.INVALID_CLIENT_ID.DATA_UPDATED_ERROR);
                        }
                        peer.setBillStatus(billPeerStatus);
                        cbrCT = customerBillRelationMapper.updateById(peer);
                        bill.setId(peer.getBillId());

                        if(cbrCT != 1){
                            throw new BizException(BizExceptionEnum.INVALID_CLIENT_ID.DATA_UPDATED_ERROR);
                        }

                        bill.setBillStatus(billPeerStatus);
                        billMapper.updateById(bill);
                    }
                }else{
                    break;
                }
            }
        }

        if(!Objects.isNull(rr)){
            rr.setRemainAmount(totalAmount);
            rr.setUpdateTime(new Date());
            remainRecordMapper.updateById(rr);
        }else{
            rr = new RemainRecord();
            rr.setId(UUIDUtils.getUUID());
            rr.setCreateId(param.getCreateId());
            rr.setIsPayment(condition.getIsPayment());
            rr.setRemainAmount(totalAmount);
            remainRecordMapper.insert(rr);
        }
        return Result.OK();
    }
}
