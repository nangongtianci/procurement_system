package com.msb.common.cache;

import com.msb.common.utils.base.DateUtil;
import com.msb.common.utils.base.UUIDUtils;

/**
 * 例如redis实现用户账单排名工具
 * @author ylw
 * @date 18-11-27 下午2:01
 * @param
 * @return
 */
public class RedisUtils {

    public static String zsetMember(String id,String customerName,String mobile){
        return "";
    }

    public static String zsetKey(){
        return "bill_ranking_"+ DateUtil.getFormatDate(DateUtil.DATE_FORMAT_01);
    }

    /**
     * 积分(红包)key
     * @param customerId
     * @return
     */
    public static String redPacketKey(String customerId){
        return "customer_integral_"+customerId;
    }

    public static void main(String[] args) {
//        System.out.println(DateUtil.getFormateDateSimple());
//        System.out.println("bill_ranking_"+DateUtil.getFormatDate(DateUtil.DATE_FORMAT_01));
        System.out.println(redPacketKey(UUIDUtils.getUUID()));
    }
}
