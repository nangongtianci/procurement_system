package com.personal.common.enume;

/**  
 * 业务状态
 * @author ylw  
 * @date 18-5-13 下午8:41
 * @param   
 * @return   
 */ 
public enum BusinessStatusEnum {

    in("in", "买入"),
    out("out", "卖出"),
    profit("profit", "盘盈"),
    loss("loss", "盘损"),
    ;

    /**
     * 值 String型
     */
    private final String value;
    /**
     * 描述 String型
     */
    private final String description;

    BusinessStatusEnum(String value, String description) {
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

    public static BusinessStatusEnum getByValue(String value) {
        if (null == value)
            return null;
        for (BusinessStatusEnum _enum : BusinessStatusEnum.values()) {
            if (value.equals(_enum.getValue()))
                return _enum;
        }
        return null;
    }

    public static BusinessStatusEnum getByDescription(String description) {
        if (null == description)
            return null;
        for (BusinessStatusEnum _enum : BusinessStatusEnum.values()) {
            if (description.equals(_enum.getDescription()))
                return _enum;
        }
        return null;
    }
}
