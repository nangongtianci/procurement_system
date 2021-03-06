package com.msb.common.enume;

/**  
 * 是否为对等账单
 * @author ylw  
 * @date 18-5-13 下午8:41
 * @param   
 * @return   
 */ 
public enum IsPeerBillEnum {

    yes("yes", "是"),
    no("no", "不是");

    /**
     * 值 String型
     */
    private final String value;
    /**
     * 描述 String型
     */
    private final String description;

    IsPeerBillEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    /**
     * 获取值
     *
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * 获取描述信息
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    public static IsPeerBillEnum getByValue(String value) {
        if (null == value)
            return null;
        for (IsPeerBillEnum _enum : IsPeerBillEnum.values()) {
            if (value.equals(_enum.getValue()))
                return _enum;
        }
        return null;
    }

    public static IsPeerBillEnum getByDescription(String description) {
        if (null == description)
            return null;
        for (IsPeerBillEnum _enum : IsPeerBillEnum.values()) {
            if (description.equals(_enum.getDescription()))
                return _enum;
        }
        return null;
    }
}
