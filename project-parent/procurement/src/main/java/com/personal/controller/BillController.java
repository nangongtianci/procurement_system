package com.personal.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.personal.common.annotation.InsertMethodFlag;
import com.personal.common.annotation.PageQueryMethodFlag;
import com.personal.common.annotation.UpdateMethodFlag;
import com.personal.common.base.BaseController;
import com.personal.common.cache.RedisUtils;
import com.personal.common.enume.*;
import com.personal.common.json.JsonUtils;
import com.personal.common.utils.base.DateUtil;
import com.personal.common.utils.base.GenerateOrderUtil;
import com.personal.common.utils.base.StringUtils;
import com.personal.common.utils.base.UUIDUtils;
import com.personal.common.utils.collections.ListUtils;
import com.personal.common.utils.file.ExcelUtils;
import com.personal.common.utils.result.PaginationUtils;
import com.personal.common.utils.result.Result;
import com.personal.communicate.HttpUtil;
import com.personal.conditions.BillQueryParam;
import com.personal.config.redis.RedisService;
import com.personal.config.system.file.FileConfig;
import com.personal.config.system.mail.ReportConfig;
import com.personal.config.token.TokenUtils;
import com.personal.entity.*;
import com.personal.entity.vo.BillGoodsForIndexPageVO;
import com.personal.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;

import static com.personal.common.utils.result.CommonResultMsg.*;
import static com.personal.common.utils.result.RegUtils.matchesIds;

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
public class BillController extends BaseController{
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
    @Autowired
    private CustomerBillService customerBillService;

    /**
     * 个人销售榜单排名
     * @param request
     * @return
     */
    @GetMapping("/person/ranking")
    public Result getPersonRankingByDay(HttpServletRequest request){
        String customerId = TokenUtils.getUid(UserTypeEnum.customer,request.getHeader("token"),redisService);
        Customer customer = customerService.selectById(customerId);
        String key = RedisUtils.zsetKey();
        long ranking = redisService.zrevrank(key,customer.getId()+":"+customer.getPhone()+":"+customer.getName())+1;
        return Result.OK(ranking);
    }

    /**
     * 销售榜单前10排名
     * @return
     */
    @GetMapping("/ranking")
    public Result rankingByDay(){
        String key = RedisUtils.zsetKey();
        Set<ZSetOperations.TypedTuple<String>> rankings = redisService.zreverseRangeWithScores(key,0,10);
        return Result.OK(rankings);
    }

