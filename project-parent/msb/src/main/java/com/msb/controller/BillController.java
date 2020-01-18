package com.msb.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.msb.common.annotation.InsertMethodFlag;
import com.msb.common.annotation.PageQueryMethodFlag;
import com.msb.common.annotation.UpdateMethodFlag;
import com.msb.common.base.controller.BaseMsbController;
import com.msb.common.base.validate.ValidateUtils;
import com.msb.common.cache.RedisService;
import com.msb.common.cache.RedisUtils;
import com.msb.common.enume.ModuleEnum;
import com.msb.common.utils.base.DateUtil;
import com.msb.common.utils.base.GenerateOrderUtil;
import com.msb.common.utils.base.StringUtils;
import com.msb.common.utils.base.UUIDUtils;
import com.msb.common.utils.collections.ListUtils;
import com.msb.common.utils.constants.DictConstant;
import com.msb.common.utils.file.ExcelUtils;
import com.msb.common.utils.result.PaginationUtils;
import com.msb.common.utils.result.Result;
import com.msb.config.system.file.FileConfig;
import com.msb.config.system.mail.ReportConfig;
import com.msb.entity.*;
import com.msb.entity.vo.BillProductsForIndexPageVO;
import com.msb.entity.vo.BillProductsForQueryPageVO;
import com.msb.entity.vo.BillStatisticsVO;
import com.msb.requestParam.BillQueryParam;
import com.msb.service.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

import static com.msb.common.utils.result.CommonResultMsg.*;
import static com.msb.common.utils.result.RegUtils.matchesIds;

/**
 * <p>
 * 账单 前端控制器
 * </p>
 *
 * @author ylw
 * @since 2019-06-05
 */
@Api(value = "账单controller",description = "账单控制器")
@RestController
@RequestMapping("/bill")
public class BillController extends BaseMsbController {
    @Autowired
    private BillService billService;
    @Autowired
    private DictService dictService;
    @Autowired
    private BillGoodsService billGoodsService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private CustomerBillRelationService customerBillRelationService;
    @Autowired
    private FileConfig fileConfig;
    @Autowired
    private ReportConfig reportConfig;
    @Autowired
    private MailService mailService;

    /**
     * 新增账单
     * @param bill
     * @return
     */
    @InsertMethodFlag
    @PostMapping("/add")
    public Result add(HttpServletRequest request,@RequestBody Bill bill) {
        String cid = getCid(request.getHeader("token"));
        bill.setCreateCustomerId(cid);

        /**
         * 校验客户信息
         */
        Result result = validateCustomer((byte)1,bill.getCustomer());
        if(!Objects.isNull(result)){
            return result;
        }

        /**
         * 校验商品本身信息
         */
        result = validateBill((byte)1,bill);
        if(!Objects.isNull(result)){
            return result;
        }

        /**
         * 校验货品|商品信息
         */
        result = validateBillGoods((byte)1,bill);
        if(!Objects.isNull(result)){
            return result;
        }

        return billService.add(bill);
    }

