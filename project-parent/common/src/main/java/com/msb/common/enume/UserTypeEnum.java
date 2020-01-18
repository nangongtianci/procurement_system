package com.msb.common.enume;
/**
 * 用户类型
 * @author ylw
 * @date 18-6-7 下午1:54
 * @param
 * @return
 */
public enum UserTypeEnum {
    customer("customer", "客户"),
    employee("employee", "员工"),
    ;

    /**
     * 值 String型
     */
    private final String value;
    /**
     * 描述 String型
     */
    private final String description;

    UserTypeEnum(String value, String description) {
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

    public static UserTypeEnum getByValue(String value) {
        if (null == value)
            return null;
        for (UserTypeEnum _enum : UserTypeEnum.values()) {
            if (value.equals(_enum.getValue()))
                return _enum;
        }
        return null;
    }

    public static UserTypeEnum getByDescription(String description) {
        if (null == description)
            return null;
        for (UserTypeEnum _enum : UserTypeEnum.values()) {
            if (description.equals(_enum.getDescription()))
                return _enum;
        }
        return null;
    }
}
