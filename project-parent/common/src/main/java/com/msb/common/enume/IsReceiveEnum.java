package com.msb.common.enume;

/**  
 * 是否可领取
 * @author ylw  
 * @date 18-5-13 下午8:41
 * @param   
 * @return   
 */ 
public enum IsReceiveEnum {

    yes("yes", "可领取"),
    no("no", "不可领取");

    /**
     * 值 String型
     */
    private final String value;
    /**
     * 描述 String型
     */
    private final String description;

    IsReceiveEnum(String value, String description) {
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

    public static IsReceiveEnum getByValue(String value) {
        if (null == value)
            return null;
        for (IsReceiveEnum _enum : IsReceiveEnum.values()) {
            if (value.equals(_enum.getValue()))
                return _enum;
        }
        return null;
    }

    public static IsReceiveEnum getByDescription(String description) {
        if (null == description)
            return null;
        for (IsReceiveEnum _enum : IsReceiveEnum.values()) {
            if (description.equals(_enum.getDescription()))
                return _enum;
        }
        return null;
    }
}