    /**
     * 账单置顶
     * @param id
     * @param top
     * @return
     */
    @GetMapping("top/{id}/{top}")
    public Result billTop(@PathVariable String id,@PathVariable int top){
        if(!matchesIds(id)){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.bill));
        }
        Bill bill = new Bill();
        bill.setId(id);
        if(top > 0){
            bill.setIsTop(1);
        }else{
            bill.setIsTop(0);
        }
        if(billService.updateById(bill)){
            return Result.OK();
        }else{
            return Result.FAIL();
        }
    }

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
            // 代卖逻辑-----------------------start
            if(StringUtils.isNotBlank(bill.getPid())){
                if(bill.getGoods().size() > 1){
                    return Result.FAIL("代卖母账单，只能存在一个商品！");
                }
            }
            // 代卖逻辑-----------------------end
            int i = 1;
            String tip = "第"+i+"条商品信息";
            for(Goods temp : bill.getGoods()){
                temp.setCreateCustomerId(customerId);
                if(StringUtils.isBlank(temp.getName())){
                    return Result.FAIL(assignFieldNotNull(tip+"品名"));
                }

                if(temp.getPrice().compareTo(new BigDecimal(0))<0){
                    return Result.FAIL(tip+assignFieldNameForMoney("商品单价",new BigDecimal(0)));
                }

                if(temp.getNumber() <= 0){
                    return Result.FAIL(tip+assignFieldNameForInteger("商品数量",0));
                }

                if(WeightUnitEnum.getByValue(temp.getWeightUnit()) == null){
                    return Result.FAIL(assignFieldIllegalValueRange(tip+"重量单位"));
                }

                if(StringUtils.isNotBlank(temp.getCodeImgPath())
                        && StringUtils.isBlank(temp.getCmid())){
                    return Result.OK("代码管理主键不能为空！");
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
                return Result.FAIL("交易类型为盘盈，盘损，其他费用时，账单状态不必传值！");
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
            feedBacks(customerId,bill.getFeedBacks());
            return Result.OK(bill.getId());
        }
        return Result.FAIL();
    }

    private static void feedBacks(String userId,String feedBacks){
        if(StringUtils.isBlank(feedBacks)){
            return;
        }

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
        HttpUtil.httpPost("http://112.125.89.15/bill/feedbacks",JsonUtils.toJson(data),headerMap);
    }

    /**
     * 分享账单
     * @param phone
     * @return
     */
    @PostMapping("share")
    public Result shareBill(String phone,String billId){
        if(StringUtils.isBlank(phone)){
            return Result.FAIL("手机号不能为空!");
        }

        EntityWrapper<Customer> ew = new EntityWrapper<>();
        ew.where("phone={0}",phone);
        Customer customer = customerService.selectOne(ew);
        if(customer == null){
            return Result.FAIL("请先注册！");
        }

        if(!matchesIds(billId)){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.bill));
        }

        Bill bill = billService.selectById(billId);
        if(bill == null){
           return Result.FAIL("您要分享的账单不存在！");
        }

        if(BusinessStatusEnum.out.getValue().equalsIgnoreCase(bill.getBusinessStatus())){
            return Result.FAIL("对不起，卖出状态下的账单无法分享！");
        }

        billService.shareBill(customer.getId(),bill);
        return Result.OK();
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

        if(IsPeerBillEnum.yes.getValue().equalsIgnoreCase(exist.getIsPeerBill())){
            return Result.FAIL("无法重复扫描生成对等账单！");
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
            return Result.FAIL("盘盈，盘损，其他费用无法生成对等账单！");
        }
        String customerId = TokenUtils.getUid(UserTypeEnum.customer,request.getHeader("token"),redisService);
        if(StringUtils.isBlank(customerId)){
            return Result.FAIL("请先登录！");
        }

        EntityWrapper<Bill> oneselfew = new EntityWrapper();
        oneselfew.where("bill_sn={1}",customerId,sn);
        Bill oneself = billService.selectOne(oneselfew);
        if(oneself != null){
            CustomerBill cb = customerBillService.selectOne(new EntityWrapper<CustomerBill>().
                    where("cid={0} and bid={1}",oneself.getId(),customerId));
            if(cb != null){
                return Result.FAIL("无法扫描生成自己的账单！");
            }
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
        if(billService.insertScan(exist,originalId)){
            return Result.OK();
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

        if(BusinessStatusEnum.getByValue(bill.getBusinessStatus()) == null){ // 业务状态
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
                            return Result.FAIL("交易类型为盘盈,盘损,其他费用时，账单状态不必传值！");
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

                if(temp.getPrice().compareTo(new BigDecimal(0))<0){
                    return Result.FAIL(tip+assignFieldNameForMoney("商品单价",new BigDecimal(0)));
                }

                if(temp.getNumber() <= 0){
                    return Result.FAIL(tip+assignFieldNameForInteger("商品数量",0));
                }

                if(WeightUnitEnum.getByValue(temp.getWeightUnit()) == null){
                    return Result.FAIL(assignFieldIllegalValueRange(tip+"重量单位"));
                }

                if(StringUtils.isNotBlank(temp.getCodeImgPath())
                        && StringUtils.isBlank(temp.getCmid())){
                    return Result.OK("代码管理主键不能为空！");
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
    public Result deleteById(HttpServletRequest request,@PathVariable String id){
        if(!matchesIds(id)){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.bill));
        }

        String customerId = TokenUtils.getUid(UserTypeEnum.customer,request.getHeader("token"),redisService);
        if(billService.deleteByIdAndPeerUpdate(customerId,id)){
            return Result.OK();
        }
        return Result.FAIL();
    }

    /**
     * 账单分页查询（首页）-（级联商品名称分页）
     * @param param
     * @return
     */
    @PageQueryMethodFlag
    @PostMapping("goods/page")
    public Result selectPageForIndex(HttpServletRequest request,BillQueryParam param){
        String customerId = TokenUtils.getUid(UserTypeEnum.customer,request.getHeader("token"),redisService);
        param.setCreateCustomerId(customerId);
        int count = billService.selectCountByCondition(param);
        if (count == 0) {
            return Result.EMPTY();
        }
        List<BillGoodsForIndexPageVO> content = billService.selectByParamForIndexPage(param);
        return PaginationUtils.getResultObj(content,count,param.getPageNow(),param.getPageSize());
    }

    /**
     * 账单查询（多条件,不包含分页信息）-（级联商品分页）
     * @param param
     * @return
     */
    @PostMapping("goods/much/condition")
    public Result selectListByCondition(HttpServletRequest request,BillQueryParam param){
        String customerId = TokenUtils.getUid(UserTypeEnum.customer,request.getHeader("token"),redisService);
        param.setCreateCustomerId(customerId);
        List<Bill> billList = billService.selectByParam(param);

        BigDecimal statisticsAtp = new BigDecimal(0);
        if(!ListUtils.isEmpty(billList)){
            for(Bill bill : billList){
                statisticsAtp = statisticsAtp.add(bill.getActualTotalPrice());
            }
        }

        HashMap<String,Object> rt = new HashMap<>();
        rt.put("statisticsAtp",statisticsAtp.toString());
        rt.put("data",billList);
        return Result.OK(rt);
    }

    /**
     * 根据主键查询（不级联查询商品）
     * @param id
     * @return
     */
    @RequestMapping("/{id}")
    public Result selectById(HttpServletRequest request,@PathVariable String id){
        if(!matchesIds(id)){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.bill));
        }
        String customerId = TokenUtils.getUid(UserTypeEnum.customer,request.getHeader("token"),redisService);
        Customer customer = customerService.selectById(customerId);
        Bill bill = billService.selectById(id);
        if(BusinessStatusEnum.in.getValue().equalsIgnoreCase(bill.getBusinessStatus())){
            bill.setSubList(billService.selectList(new EntityWrapper().where("pid={0}",bill.getId())));
        }

        Map<String,Object> rt = new HashMap<>();
        if(bill.getSubList() != null && bill.getSubList().size()>0){
            BigDecimal in = bill.getActualTotalPrice(); // 买入总额
            BigDecimal out = new BigDecimal(0); // 卖出总额(收入总额)
            BigDecimal profit = new BigDecimal(0); // 盘盈总额
            BigDecimal loss = new BigDecimal(0); // 盘损总额
            BigDecimal other = new BigDecimal(0); // 其他费用总额

            for(Bill tmp : bill.getSubList()){
                switch (BusinessStatusEnum.getByValue(tmp.getBusinessStatus())){
                    case out:
                        out = out.add(tmp.getTotalPrice());
                        break;
                    case profit:
                        profit = profit.add(tmp.getTotalPrice());
                        break;
                    case loss:
                        loss = loss.add(tmp.getTotalPrice());
                        break;
                    case other:
                        other = other.add(tmp.getTotalPrice());
                        break;
                    default:
                        break;
                }
            }

            // 收入总额
            rt.put("incomeTP",out.add(profit));
            // 利润=卖出额+盘盈额-买入额-费用额-盘损额
            rt.put("profitTP",out.add(profit).subtract(in).subtract(loss).subtract(other));
        }

        rt.put("companyName",customer.getCompanyName());
        rt.put("data",bill);
        return Result.OK(rt);
    }

    /**
     * 根据主键查询（级联商品）
     * @param id
     * @return
     */
    @GetMapping("goods/{id}")
    public Result selectByIdCascadeGoods(HttpServletRequest request,@PathVariable String id){
        if(!matchesIds(id)){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.bill));
        }
        String customerId = TokenUtils.getUid(UserTypeEnum.customer,request.getHeader("token"),redisService);
        Customer customer = customerService.selectById(customerId);
        Bill bill = billService.selectByIdCascadeGoods(id);
        if(BusinessStatusEnum.in.getValue().equalsIgnoreCase(bill.getBusinessStatus())){
            bill.setSubList(billService.selectSubBillByPidCascadeGoods(bill.getId()));
        }

        Map<String,Object> rt = new HashMap<>();
        if(bill.getSubList() != null && bill.getSubList().size()>0){
            BigDecimal in = bill.getActualTotalPrice(); // 买入总额
            BigDecimal out = new BigDecimal(0); // 卖出总额(收入总额)
            BigDecimal profit = new BigDecimal(0); // 盘盈总额
            BigDecimal loss = new BigDecimal(0); // 盘损总额
            BigDecimal other = new BigDecimal(0); // 其他费用总额

            for(Bill tmp : bill.getSubList()){
                switch (BusinessStatusEnum.getByValue(tmp.getBusinessStatus())){
                    case out:
                        out = out.add(tmp.getTotalPrice());
                        break;
                    case profit:
                        profit = profit.add(tmp.getTotalPrice());
                        break;
                    case loss:
                        loss = loss.add(tmp.getTotalPrice());
                        break;
                    case other:
                        other = other.add(tmp.getTotalPrice());
                        break;
                    default:
                        break;
                }
            }

            // 收入总额
            rt.put("incomeTP",out.add(profit));
            // 利润=卖出额+盘盈额-买入额-费用额-盘损额
            rt.put("profitTP",out.add(profit).subtract(in).subtract(loss).subtract(other));
        }

        rt.put("companyName",customer.getCompanyName());
        rt.put("data",bill);
        return Result.OK(rt);
    }

    /**
     * 发送报表邮件 (TODO 此方法日后会作性能优化)
     * @param param
     * @return
     */
    @PostMapping("/send/mail")
    public Result sendMail(HttpServletRequest request,BillQueryParam param) throws Exception {
        String customerId = TokenUtils.getUid(UserTypeEnum.customer,request.getHeader("token"),redisService);
        param.setCreateCustomerId(customerId);
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

    /**
     * 账单统计信息
     * @param isReceivable
     * @return
     */
    @PostMapping("statistics")
    public Result selectStatisticsForBill(HttpServletRequest request,
                                          String isReceivable,
                                          Date startDate,
                                          Date endDate){
        String customerId = TokenUtils.getUid(UserTypeEnum.customer,request.getHeader("token"),redisService);
        Map<String,Object> param = new HashMap<>();
        param.put("createCustomerId",customerId);
        if("true".equalsIgnoreCase(isReceivable)){ // 应收界面
            param.put("isReceivable","true");
        }
        param.put("startDate",startDate);
        param.put("endDate",endDate);
        return Result.OK(billService.selectStatisticsForBill(param));
    }
}

