package com.msb.common.enume;
/**
 * 状态
 * @author ylw
 * @date 18-10-28 上午9:45
 * @param
 * @return
 */
public enum StatusEnum {
    yes("yes", "是"),
    no("no", "否"),
    ;

    /**
     * 值 String型
     */
    private final String value;
    /**
     * 描述 String型
     */
    private final String description;

    StatusEnum(String value, String description) {
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

    public static StatusEnum getByValue(String value) {
        if (null == value)
            return null;
        for (StatusEnum _enum : StatusEnum.values()) {
            if (value.equals(_enum.getValue()))
                return _enum;
        }
        return null;
    }

    public static StatusEnum getByDescription(String description) {
        if (null == description)
            return null;
        for (StatusEnum _enum : StatusEnum.values()) {
            if (description.equals(_enum.getDescription()))
                return _enum;
        }
        return null;
    }
}
