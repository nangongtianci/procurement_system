package com.personal.controller;


import com.personal.common.TokenUtils;
import com.personal.common.annotation.InsertMethodFlag;
import com.personal.common.annotation.UpdateMethodFlag;
import com.personal.common.enume.*;
import com.personal.common.utils.base.DateUtil;
import com.personal.common.utils.base.GenerateOrderUtil;
import com.personal.common.utils.base.StringUtils;
import com.personal.common.utils.base.UUIDUtils;
import com.personal.common.utils.collections.ListUtils;
import com.personal.common.utils.file.ExcelUtils;
import com.personal.common.utils.result.Result;
import com.personal.conditions.BillQueryParam;
import com.personal.config.system.file.FileConfig;
import com.personal.config.system.mail.ReportConfig;
import com.personal.entity.Bill;
import com.personal.entity.Customer;
import com.personal.entity.Goods;
import com.personal.service.BillService;
import com.personal.service.CustomerService;
import com.personal.service.MailService;
import com.personal.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;

import static com.personal.common.utils.result.CommonResultMsg.*;
import static com.personal.common.utils.result.RegUtils.*;

/**
 * <p>
 * 账单 前端控制器
 * </p>
 *
 * @author ylw
 * @since 2018-05-13
 */
@RestController
@RequestMapping("/bill")
public class BillController {
    @Autowired
    private BillService billService;
    @Autowired
    private MailService mailService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private FileConfig fileConfig;
    @Autowired
    private ReportConfig reportConfig;
    @Autowired
    private RedisService redisService;

    /**
     * 新增账单
     * @param bill
     * @return
     */
    @InsertMethodFlag
    @PostMapping
    public Result add(HttpServletRequest request,Bill bill){
        String customerId = TokenUtils.getUid(UserTypeEnum.customer,request.getHeader("token"),redisService);
        bill.setCreateCustomerId(customerId);
        if(ListUtils.isEmpty(bill.getGoods())){ // 商品列表
            return Result.FAIL("商品至少要添加一条！");
        }else{
            int i = 1;
            String tip = "第"+i+"条商品信息";
            for(Goods temp : bill.getGoods()){
                temp.setCreateCustomerId(customerId);
                if(StringUtils.isBlank(temp.getName())){
                    return Result.FAIL(assignFieldNotNull(tip+"品名"));
                }

                if(!matchesAmount(temp.getPrice(),new BigDecimal(0))){
                    return Result.FAIL(tip+assignFieldNameForMoney("商品单价",new BigDecimal(0)));
                }

                if(!matchesPositiveInteger(temp.getNumber().toString(), 0)){
                    return Result.FAIL(tip+assignFieldNameForInteger("商品数量",0));
                }

                if(WeightUnitEnum.getByValue(temp.getWeightUnit()) == null){
                    return Result.FAIL(assignFieldIllegalValueRange(tip+"重量单位"));
                }

                if(StringUtils.isBlank(temp.getSecurityDetectionInfo())){
                    return Result.FAIL(assignFieldNotNull(tip+"安全检测信息"));
                }

                temp.setBillId(bill.getId());
                temp.setCreateCustomerId(bill.getCreateCustomerId());
                i++;
            }
        }

        if(BusinessStatusEnum.getByValue(bill.getBusinessStatus()) == null){ // 业务状态
            return Result.FAIL(assignFieldIllegalValueRange("业务状态"));
        }

        if(BusinessStatusEnum.in.getValue().equalsIgnoreCase(bill.getBusinessStatus())){ // 买入
            if(!BillStatusEnum.payable.getValue().equalsIgnoreCase(bill.getBillStatus())
                    && !BillStatusEnum.paid.getValue().equalsIgnoreCase(bill.getBillStatus()) ){
                return Result.FAIL("业务状态为买入，则账单状态只能为应付或已付其中一种状态！");
            }
        }else if(BusinessStatusEnum.out.getValue().equalsIgnoreCase(bill.getBusinessStatus())){ // 卖出
            if(!BillStatusEnum.receivable.getValue().equalsIgnoreCase(bill.getBillStatus())
                    && !BillStatusEnum.received.getValue().equalsIgnoreCase(bill.getBillStatus()) ){
                return Result.FAIL("业务状态为卖出，则账单状态只能为应收或已收其中一种状态！");
            }
        }else{
            if(StringUtils.isNotBlank(bill.getBillStatus())){
                return Result.FAIL("交易类型为盘盈或盘损时，账单状态不必传值！");
            }
            bill.setBillStatus(null);
        }

        if(IsSourceEnum.getByValue(bill.getIsSource()) == null){ // 溯源信息
            return Result.FAIL(assignFieldIllegalValueRange("溯源信息"));
        }

        if(bill.getBillDate() == null){
            return Result.FAIL("账单日期不能为空！");
        }

        // 如果公开溯源信息，则设置为可领取
        if(IsSourceEnum.yes.getValue().equalsIgnoreCase(bill.getIsSource())
                && !BusinessStatusEnum.profit.getValue().equalsIgnoreCase(bill.getBusinessStatus())
                && !BusinessStatusEnum.loss.getValue().equalsIgnoreCase(bill.getBusinessStatus())){
            bill.setIsReceive(IsReceiveEnum.yes.getValue());
        }
        // 生成账单号
        bill.setBillSn(GenerateOrderUtil.nextSN());
        // 设置对等账单为no
        bill.setIsPeerBill(IsPeerBillEnum.no.getValue());
        if(billService.insertCascadeGoods(bill)){
            return Result.OK(bill.getId());
        }
        return Result.FAIL();
    }

