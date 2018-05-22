package com.personal.common.enume;

/**  
 * 是否同意协议
 * @author ylw  
 * @date 18-5-13 下午8:41
 * @param   
 * @return   
 */ 
public enum IsAgreeProtocolEnum {

    yes("yes", "同意"),
    no("no", "不同意");

    /**
     * 值 String型
     */
    private final String value;
    /**
     * 描述 String型
     */
    private final String description;

    IsAgreeProtocolEnum(String value, String description) {
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

    public static IsAgreeProtocolEnum getByValue(String value) {
        if (null == value)
            return null;
        for (IsAgreeProtocolEnum _enum : IsAgreeProtocolEnum.values()) {
            if (value.equals(_enum.getValue()))
                return _enum;
        }
        return null;
    }

    public static IsAgreeProtocolEnum getByDescription(String description) {
        if (null == description)
            return null;
        for (IsAgreeProtocolEnum _enum : IsAgreeProtocolEnum.values()) {
            if (description.equals(_enum.getDescription()))
                return _enum;
        }
        return null;
    }
}
