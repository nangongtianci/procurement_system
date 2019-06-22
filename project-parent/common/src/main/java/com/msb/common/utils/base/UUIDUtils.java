package com.msb.common.utils.base;

import java.util.UUID;

/**  
 * uuid 工具
 * @author ylw  
 * @date 18-5-13 下午9:31
 * @param   
 * @return   
 */ 
public class UUIDUtils {
    public UUIDUtils() {
    }

    /**
     * 获得一个UUID
     *
     * @return String UUID
     */
    public static String getUUID() {
        String s = UUID.randomUUID().toString();
        //去掉“-”符号
        return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
    }

    /**
     * 获得指定数目的UUID
     *
     * @param number int 需要获得的UUID数量
     * @return String[] UUID数组
     */
    public static String[] getUUID(int number) {
        if (number < 1) {
            return null;
        }
        String[] ss = new String[number];
        for (int i = 0; i < number; i++) {
            ss[i] = getUUID();
        }
        return ss;
    }

    public static void main(String[] args) {
        System.out.println(getUUID());
        System.out.println("----------------------");
        String[] ss = getUUID(10);
        for (int i = 0; i < ss.length; i++) {
            System.out.println(ss[i]);
        }
    }
}