    /**
     * 分享账单
     * @param param
     * @return
     */
    @PostMapping("share")
    public Result shareBill(HttpServletRequest request,@RequestBody String param){
        Object phone = getParamByKey(param,"phone");
        Object billId = getParamByKey(param,"billId");
        if(Objects.isNull(phone) || !ValidateUtils.isMobile(phone.toString())){
            return render(null,assignFieldIllegal("手机号"));
        }

        if(Objects.isNull(billId) || !matchesIds(billId.toString())){
            return render(null,assignModuleNameForPK(ModuleEnum.bill));
        }

        String cid = getCid(request.getHeader("token"));
        Customer current = customerService.selectById(cid);
        if(phone.toString().equalsIgnoreCase(current.getPhone())){
            return render(null,"无法分享给自己!");
        }

        EntityWrapper<Customer> ew = new EntityWrapper<>();
        ew.where("phone={0}",phone);
        Customer customer = customerService.selectOne(ew);
        if(Objects.isNull(customer) || StringUtils.isBlank(customer.getId())){
            customer = new Customer();
            customer.setId(UUIDUtils.getUUID());
            customer.setName("临时名称");
            customer.setPhone(phone.toString());
            customer.setIsAgreeProtocol("1");
            customerService.insert(customer);
        }

        Bill bill = billService.selectById(billId.toString());
        if(bill == null){
            return render(null,"您要分享的账单不存在！");
        }

        if(bill.getCid().equalsIgnoreCase(customer.getId())){
            return render(null,"分享手机号不能为本账单客户手机号!");
        }

        EntityWrapper<CustomerBillRelation> cbrEW = new EntityWrapper<>();
        cbrEW.where("create_id={0} and bill_id={1} and relation_type='4'",customer.getId(),billId);
        int ct = customerBillRelationService.selectCount(cbrEW);
        if(ct>0){
            return render(null,"手机号已被分享过，无法重复分享！");
        }

        if(!"0".equalsIgnoreCase(bill.getBusinessStatus())){
            return render(null,"非买入账单无法分享！");
        }

        billService.shareBill(customer.getId(),bill);
        return render(true);
    }

    /**
     * 生成对等账单(扫描)
     * @param request
     * @param param
     * @return
     */
    @PostMapping("scan")
    public Result scan(HttpServletRequest request,@RequestBody String param){
        String cid = getCid(request.getHeader("token"));

        Object sn = getParamByKey(param,"sn");
        if(Objects.isNull(sn) || StringUtils.isBlank(sn.toString())){
            return render(null,assignFieldNotNull("账单号"));
        }

        Bill exist = billService.selectOne(new EntityWrapper().where("bill_sn={0}",sn));
        if(exist == null){
            return render(null,"指定账单不存在，无法生成对等账单！");
        }

        int ct = customerBillRelationService.selectCount(new EntityWrapper<CustomerBillRelation>()
                .where("bill_id={0} and is_peer='1'",exist.getId()));
        if(ct>0){
            return render("当前账单已存在对等账单！");
        }

        if(!cid.equalsIgnoreCase(exist.getCid())){
            return render(null,"当前用户非账单客户，无法生成对等账单！");
        }

        exist.setSourceBillId(exist.getId()); // 原始账单
        exist.setId(UUIDUtils.getUUID()); // 新的账单主键
        // 组件对等账单
        if("1".equals(exist.getBusinessStatus())){ // (卖出)卖家
            exist.setBusinessStatus("0");
            exist.setPid(null);
            // 应收-应付,已收-已付
            if("3".equals(exist.getBillStatus())){
                exist.setBillStatus("2");
            }else if("1".equals(exist.getBillStatus())){
                exist.setBillStatus("0");
            }
        }else if("0".equals(exist.getBusinessStatus())){ // (买入)买家
            exist.setBusinessStatus("1");
            // 应付-应收，已付-已收
            if("2".equals(exist.getBillStatus())){
                exist.setBillStatus("3");
            }else if("0".equals(exist.getBillStatus())){
                exist.setBillStatus("1");
            }
        }else{
            return render(null,"盘盈，盘损，其他费用无法生成对等账单！");
        }

        BillGoods billGoods = billGoodsService.selectOne(new EntityWrapper<BillGoods>().where("bill_id={0}",exist.getSourceBillId()));
        if(Objects.isNull(billGoods)){
            return render(null,"账单中不包含商品，生成失败！");
        }
        billGoods.setId(UUIDUtils.getUUID());
        billGoods.setBillId(exist.getId());
        exist.setBillGoods(billGoods);

        exist.setCid(exist.getCreateCustomerId());
        exist.setCreateCustomerId(cid);
        exist.setBillDate(new Date());
        exist.setBillSn(GenerateOrderUtil.nextSN());
        return billService.scan(exist);
    }