    /**
     * 更新账单
     * @param bill
     * @return
     */
    @UpdateMethodFlag
    @PostMapping("/update")
    public Result update(HttpServletRequest request,Bill bill){
        if(!matchesIds(bill.getId())){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.bill));
        }

        if(StringUtils.isNotBlank(bill.getBusinessStatus())
                && BusinessStatusEnum.getByValue(bill.getBusinessStatus()) == null){ // 业务状态
            return Result.FAIL(assignFieldIllegalValueRange("交易状态"));
        }

        if(StringUtils.isNotBlank(bill.getBillStatus())){
            if(BillStatusEnum.getByValue(bill.getBillStatus()) == null){ // 账单状态
                return Result.FAIL(assignFieldIllegalValueRange("账单状态"));
            }else {
                if(BusinessStatusEnum.getByValue(bill.getBusinessStatus()) == null){
                    return Result.FAIL(assignFieldIllegalValueRange("交易类型"));
                }else{
                    if(BusinessStatusEnum.in.getValue().equalsIgnoreCase(bill.getBusinessStatus())){ // 买入
                        if(!BillStatusEnum.payable.getValue().equalsIgnoreCase(bill.getBillStatus())
                                && !BillStatusEnum.paid.getValue().equalsIgnoreCase(bill.getBillStatus()) ){
                            return Result.FAIL("业务状态为买入，则账单状态只能为应付或已付其中一种状态！");
                        }
                    }else if(BusinessStatusEnum.out.getValue().equalsIgnoreCase(bill.getBusinessStatus())){ // 卖出
                        if(!BillStatusEnum.receivable.getValue().equalsIgnoreCase(bill.getBillStatus())
                                && !BillStatusEnum.received.getValue().equalsIgnoreCase(bill.getBillStatus()) ){
                            return Result.FAIL("业务状态为卖出，则账单状态只能为应收或已收其中一种状态！");
                        }
                    }else{
                        if(StringUtils.isNotBlank(bill.getBillStatus())){
                            return Result.FAIL("交易类型为盘盈或盘损时，账单状态不必传值！");
                        }
                        bill.setBillStatus(null);
                    }
                }
            }
        }else{
            bill.setBillStatus(null);
        }
        String customerId = TokenUtils.getUid(UserTypeEnum.customer,request.getHeader("token"),redisService);
        bill.setCreateCustomerId(customerId);
        if(ListUtils.isEmpty(bill.getGoods())){ // 商品列表
            return Result.FAIL("商品至少要添加一条！");
        }else{
            int i = 1;
            String tip = "第"+i+"条商品信息";
            for(Goods temp : bill.getGoods()){
                if(StringUtils.isBlank(temp.getId())){
                    temp.setId(UUIDUtils.getUUID());
                }

                if(StringUtils.isBlank(temp.getName())){
                    return Result.FAIL(assignFieldNotNull(tip+"品名"));
                }

                if(!matchesAmount(temp.getPrice(),new BigDecimal(0))){
                    return Result.FAIL(tip+assignFieldNameForMoney("商品单价",new BigDecimal(0)));
                }

                if(!matchesPositiveInteger(temp.getNumber().toString(), 0)){
                    return Result.FAIL(tip+assignFieldNameForInteger("商品数量",0));
                }

                if(WeightUnitEnum.getByValue(temp.getWeightUnit()) == null){
                    return Result.FAIL(assignFieldIllegalValueRange(tip+"重量单位"));
                }

                if(StringUtils.isBlank(temp.getSecurityDetectionInfo())){
                    return Result.FAIL(assignFieldNotNull(tip+"安全检测信息"));
                }

                temp.setBillId(bill.getId());
                temp.setCreateCustomerId(bill.getCreateCustomerId());
                i++;
            }
        }

        bill.setIsReceive(null);
        bill.setIsSource(null);
        if(billService.updateCascadeGoods(bill)){
            return Result.OK();
        }
        return Result.FAIL();
    }


    /**
     * 根据主键删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable String id){
        if(!matchesIds(id)){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.bill));
        }

        if(billService.deleteByIdAndPeerUpdate(id)){
            return Result.OK();
        }
        return Result.FAIL();
    }

    /**
     * 根据客户主键查询列表信息
     * @param customerId
     * @return
     */
    @RequestMapping("list/{customerId}")
    public Result selectListByCustomerId(@PathVariable String customerId){
        if(!matchesIds(customerId)){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.goods));
        }
        return Result.OK(billService.selectListByCustomerIdCascadeGoods(customerId));
    }

    /**
     * 根据客户主键查询列表信息（级联商品）
     * @param customerId
     * @return
     */
    @RequestMapping("list/cascade/goods/{customerId}")
    public Result selectListByCustomerIdCascadeGoods(@PathVariable String customerId){
        if(!matchesIds(customerId)){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.goods));
        }
        return Result.OK(billService.selectListByCustomerIdCascadeGoods(customerId));
    }

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    @RequestMapping("/{id}")
    public Result selectById(@PathVariable String id){
        if(!matchesIds(id)){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.bill));
        }
        return Result.OK(billService.selectById(id));
    }

    /**
     * 根据主键查询（级联商品）
     * @param id
     * @return
     */
    @RequestMapping("/cascade/goods/{id}")
    public Result selectByIdCascadeGoods(@PathVariable String id){
        if(!matchesIds(id)){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.bill));
        }
        return Result.OK(billService.selectByIdCascadeGoods(id));
    }

    /**
     * 多条件查询
     * @param billQueryParam
     * @return
     */
    @PostMapping("/multi/conditions")
    public Result selectListByMultiConditions(BillQueryParam billQueryParam){
        Result result = multiQueryValidate(billQueryParam);
        if(result != null){
            return result;
        }
        return Result.OK(billService.selectListByMultiConditions(billQueryParam));
    }

    /**
     * 发送报表邮件 (TODO 此方法日后会作性能优化)
     * @param billQueryParam
     * @return
     */
    @PostMapping("/send/mail")
    public Result sendMail(BillQueryParam billQueryParam) throws Exception {
        Result result = multiQueryValidate(billQueryParam);
        if(result != null){
            return result;
        }

        // 生成excel文件
        List<Bill> list = billService.selectListByMultiConditions(billQueryParam);
        if(ListUtils.isEmpty(list)){
            return Result.FAIL("数据为空，无需发送邮件！");
        }

        List<Map<String,Object>> mapList = new ArrayList<>();
        Map<String,Object> map;
        for(Bill tmp : list){
            map = new HashMap<>();
            map.put("customerName",tmp.getCustomerName());
            map.put("customerPhone",tmp.getCustomerPhone());
            map.put("goodsName",joinPropertyValue(tmp.getGoods()));
            map.put("totalPrice",tmp.getTotalPrice());
            map.put("billStatus",BillStatusEnum.getByValue(tmp.getBillStatus()).getDescription());
            mapList.add(map);
        }
        String[] headList = new String[]{"客户名称","客户手机号","商品名称","总价格","账单状态"};
        String[] fieldList = new String[]{"customerName","customerPhone","goodsName","totalPrice","billStatus"};
        String filePathName = fileConfig.getExcelSavePath() + File.separator + "bill_"
                + DateUtil.format(new Date(),"yyyyMMddHHmmss")+".xlsx";
        ExcelUtils.createExcel(filePathName,headList,fieldList,mapList);

        // 发送邮件
        Customer customer = customerService.selectById(billQueryParam.getCreateCustomerId());
        if(mailService.sendAttachmentsMail(customer.getEmail(),reportConfig.getSubject(),reportConfig.getContent(),filePathName)){
            return Result.OK();
        }
        return Result.FAIL();
    }

    private String joinPropertyValue(List<Goods> goods){
        String result = null;
        if(!ListUtils.isEmpty(goods)){
            for(int i = 0;i<goods.size();i++){
                if(i == (goods.size()-1)){
                    result=result+goods.get(i).getName();
                }else{
                    result=result+goods.get(i).getName()+",";
                }
            }
        }
        return result;
    }

    /**
     * 多条件查询通用参数校验
     * @param billQueryParam
     * @return
     */
    private Result multiQueryValidate(BillQueryParam billQueryParam){
        if(!matchesIds(billQueryParam.getCreateCustomerId())){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.customer));
        }

        if(!((billQueryParam.getStartDate() != null && billQueryParam.getEndDate() != null)
                || (billQueryParam.getStartDate() == null && billQueryParam.getEndDate() == null))){
            return Result.FAIL("如果日期作为筛选条件，开始日期和结束日期都不能为空！");
        }

        if(StringUtils.isNotBlank(billQueryParam.getBusinessStatus())
                && BusinessStatusEnum.getByValue(billQueryParam.getBusinessStatus()) == null){
            return Result.FAIL(assignFieldIllegalValueRange("交易类型"));
        }

        if(StringUtils.isNotBlank(billQueryParam.getBillStatus())
                && BillStatusEnum.getByValue(billQueryParam.getBillStatus()) == null){
            return Result.FAIL(assignFieldIllegalValueRange("付款状态"));
        }
        return null;
    }
}

