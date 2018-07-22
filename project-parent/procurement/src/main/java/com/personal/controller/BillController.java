package com.personal.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.personal.common.TokenUtils;
import com.personal.common.annotation.InsertMethodFlag;
import com.personal.common.annotation.PageQueryMethodFlag;
import com.personal.common.annotation.UpdateMethodFlag;
import com.personal.common.enume.*;
import com.personal.common.utils.base.DateUtil;
import com.personal.common.utils.base.GenerateOrderUtil;
import com.personal.common.utils.base.StringUtils;
import com.personal.common.utils.base.UUIDUtils;
import com.personal.common.utils.collections.ListUtils;
import com.personal.common.utils.file.ExcelUtils;
import com.personal.common.utils.result.PaginationUtils;
import com.personal.common.utils.result.Result;
import com.personal.config.system.file.FileConfig;
import com.personal.config.system.mail.ReportConfig;
import com.personal.entity.Bill;
import com.personal.entity.Customer;
import com.personal.entity.Goods;
import com.personal.conditions.BillQueryParam;
import com.personal.service.*;
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
    @Autowired
    private GoodsService goodsService;

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
        }else if(BusinessStatusEnum.out.getValue().equalsIgnoreCase(bill.getBusinessStatus())){ // 卖出jintian
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
        // 账单号类型(手动)
        bill.setBillSnType(BillSnTypeEnum.manual.getValue());
        // 设置对等账单为no
        bill.setIsPeerBill(IsPeerBillEnum.no.getValue());
        if(billService.insertCascadeGoods(bill)){
            return Result.OK(bill.getId());
        }
        return Result.FAIL();
    }

    /**
     * 生成对等账单
     * @param request
     * @param sn
     * @return
     */
    @PostMapping("scan")
    public Result scanGeneratorBill(HttpServletRequest request,String sn){
        if(StringUtils.isBlank(sn)){
            return Result.FAIL(assignFieldNotNull("账单号"));
        }

        EntityWrapper<Bill> ew = new EntityWrapper();
        ew.where("bill_sn={0}",sn);
        Bill exist = billService.selectOne(ew);
        if(exist == null){
            return Result.FAIL("原始账单不存在，无法生成对等账单！");
        }
        String originalId = exist.getId();

        // 组件对等账单
        if(BusinessStatusEnum.out.getValue().equals(exist.getBusinessStatus())){ // 卖家
            exist.setBusinessStatus(BusinessStatusEnum.in.getValue());
            if(BillStatusEnum.receivable.getValue().equals(exist.getBillStatus())){
                exist.setBillStatus(BillStatusEnum.payable.getValue());
            }else if(BillStatusEnum.received.getValue().equals(exist.getBillStatus())){
                exist.setBillStatus(BillStatusEnum.paid.getValue());
            }
        }else if(BusinessStatusEnum.in.getValue().equals(exist.getBusinessStatus())){ // 卖家
            exist.setBusinessStatus(BusinessStatusEnum.out.getValue());
            if(BillStatusEnum.payable.getValue().equals(exist.getBillStatus())){
                exist.setBillStatus(BillStatusEnum.receivable.getValue());
            }else if(BillStatusEnum.paid.getValue().equals(exist.getBillStatus())){
                exist.setBillStatus(BillStatusEnum.received.getValue());
            }
        }else{
            return Result.FAIL("盘盈，盘损，无法生成对等账单！");
        }
        String customerId = TokenUtils.getUid(UserTypeEnum.customer,request.getHeader("token"),redisService);
        if(StringUtils.isBlank(customerId)){
            return Result.FAIL("请先登录！");
        }

        Customer customer = customerService.selectById(exist.getCreateCustomerId());
        if(customer != null){
            exist.setCustomerName(customer.getName());
            exist.setCustomerPhone(customer.getPhone());
            exist.setCustomerIdCard(customer.getIdCard());
            exist.setCustomerUnit(customer.getCompanyName());
            exist.setMarketName(customer.getMarketName());
        }

        List<Goods> goods = goodsService.selectList(new EntityWrapper<Goods>().where("bill_id={0}",exist.getId()));
        exist.setId(UUIDUtils.getUUID());
        if(ListUtils.isEmpty(goods)){
            return Result.FAIL("账单中不包含商品，生成失败！");
        }else{
            goods.forEach((item)->{
                item.setId(UUIDUtils.getUUID());
                item.setBillId(exist.getId());
                item.setCreateTime(new Date());
                item.setUpdateTime(new Date());
            });
            exist.setGoods(goods);
        }

        exist.setCreateCustomerId(customerId);
        exist.setCreateTime(new Date());
        exist.setUpdateTime(new Date());
        exist.setBillDate(new Date());
        exist.setBillSn(GenerateOrderUtil.nextSN());
        exist.setBillSnType(BillSnTypeEnum.scan.getValue());
        exist.setIsPeerBill(IsPeerBillEnum.yes.getValue());
        exist.setRemark("");
        if(billService.insertCascadeGoods(exist)){
            // 更新原始账单为对等账单
            Bill original = new Bill();
            original.setId(originalId);
            original.setIsPeerBill(IsPeerBillEnum.yes.getValue());
            if(billService.updateById(original)){
                return Result.OK();
            }
        }
        return Result.FAIL().setMessage("生成对等账单失败！");
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
     * 账单分页查询（多条件）-（级联商品分页）
     * @param param
     * @return
     */
    @PageQueryMethodFlag
    @PostMapping("goods/page")
    public Result selectPageByCondition(BillQueryParam param){
        if(param == null || !matchesIds(param.getCreateCustomerId())){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.customer));
        }

        int count = billService.selectCountByCondition(param);
        if (count == 0) {
            return Result.EMPTY();
        }
        List<Bill> content = billService.selectPageByParam(param);
        return PaginationUtils.getResultObj(content,count,param.getPageNow(),param.getPageSize());
    }

    /**
     * 账单分页查询（多条件）-（不级联商品分页）
     * @param param
     * @return
     */
    @PageQueryMethodFlag
    @PostMapping("page")
    public Result selectPageByParamNoCascadeGoods(BillQueryParam param){
        if(param == null || !matchesIds(param.getCreateCustomerId())){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.customer));
        }

        int count = billService.selectCountByCondition(param);
        if (count == 0) {
            return Result.EMPTY();
        }
        List<Bill> content = billService.selectPageByParamNoCascadeGoods(param);
        return PaginationUtils.getResultObj(content,count,param.getPageNow(),param.getPageSize());
    }

    /**
     * 账单查询（多条件,不包含分页信息）-（级联商品分页）
     * @param param
     * @return
     */
    @PostMapping("goods/list")
    public Result selectListByCondition(BillQueryParam param){
        if(param == null || !matchesIds(param.getCreateCustomerId())){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.customer));
        }
        return Result.OK(billService.selectByParam(param));
    }

    /**
     * 账单查询（多条件,不包含分页信息）-（不级联商品分页）
     * @param param
     * @return
     */
    @PostMapping("list")
    public Result selectByParamNoCascadeGoods(BillQueryParam param){
        if(param == null || !matchesIds(param.getCreateCustomerId())){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.customer));
        }
        return Result.OK(billService.selectByParamNoCascadeGoods(param));
    }

    /**
     * 根据主键查询（不级联查询商品）
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
    @GetMapping("goods/{id}")
    public Result selectByIdCascadeGoods(@PathVariable String id){
        if(!matchesIds(id)){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.bill));
        }
        return Result.OK(billService.selectByIdCascadeGoods(id));
    }

    /**
     * 发送报表邮件 (TODO 此方法日后会作性能优化)
     * @param param
     * @return
     */
    @PostMapping("/send/mail")
    public Result sendMail(BillQueryParam param) throws Exception {
        Result result = multiQueryValidate(param);
        if(result != null){
            return result;
        }

        // 生成excel文件
        List<Bill> list = billService.selectByParam(param);
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
        Customer customer = customerService.selectById(param.getCreateCustomerId());
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

