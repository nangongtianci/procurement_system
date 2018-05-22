package com.personal.common.utils.base;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

/**
 * 账单生成器
 * @author ylw
 * @date 18-5-18 下午1:41
 * @param
 * @return
 */
public class GenerateOrderUtil {

    public static synchronized String nextSN() {
        LocalDateTime now = LocalDateTime.now();
        String nowStr = now.getYear() + String.format("%02d", now.getMonthValue())
                + String.format("%02d", now.getDayOfMonth()) + String.format("%02d", now.getHour())
                + String.format("%02d", now.getMinute()) + String.format("%02d", now.getSecond())
                + String.format("%03d", now.getNano());
        return nowStr.substring(0, 17) + String.format("%03d", new Random().nextInt(1000));
    }

    public static synchronized String nextGroupId() {
        return UUID.randomUUID().toString().replace("-","");
    }

    public static void main(String[] args) {
        System.out.println(nextSN().length());
    }
}
