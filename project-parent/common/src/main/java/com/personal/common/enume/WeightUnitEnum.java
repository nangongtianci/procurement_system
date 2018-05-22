package com.personal.common.enume;

/**  
 * 商品单位
 * @author ylw  
 * @date 18-5-13 下午8:41
 * @param   
 * @return   
 */ 
public enum WeightUnitEnum {

    ton("ton", "吨"),
    kilo("kilo", "公斤"),
    box("box", "箱")
    ;

    /**
     * 值 String型
     */
    private final String value;
    /**
     * 描述 String型
     */
    private final String description;

    WeightUnitEnum(String value, String description) {
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

    public static WeightUnitEnum getByValue(String value) {
        if (null == value)
            return null;
        for (WeightUnitEnum _enum : WeightUnitEnum.values()) {
            if (value.equals(_enum.getValue()))
                return _enum;
        }
        return null;
    }

    public static WeightUnitEnum getByDescription(String description) {
        if (null == description)
            return null;
        for (WeightUnitEnum _enum : WeightUnitEnum.values()) {
            if (description.equals(_enum.getDescription()))
                return _enum;
        }
        return null;
    }
}
