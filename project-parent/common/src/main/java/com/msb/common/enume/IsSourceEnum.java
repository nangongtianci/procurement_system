package com.msb.common.enume;

/**  
 * 是否公开溯源信息
 * @author ylw  
 * @date 18-5-13 下午8:41
 * @param   
 * @return   
 */ 
public enum IsSourceEnum {

    yes("yes", "公开"),
    no("no", "不公开");

    /**
     * 值 String型
     */
    private final String value;
    /**
     * 描述 String型
     */
    private final String description;

    IsSourceEnum(String value, String description) {
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

    public static IsSourceEnum getByValue(String value) {
        if (null == value)
            return null;
        for (IsSourceEnum _enum : IsSourceEnum.values()) {
            if (value.equals(_enum.getValue()))
                return _enum;
        }
        return null;
    }

    public static IsSourceEnum getByDescription(String description) {
        if (null == description)
            return null;
        for (IsSourceEnum _enum : IsSourceEnum.values()) {
            if (description.equals(_enum.getDescription()))
                return _enum;
        }
        return null;
    }
}
