package com.personal.common.utils.base;


/**
 * NumberUtil
 *
 * @author zwd
 * @date 2016/6/12
 */
public class NumberUtil {
    /**
     * 判断字符串是否是整数
     */
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    /**
     * 判断字符串是否是浮点数
     */
    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            if (value.contains("."))
                return true;
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是数字
     */
    public static boolean isNumber(String value) {
        return isInteger(value) || isDouble(value);
    }

    /**
     * 如果数字为空，则返回0
     * @param num
     * @return
     */
    public static Integer ifNull(Integer num){
        return num == null ? 0 : num;
    }
}
