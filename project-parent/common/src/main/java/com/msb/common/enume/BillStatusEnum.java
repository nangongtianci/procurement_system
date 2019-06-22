package com.msb.common.enume;

/**  
 * 账单状态
 * @author ylw  
 * @date 18-5-13 下午8:41
 * @param   
 * @return   
 */ 
public enum BillStatusEnum {

    paid("paid", "已付"),
    received("received", "已收"),
    payable("payable", "应付"),
    receivable("receivable", "应收")
    ;

    /**
     * 值 String型
     */
    private final String value;
    /**
     * 描述 String型
     */
    private final String description;

    BillStatusEnum(String value, String description) {
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

    public static BillStatusEnum getByValue(String value) {
        if (null == value)
            return null;
        for (BillStatusEnum _enum : BillStatusEnum.values()) {
            if (value.equals(_enum.getValue()))
                return _enum;
        }
        return null;
    }

    public static BillStatusEnum getByDescription(String description) {
        if (null == description)
            return null;
        for (BillStatusEnum _enum : BillStatusEnum.values()) {
            if (description.equals(_enum.getDescription()))
                return _enum;
        }
        return null;
    }
}
