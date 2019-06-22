package com.msb.common.enume;

/**  
 * 账单号类型
 * @author ylw  
 * @date 18-5-13 下午8:41
 * @param   
 * @return   
 */ 
public enum BillSnTypeEnum {

    scan("scan", "扫描"),
    manual("manual", "手动"),
    ;

    /**
     * 值 String型
     */
    private final String value;
    /**
     * 描述 String型
     */
    private final String description;

    BillSnTypeEnum(String value, String description) {
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

    public static BillSnTypeEnum getByValue(String value) {
        if (null == value)
            return null;
        for (BillSnTypeEnum _enum : BillSnTypeEnum.values()) {
            if (value.equals(_enum.getValue()))
                return _enum;
        }
        return null;
    }

    public static BillSnTypeEnum getByDescription(String description) {
        if (null == description)
            return null;
        for (BillSnTypeEnum _enum : BillSnTypeEnum.values()) {
            if (description.equals(_enum.getDescription()))
                return _enum;
        }
        return null;
    }
}