    /**
     * 更新账单
     * @param bill
     * @return
     */
    @UpdateMethodFlag
    @PostMapping("/update")
    public Result update(@RequestBody Bill bill){
        /**
         * 校验货品|商品信息
         */
        Result result = validateBillGoods((byte)0,bill);
        if(!Objects.isNull(result)){
            return result;
        }

        /**
         * 校验客户信息
         */
        result = validateCustomer((byte)0,bill.getCustomer());
        if(!Objects.isNull(result)){
            return result;
        }

        /**
         * 校验商品本身信息
         */
        result = validateBill((byte)0,bill);
        if(!Objects.isNull(result)){
            return result;
        }

        return billService.update(bill);
    }

    /**
     * 查询账单详情
     * @param id
     * @return
     */
    @PostMapping("/{id}")
    public Result getById(@PathVariable String id){
        return render(billService.getBillDetailById(id));
    }

    /**
     * 查询已经存在的商品名称
     * @param request
     * @return
     */
    @PostMapping("/exist/goodsName")
    public Result getExistGoodsName(HttpServletRequest request){
        String cid = getCid(request.getHeader("token"));
        EntityWrapper<BillGoods> ew = new EntityWrapper<>();
        ew.setSqlSelect("distinct name");
        ew.where("create_customer_id={0}",cid);
        return render(billGoodsService.selectObjs(ew));
    }

    private Result validateBillGoods(byte isAdd,Bill bill){
        BillGoods billGoods = bill.getBillGoods();
        if(isAdd==1){ // 新增
            if(Objects.isNull(billGoods)){
                return render(null,"货品信息必须上传！");
            }

            if(StringUtils.isBlank(billGoods.getName())){
                return render(null,assignFieldNotNull("品名"));
            }

            if("0".equalsIgnoreCase(bill.getBusinessStatus())
                    || "1".equalsIgnoreCase(bill.getBusinessStatus())) { // 买入卖出
                int ct = dictService.selectCount(new EntityWrapper<Dict>().
                        where("pid={0} and code={1}",DictConstant.WEIGHT_UNIT_CATEGORY_ID,billGoods.getWeightUnit()));
                if(ct<=0){
                    return render(null,assignFieldIllegalValueRange("重量单位"));
                }
            }
        }else{ // 更新
            if(!Objects.isNull(billGoods)){
                if(!matchesIds(bill.getId())){
                    return render(null,assignModuleNameForPK(ModuleEnum.bill));
                }

                if(StringUtils.isNotBlank(billGoods.getWeightUnit())){
                    int ct = dictService.selectCount(new EntityWrapper<Dict>().
                            where("pid={0} and code={1}",DictConstant.WEIGHT_UNIT_CATEGORY_ID,billGoods.getWeightUnit()));
                    if(ct<=0){
                        return render(null,assignFieldIllegalValueRange("重量单位"));
                    }
                }
            }
        }
        return null;
    }

    private Result validateCustomer(byte isAdd,Customer customer){
        if(isAdd == 1){ // 新增
            if(Objects.isNull(customer)){
                return render(null,"请填写用户信息！");
            }

            if(StringUtils.isBlank(customer.getName())){
                return render(null,assignFieldNotNull("客户名称"));
            }

            if(Objects.isNull(customer.getPhone()) || !ValidateUtils.isMobile(customer.getPhone())){
                return render(null,assignFieldIllegal("手机号"));
            }
        }else{ // 更新
            if(!Objects.isNull(customer)){
                if(!matchesIds(customer.getId())){
                    return render(null,assignModuleNameForPK(ModuleEnum.customer));
                }

                if(StringUtils.isNotBlank(customer.getPhone()) && !ValidateUtils.isMobile(customer.getPhone())){
                    return render(null,assignFieldIllegal("手机号"));
                }
            }
        }
        return null;
    }

