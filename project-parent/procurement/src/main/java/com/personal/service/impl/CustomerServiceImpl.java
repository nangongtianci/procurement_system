package com.personal.service.impl;

import com.msb.common.enume.*;
import com.msb.common.utils.base.GenerateOrderUtil;
import com.msb.common.utils.base.UUIDUtils;
import com.personal.entity.Bill;
import com.personal.entity.Customer;
import com.personal.entity.CustomerBill;
import com.personal.entity.Goods;
import com.personal.mapper.BillMapper;
import com.personal.mapper.CustomerBillMapper;
import com.personal.mapper.CustomerMapper;
import com.personal.mapper.GoodsMapper;
import com.personal.service.CustomerService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 客户信息 服务实现类
 * </p>
 *
 * @author ylw
 * @since 2018-05-13
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private BillMapper billMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private CustomerBillMapper customerBillMapper;

    @Transactional
    @Override
    public void insertCustomerAndInitDefaultBill(Customer customer) {
        customerMapper.insert(customer);
        generateDefaultBill(customer.getId());
    }

    /**
     * 生成默认账单
     */
    private void generateDefaultBill(String cid){
        Bill bill = new Bill();
        bill.setId(UUIDUtils.getUUID());

        Date date = new Date();
        Goods goods = new Goods();
        goods.setId(UUIDUtils.getUUID());
        goods.setCreateTime(date);
        goods.setUpdateTime(date);
        goods.setBillId(bill.getId());
        goods.setAmount(new BigDecimal(5200));
        goods.setPrice(new BigDecimal(5.2));
        goods.setWeightUnit(WeightUnitEnum.box.getValue());
        goods.setNumber(1000);
        goods.setName("苹果");
        goods.setSecurityDetectionInfo("特级");
        goods.setCreateCustomerId(cid);
        goodsMapper.insert(goods);

        bill.setIsTop(0); // 默认不置顶
        bill.setCreateCustomerId(cid);
        bill.setIsPeerBill(IsPeerBillEnum.no.getValue()); // 非对等账单
        bill.setRemark("备注信息");
        bill.setBillDate(new Date());
        bill.setBillStatus(BillStatusEnum.paid.getValue()); // 已付
        bill.setBusinessStatus(BusinessStatusEnum.in.getValue()); // 买入
        bill.setBillSnType(BillSnTypeEnum.manual.getValue()); // 手动
        bill.setBillSn(GenerateOrderUtil.nextSN()); // 账单号
        bill.setMarketName("北京新发地");
        bill.setCustomerName("王大禾");
        bill.setCustomerPhone("18888888888");
        bill.setCityName("北京");
        bill.setIsSource(IsSourceEnum.yes.getValue()); // 是否公开溯源信息
        bill.setIsReceive(IsReceiveEnum.no.getValue()); // 不可领取虚拟币
        bill.setTransactionAddress("北京朝阳");
        bill.setFreight(new BigDecimal(0)); // 运费 0
        bill.setTotalPrice(new BigDecimal(0)); // 总额 0
        bill.setActualTotalPrice(new BigDecimal(0)); // 实付金额 0
        bill.setExpectPayDays(7); // 账期7天
        bill.setCreateTime(date);
        bill.setUpdateTime(date);
        billMapper.insert(bill);

        CustomerBill cb = new CustomerBill();
        cb.setId(UUIDUtils.getUUID());
        cb.setCid(cid);
        cb.setBid(bill.getId());
        cb.setMasterShare(MasterShareEnum.master.getValue());
        cb.setCreateTime(date);
        cb.setUpdateTime(date);
        customerBillMapper.insert(cb);
    }
}
