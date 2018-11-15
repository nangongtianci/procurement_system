package com.personal.common.enume;

/**  
 * 是否来源共享账单
 * @author ylw  
 * @date 18-5-13 下午8:41
 * @param   
 * @return   
 */ 
public enum MasterShareEnum {

    master("master", "主账单"),
    share("share", "共享账单");

    /**
     * 值 String型
     */
    private final String value;
    /**
     * 描述 String型
     */
    private final String description;

    MasterShareEnum(String value, String description) {
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

    public static MasterShareEnum getByValue(String value) {
        if (null == value)
            return null;
        for (MasterShareEnum _enum : MasterShareEnum.values()) {
            if (value.equals(_enum.getValue()))
                return _enum;
        }
        return null;
    }

    public static MasterShareEnum getByDescription(String description) {
        if (null == description)
            return null;
        for (MasterShareEnum _enum : MasterShareEnum.values()) {
            if (description.equals(_enum.getDescription()))
                return _enum;
        }
        return null;
    }
}