    private Result validateBill(byte isAdd,Bill bill){
        if(isAdd == 1) { // 新增
            int bs = dictService.selectCount(new EntityWrapper<Dict>().
                    where("pid={0} and code={1}",DictConstant.BUSINESS_STATUS_CATEGORY_ID,bill.getBusinessStatus()));
            if(bs<=0){
                return render(null,assignFieldIllegalValueRange("交易状态"));
            }

            if("0".equalsIgnoreCase(bill.getBusinessStatus())
                || "1".equalsIgnoreCase(bill.getBusinessStatus())){ // 买入卖出
                int bt = dictService.selectCount(new EntityWrapper<Dict>().
                        where("pid={0} and code={1}",DictConstant.BILL_STATUS_CATEGORY_ID,bill.getBillStatus()));
                if(bt<=0){
                    return render(null,assignFieldIllegalValueRange("账单状态"));
                }

                // 卖货校验
                if(StringUtils.isNotBlank(bill.getPid())){
                    if("0".equalsIgnoreCase(bill.getBusinessStatus())){
                        return render(null,"卖货不能为买入状态！");
                    }

                    BillGoods billGoods = billGoodsService.
                            selectOne(new EntityWrapper<BillGoods>().where("bill_id={0}",bill.getPid()));
                    if(!billGoods.getWeightUnit().equalsIgnoreCase(bill.getBillGoods().getWeightUnit())){
                        return render(null,"卖出货品单位与母账单不一致！");
                    }

                    if(!billGoods.getName().equalsIgnoreCase(bill.getBillGoods().getName())){
                        return render(null,"卖出品名与母账单不一致！");
                    }

                    List<Bill> subBills = billService.getBillsByPidLinkGoods(bill.getPid());
                    float number=0,weight=0;
                    if(!ListUtils.isEmpty(subBills)){
                        for(Bill tmp : subBills){
                            number+=tmp.getBillGoods().getNumber();
                            weight+=tmp.getBillGoods().getWeight();
                        }
                    }

                    if("1".equalsIgnoreCase(billGoods.getWeightUnit())
                            && bill.getBillGoods().getNumber()>(billGoods.getNumber()-number)){ // 元/箱
                        return render(null,"本账单所需货品数量已超过母账单剩余数量！");
                    }

                    if("0".equalsIgnoreCase(billGoods.getWeightUnit())
                            && bill.getBillGoods().getWeight()>(billGoods.getWeight()-weight)){ // 元/斤
                        return render(null,"本账单所需货品斤数已超过母账单剩余斤数！");
                    }
                }
            }
        }else{
            if(!Objects.isNull(bill)){
                if(!matchesIds(bill.getId())){
                    return render(null,assignModuleNameForPK(ModuleEnum.bill));
                }

                if(StringUtils.isNotBlank(bill.getBusinessStatus())){
                    int bs = dictService.selectCount(new EntityWrapper<Dict>().
                            where("pid={0} and code={1}",DictConstant.BUSINESS_STATUS_CATEGORY_ID,bill.getBusinessStatus()));
                    if(bs<=0){
                        return render(null,assignFieldIllegalValueRange("交易状态"));
                    }
                }

                if(StringUtils.isNotBlank(bill.getBillStatus())){
                    int bt = dictService.selectCount(new EntityWrapper<Dict>().
                            where("pid={0} and code={1}",DictConstant.BILL_STATUS_CATEGORY_ID,bill.getBillStatus()));
                    if(bt<=0){
                        return render(null,assignFieldIllegalValueRange("账单状态"));
                    }
                }
            }
        }
        return null;
    }

    /**
     * 个人销售榜单排名
     * @param request
     * @return
     */
    @PostMapping("/person/ranking")
    public Result getPersonRankingByDay(HttpServletRequest request){
        Customer customer = customerService.selectById(getCid(request.getHeader("token")));
        String key = RedisUtils.zsetKey();
        long ranking = redisService.zrevrank(key,customer.getId()+":"+customer.getPhone()+":"+customer.getName())+1;
        return render(ranking);
    }

    /**
     * 销售榜单前10排名
     * @return
     */
    @PostMapping("/ranking")
    public Result rankingByDay(){
        String key = RedisUtils.zsetKey();
        Set<ZSetOperations.TypedTuple<String>> rankings = redisService.zreverseRangeWithScores(key,0,10);
        return render(rankings);
    }

