package com.msb.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.msb.common.annotation.InsertMethodFlag;
import com.msb.common.annotation.PageQueryMethodFlag;
import com.msb.common.annotation.UpdateMethodFlag;
import com.msb.common.base.controller.BaseMsbController;
import com.msb.common.base.validate.ValidateUtils;
import com.msb.common.cache.RedisService;
import com.msb.common.cache.RedisUtils;
import com.msb.common.enume.BillStatusEnum;
import com.msb.common.enume.BusinessStatusEnum;
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
import com.msb.requestParam.BillQueryParam;
import com.msb.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

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
         * 校验货品|商品信息
         */
        Result result = validateBillGoods((byte)1,bill);
        if(!Objects.isNull(result)){
            return result;
        }

        /**
         * 校验客户信息
         */
        result = validateCustomer((byte)1,bill.getCustomer());
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

        EntityWrapper<CustomerBillRelation> cbrEW = new EntityWrapper<>();
        cbrEW.where("cid={0} and bid={1} and pid={2} and relation_type='4'",customer.getId(),billId,cid);
        int ct = customerBillRelationService.selectCount(cbrEW);
        if(ct>0){
            return render(null,"手机号已被分享过，无法重复分享！");
        }

        Bill bill = billService.selectById(billId.toString());
        if(bill == null){
            return render(null,"您要分享的账单不存在！");
        }

        if(!"0".equalsIgnoreCase(bill.getBusinessStatus())){
            return render(null,"非买入账单无法分享！");
        }

        billService.shareBill(customer.getId(),bill.getCreateCustomerId(),bill.getId());
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
                .where("pbid={0} and relation_type={1}",exist.getId(),"1"));
        if(ct>0){
            return render("当前账单已存在对等账单！");
        }

        exist.setSourceBillId(exist.getId()); // 原始账单
        exist.setId(UUIDUtils.getUUID()); // 新的账单主键
        // 组件对等账单
        if("1".equals(exist.getBusinessStatus())){ // (卖出)卖家
            exist.setBusinessStatus("0");
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
        exist.setBillSnType("0"); // 扫描
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
        // 查询账单信息
        Bill bill = billService.selectById(id);
        if(Objects.isNull(bill)){
           return render(null,"账单不存在！");
        }

        // 查询用户信息
        Customer customer = customerService.selectById(bill.getCreateCustomerId());
        bill.setCustomer(customer);

        // 查询货品|商品信息
        BillGoods billGoods = billGoodsService.selectOne(new EntityWrapper<BillGoods>().where("bill_id={0}",bill.getId()));
        bill.setBillGoods(billGoods);

        if("0".equalsIgnoreCase(bill.getBillStatus())){ // 买入
            List<Bill> bills = billService.getBillsByPidLinkGoods(bill.getId());
            bill.setSubBills(bills);
        }
        return render(bill);
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

            int ct = dictService.selectCount(new EntityWrapper<Dict>().
                    where("pid={0} and code={1}",DictConstant.WEIGHT_UNIT_CATEGORY_ID,billGoods.getWeightUnit()));
            if(ct<=0){
                return render(null,assignFieldIllegalValueRange("重量单位"));
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
            bill.setCid(bill.getCustomer().getId());
            int bs = dictService.selectCount(new EntityWrapper<Dict>().
                    where("pid={0} and code={1}",DictConstant.BUSINESS_STATUS_CATEGORY_ID,bill.getBusinessStatus()));
            if(bs<=0){
                return render(null,assignFieldIllegalValueRange("交易状态"));
            }

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
    @PostMapping("top/{id}/{top}")
    public Result billTop(@PathVariable String id,@PathVariable int top){
        if(!matchesIds(id)){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.bill));
        }
        Bill bill = new Bill();
        bill.setId(id);
        if(top > 0){
            bill.setIsTop("1"); // 置顶
        }else{
            bill.setIsTop("0"); // 不置顶
        }
        if(billService.updateById(bill)){
            return render(true);
        }else{
            return render(false);
        }
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
            map.put("billStatus",getDictName(dicts,tmp.getBusinessStatus()));
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
            return render(true);
        }
        return render(false);
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

