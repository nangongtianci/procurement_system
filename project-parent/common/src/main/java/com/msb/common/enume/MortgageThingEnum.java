package com.msb.common.enume;

/**  
 * 可抵押物品
 * @author ylw  
 * @date 18-5-13 下午8:41
 * @param   
 * @return   
 */ 
public enum MortgageThingEnum {

    none("none", "无"),
    house("house", "房子"),
    car("box", "车"),
    goods("goods", "货品")
    ;

    /**
     * 值 String型
     */
    private final String value;
    /**
     * 描述 String型
     */
    private final String description;

    MortgageThingEnum(String value, String description) {
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

    public static MortgageThingEnum getByValue(String value) {
        if (null == value)
            return null;
        for (MortgageThingEnum _enum : MortgageThingEnum.values()) {
            if (value.equals(_enum.getValue()))
                return _enum;
        }
        return null;
    }

    public static MortgageThingEnum getByDescription(String description) {
        if (null == description)
            return null;
        for (MortgageThingEnum _enum : MortgageThingEnum.values()) {
            if (description.equals(_enum.getDescription()))
                return _enum;
        }
        return null;
    }
}