    /**
     * 账单置顶
     * @param id
     * @param top
     * @return
     */
    @ApiOperation(value = "是否置顶",notes = "注意：id=客户账单关系主键,top:0:不置顶，1：置顶")
    @PostMapping("top/{id}/{top}")
    public Result billTop(@PathVariable @ApiParam(name = "id",value = "客户关系主键",required = true) String id,
                          @PathVariable @ApiParam(name = "top",value = "是否置顶",required = true) String top){
        if(!matchesIds(id)){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.bill));
        }

        int ct = dictService.selectCount(new EntityWrapper<Dict>().
                where("pid={0} and code={1}",DictConstant.YES_NO_CATEGORY_ID,top));
        if(ct<=0){
            return render(null,assignFieldIllegalValueRange("是否置顶"));
        }

        CustomerBillRelation exist = customerBillRelationService.selectById(id);
        if(Objects.isNull(exist) || StringUtils.isBlank(exist.getBillId())){
            return render(null,"账单不存在，置顶失败!");
        }

        if(!"0".equalsIgnoreCase(exist.getBusinessStatus())){
            return render(null,"非买入账单无法置顶！");
        }

        CustomerBillRelation cbr = new CustomerBillRelation();
        cbr.setId(id);
        cbr.setIsTop(top);
        return render(customerBillRelationService.updateById(cbr));
    }

    /**
     * 账单分页查询（首页）-（级联商品名称分页）
     * @param param
     * @return
     */
    @PageQueryMethodFlag
    @PostMapping("/page")
    public Result selectPageForIndex(HttpServletRequest request, @RequestBody BillQueryParam param){
        Result result = multiQueryValidate(param);
        if(result != null){
            return result;
        }
        param.setCreateCustomerId(getCid(request.getHeader("token")));
        int count = billService.getCounts(param);
        if (count == 0)
            return Result.EMPTY();
        List<BillProductsForIndexPageVO> content = billService.getPageForBillIndexPage(param);
        return PaginationUtils.getResultObj(content,count,param);
    }


    /**
     * 账单分页多条件查询（查询界面）-（级联商品名称分页）
     * @param param
     * @return
     */
    @ApiOperation(value = "查询界面-分页查询接口",notes = "任何条件都可以不传!")
    @PageQueryMethodFlag
    @PostMapping("/query/page")
    public Result getPageForQueryPage(HttpServletRequest request, @RequestBody BillQueryParam param){
        Result result = multiQueryValidate(param);
        if(result != null){
            return result;
        }
        param.setCreateCustomerId(getCid(request.getHeader("token")));
        int count = billService.getCounts(param);
        if (count == 0)
            return Result.EMPTY();
        List<BillProductsForQueryPageVO> content = billService.getPageForQueryPage(param);
        String total = billService.getTotalForQueryPage(param);
        Map<String,Object> map = new HashMap<>();
        map.put("total",total);
        map.put("page",PaginationUtils.getResultObj(content,count,param).getData());
        return render(map);
    }

    /**
     * 账单统计信息
     * @return
     */
    @ApiOperation(value = "统计",notes = "注意，开始和结束日期如果用户不选择，那么默认传当天日期,开始：xxxx-xx-xx 00:00:00 结束：xxxx-xx-xx 23:59:59")
    @ApiImplicitParams({
            @ApiImplicitParam(name="startDate",value="开始日期",required = true,dataType="string", paramType = "body",example="xxxx-xx-xx 00:00:00"),
            @ApiImplicitParam(name="endDate",value="结束日期",required = true,dataType="string", paramType = "body",example = "xxxx-xx-xx 23:59:59")
    })
    @PostMapping("stat")
    public Result selectStatisticsForBill(HttpServletRequest request,@RequestBody @ApiIgnore String param){
        Object startDate = getParamByKey(param,"startDate");
        if(Objects.isNull(startDate) || StringUtils.isBlank(startDate.toString())){
            return render(null,assignFieldNotNull("开始日期不能为空！"));
        }

        Object endDate = getParamByKey(param,"endDate");
        if(Objects.isNull(endDate) || StringUtils.isBlank(endDate.toString())){
            return render(null,assignFieldNotNull("结束日期不能为空！"));
        }

        // DateUtil.parse(startDate.toString(),DateUtil.DATE_TIME_FORMAT);
        String cid = getCid(request.getHeader("token"));
        Map<String,Object> map = new HashMap<>();
        map.put("createCustomerId",cid);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        return Result.OK(billService.getStatisticsForBill(map));
    }

    @ApiOperation(value = "发送统计邮件",notes = "注意，开始和结束日期如果用户不选择，那么默认传当天日期,开始：xxxx-xx-xx 00:00:00 结束：xxxx-xx-xx 23:59:59")
    @ApiImplicitParams({
            @ApiImplicitParam(name="startDate",value="开始日期",required = true,dataType="string", paramType = "body",example="xxxx-xx-xx 00:00:00"),
            @ApiImplicitParam(name="endDate",value="结束日期",required = true,dataType="string", paramType = "body",example = "xxxx-xx-xx 23:59:59"),
            @ApiImplicitParam(name="mail",value="邮箱",required = true,dataType="string", paramType = "body",example = "xxx@xxx.xxx")
    })
    @PostMapping("stat/mail")
    public Result getStatMail(HttpServletRequest request, @RequestBody @ApiIgnore String param) throws Exception{
        Object startDate = getParamByKey(param,"startDate");
        if(Objects.isNull(startDate) || StringUtils.isBlank(startDate.toString())){
            return render(null,assignFieldNotNull("开始日期不能为空！"));
        }

        Object endDate = getParamByKey(param,"endDate");
        if(Objects.isNull(endDate) || StringUtils.isBlank(endDate.toString())){
            return render(null,assignFieldNotNull("结束日期不能为空！"));
        }

        Object mail = getParamByKey(param,"mail");
        if(Objects.isNull(mail) || !ValidateUtils.isEmail(mail.toString())){
            return render(null,"邮箱不合法!");
        }

        String cid = getCid(request.getHeader("token"));
        Map<String,Object> condition = new HashMap<>();
        condition.put("createCustomerId",cid);
        condition.put("startDate",startDate);
        condition.put("endDate",endDate);
        List<BillStatisticsVO> list = billService.getStatisticsForBill(condition);

        if(ListUtils.isEmpty(list)){
            return render(null,"数据为空，无需发送邮件！");
        }

        List<Map<String,Object>> mapList = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("buy",list.get(0).getCt()+"笔，"+list.get(0).getAtp()+"元");
        map.put("sale",list.get(1).getCt()+"笔，"+list.get(1).getAtp()+"元");
        map.put("ip",list.get(2).getCt()+"笔，"+list.get(2).getAtp()+"元");
        map.put("ad",list.get(3).getCt()+"笔，"+list.get(3).getAtp()+"元");
        map.put("cost",list.get(4).getCt()+"笔，"+list.get(4).getAtp()+"元");
        map.put("profit",(list.get(1).getAtp().add(list.get(2).getAtp()).subtract(list.get(0).getAtp()).subtract(list.get(3).getAtp()).subtract(list.get(4).getAtp()))+"元");
        map.put("payable",list.get(5).getCt()+"笔，"+list.get(5).getAtp()+"元");
        map.put("receive",list.get(6).getCt()+"笔，"+list.get(6).getAtp()+"元");
        map.put("received",list.get(7).getCt()+"笔，"+list.get(7).getAtp()+"元");
        map.put("paid",list.get(8).getCt()+"笔，"+list.get(8).getAtp()+"元");
        map.put("cashSurplus",list.get(7).getAtp().subtract(list.get(8).getAtp()).subtract(list.get(4).getAtp())+"元");
        map.put("totalSurplus",list.get(1).getAtp().subtract(list.get(0).getAtp()).subtract(list.get(4).getAtp())+"元");
        mapList.add(map);

        String[] headList = new String[]{"开始日期","结束日期","买入","卖出","盘盈","盘损","费用","利润合计","应付","应收","已收","已付","现金结余","合计结余"};
        String[] fieldList = new String[]{"startDate","endDate","buy","sale","ip","ad","cost","profit","payable","receive","received","paid","cashSurplus","totalSurplus"};
        String filePathName = fileConfig.getExcelSavePath() + File.separator + "bill_stat_"
                + DateUtil.format(new Date(),"yyyyMMddHHmmss")+".xlsx";
        ExcelUtils.createExcel(filePathName,headList,fieldList,mapList);
        return render(mailService.sendAttachmentsMail(mail.toString(),reportConfig.getSubject(),reportConfig.getContent(),filePathName));
    }

    /**
     * 发送报表邮件
     * @param param
     * @return
     */
    @PostMapping("/send/mail")
    public Result sendMail(HttpServletRequest request,@RequestBody BillQueryParam param) throws Exception {
        param.setCreateCustomerId(getCid(request.getHeader("token")));
        Result result = multiQueryValidate(param);
        if(result != null){
            return result;
        }

        // 生成excel文件
        List<BillProductsForQueryPageVO> list = billService.getBillsByParams(param);
        if(ListUtils.isEmpty(list)){
            return render(null,"数据为空，无需发送邮件！");
        }

        List<Map<String,Object>> mapList = new ArrayList<>();
        Map<String,Object> map;

        List<Map<String,Object>> dicts = dictService.selectMaps(new EntityWrapper<Dict>().
                where("pid={0}",DictConstant.BUSINESS_STATUS_CATEGORY_ID));

        for(BillProductsForQueryPageVO tmp : list){
            map = new HashMap<>();
            map.put("customerName",tmp.getCustomerName());
            map.put("customerPhone",tmp.getCustomerPhone());
            map.put("goodsName",tmp.getGoodsName());
            map.put("totalPrice",tmp.getTotalPrice());
            map.put("actualTotalPrice",tmp.getActualTotalPrice());
            map.put("billStatus",getDictName(dicts,tmp.getBusinessStatus()));
            mapList.add(map);
        }
        String[] headList = new String[]{"客户名称","客户手机号","商品名称","总价格","实收总价","账单状态"};
        String[] fieldList = new String[]{"customerName","customerPhone","goodsName","totalPrice","actualTotalPrice","billStatus"};
        String filePathName = fileConfig.getExcelSavePath() + File.separator + "bill_"
                + DateUtil.format(new Date(),"yyyyMMddHHmmss")+".xlsx";
        ExcelUtils.createExcel(filePathName,headList,fieldList,mapList);

        // 发送邮件
        Customer customer = customerService.selectById(param.getCreateCustomerId());
        return render(mailService.sendAttachmentsMail(customer.getEmail(),reportConfig.getSubject(),reportConfig.getContent(),filePathName));
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

        if(billQueryParam.getStartDate() != null && billQueryParam.getEndDate() != null){
            if(DateUtil.compareDate(billQueryParam.getStartDate(),billQueryParam.getEndDate())){
                return render(null,"开始日期不能大于结束日期！");
            }
        }

        if(StringUtils.isNotBlank(billQueryParam.getIsGoods())){
            int bs = dictService.selectCount(new EntityWrapper<Dict>().
                    where("pid={0} and code={1}",DictConstant.YES_NO_CATEGORY_ID,billQueryParam.getIsGoods()));
            if(bs<=0){
                return render(null,assignFieldIllegalValueRange("是否为商品"));
            }
        }

        if(StringUtils.isNotBlank(billQueryParam.getBusinessStatus())){
            int bs = dictService.selectCount(new EntityWrapper<Dict>().
                    where("pid={0} and code={1}",DictConstant.BUSINESS_STATUS_CATEGORY_ID,billQueryParam.getBusinessStatus()));
            if(bs<=0){
                return render(null,assignFieldIllegalValueRange("交易状态"));
            }
        }

        if(StringUtils.isNotBlank(billQueryParam.getBillStatus())){
            int bt = dictService.selectCount(new EntityWrapper<Dict>().
                    where("pid={0} and code={1}", DictConstant.BILL_STATUS_CATEGORY_ID, billQueryParam.getBillStatus()));
            if (bt <= 0) {
                return render(null, assignFieldIllegalValueRange("账单状态"));
            }
        }
        return null;
    }
}

