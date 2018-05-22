package com.personal.common.enume;

/**  
 * 项目模块枚举
 * @author ylw  
 * @date 18-5-13 下午10:15
 * @param   
 * @return   
 */ 
public enum ModuleEnum {
    bill("bill","账单"),
    goods("goods","商品"),
    customer("customer","用户主键"),
    applyLoanRecord("applyLoanRecord","贷款记录"),
    ;


    /**
     * 值 String型
     */
    private final String value;
    /**
     * 模块名称 String型
     */
    private final String name;

    ModuleEnum(String value, String name) {
        this.value = value;
        this.name = name;
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
     * @return name
     */
    public String getName() {
        return name;
    }

    public static ModuleEnum getByValue(String value) {
        if (null == value)
            return null;
        for (ModuleEnum _enum : ModuleEnum.values()) {
            if (value.equals(_enum.getValue()))
                return _enum;
        }
        return null;
    }

    public static ModuleEnum getByName(String name) {
        if (null == name)
            return null;
        for (ModuleEnum _enum : ModuleEnum.values()) {
            if (name.equals(_enum.getName()))
                return _enum;
        }
        return null;
    }
}
