package com.personal.common.utils.result;

import com.personal.common.utils.base.NumberUtil;
import com.personal.common.utils.base.StringUtils;
import com.personal.common.utils.collections.ArrayUtils;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @desc 通用校验工具类
 */
public class RegUtils {

    /**
     * @desc 校验条件枚举类
     */
    public enum ConditionEnum {
        nameLen(15, "名称长度限制"),
        commonLen(20, "通用字符限制"),
        subNameLen(25, "副标题字符限制"),
        primaryKeyLen(32,"主键长度"),
        countDefaultValue(1,"数量默认值"),
        sortMaxValue(99999,"sort最大值"),
        remarkLen(200,"备注通用长度");
        /**
         * 值 int型
         */
        private final int value;
        /**
         * 描述 String型
         */
        private final String description;

        ConditionEnum(int value, String description) {
            this.value = value;
            this.description = description;
        }

        /**
         * 获取值
         * @return value
         */
        public int getValue() {
            return value;
        }

        /**
         * 获取描述信息
         * @return description
         */
        public String getDescription() {
            return description;
        }

        public static ConditionEnum getByValue(int value) {
            if(value == 0){
                return null;
            }

            for (ConditionEnum _enum : ConditionEnum.values()) {
                if (value == _enum.getValue())
                    return _enum;
            }
            return null;
        }

        public static ConditionEnum getByDescription(String description) {
            if (null == description)
                return null;
            for (ConditionEnum _enum : ConditionEnum.values()) {
                if (description.equals(_enum.getDescription()))
                    return _enum;
            }
            return null;
        }
    }

    /**
     * @desc 主键校验
     * @param sort
     * @return
     */
    public static final boolean matchesSort(String sort){
        if(StringUtils.isBlank(sort) || !NumberUtil.isInteger(sort)
                || Integer.parseInt(sort) < ConditionEnum.countDefaultValue.getValue()
                || Integer.parseInt(sort) > ConditionEnum.sortMaxValue.getValue()){
            return false;
        }
        return true;
    }

    /**
     * @desc 主键校验
     * @param ids
     * @return
     */
    public static final boolean matchesIds(String ...ids){
        if(ArrayUtils.isEmpty(ids)){
            return false;
        }

        for(String id : ids){
            if(Objects.isNull(id) || id.length() != ConditionEnum.primaryKeyLen.getValue()
                    || !Pattern.matches("^[0-9a-zA-Z]+$",id)){
                return false;
            }
        }
        return true;
    }

    /**
     * @desc 匹配指定的正整数（大于等于）
     * @param input 接收的未知内容
     * @param startNum (下限数字)
     * @return
     */
    public static final boolean matchesPositiveInteger(String input, int startNum){
        if(!NumberUtil.isInteger(input) || Integer.parseInt(input) < startNum){
            return false;
        }
        return true;
    }

    /**
     * @desc 匹配指定的正整数（在某区间段）
     * @param input 接收的未知内容
     * @param startNum (下限数字)
     * @param endNum (上线数字)
     * @return
     */
    public static final boolean matchesPositiveIntegerSection(String input, int startNum, int endNum){
        if(StringUtils.isBlank(input) || (input.length() < startNum || input.length()>endNum)){
            return false;
        }
        return true;
    }

    /**
     * @desc 匹配当前金额是否大于指定金额
     * @param input
     * @param limit
     * @return
     */
    public static final boolean matchesAmount(BigDecimal input, BigDecimal limit){
        int result = input.compareTo(limit);
        if(result <= 0){
            return false;
        }
        return true;
    }

    /**
     * @desc 通用编码校验
     * @param code
     * @return
     */
    public static final boolean matchesCode(String code){
        if(StringUtils.isBlank(code)){
            return false;
        }
        return Pattern.matches("^[a-zA-Z_0-9]{1,15}$",code);
    }

    /**
     * @desc 通用字符校验（中文|英文）
     * @param str
     * @return
     */
    public static final boolean matchesChar(String str,int length){
        if(StringUtils.isBlank(str)){
            return false;
        }
        return Pattern.matches("^[\\u4e00-\\u9fa5a-zA-Z0-9()（）]{1,"+length+"}$",str);
    }

    /**
     * @desc 通用手机号校验
     * @param mobilePhone
     * @return
     */
    public static final boolean matchesMobilePhone(String mobilePhone){
        if(StringUtils.isBlank(mobilePhone)){
            return false;
        }
        return Pattern.matches("^((13[0-9])|(15[0-9])|(147)|(17[0-9])|(18[0-9]))\\d{8}$",mobilePhone);
    }

    /**
     * @desc 通用身份证校验
     * @param idCard
     * @return
     */
    public static final boolean matchesIdCard(String idCard){
        if(StringUtils.isBlank(idCard)){
            return false;
        }
        return Pattern.matches("^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$",idCard);
    }
}
