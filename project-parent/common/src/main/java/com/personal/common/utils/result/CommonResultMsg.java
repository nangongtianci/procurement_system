package com.personal.common.utils.result;

import com.personal.common.enume.ModuleEnum;
import com.personal.common.utils.base.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;

public class CommonResultMsg {

    // 通用错误提示 (包含所有业务模块)
    public static final String ILLEGAL_PRIMARY_KEY = "主键不合法！";
    protected static final String ILLEGAL_INTEGER = "必须为大于%d的正整数!";
    public static final String ILLEGAL_CODE = "编码不合法！";
    public static final String ILLEGAL_NAME = "名称不合法！";
    public static final String PARAM_ANALYSIS_ERROR = "参数解析错误！";
    protected static final String FIELD_NOT_NULL = "%s不能为空！";
    protected static final String ILLEGAL_FIELD = "%s不合法！";
    protected static final String ILLEGAL_VALUE_RANGE = "%s值未在规定的取值范围内！";
    public static final String ILLEGAL_DATE_FORMAT = "日期格式不合法！";
    protected static final String IS_NOT_EXISTS = "%s不存在！";
    protected static final String NOT_ASSGIN_VALUE_SECTION = "%s长度不在指定的%s-%s区间内！";
    protected static final String ILLEGAL_MONEY = "%s必须为大于%s的数字！";

    /**
     * @desc 通用字段数值区间通用错误消息
     * @return
     */
    public static final String assignFieldSection(String field,Object start,Object end){
        return String.format(NOT_ASSGIN_VALUE_SECTION,field,start,end);
    }

    /**
     * @desc 某模块不存在错误通用提醒
     * @param moduleEnum
     * @return
     */
    public static final String assignModuleIsNotExists(ModuleEnum moduleEnum){
        return String.format(IS_NOT_EXISTS, moduleEnum.getName());
    }


    /**
     * @desc 日期格式不合法通用提醒
     * @param fieldName
     * @return
     */
    public static final String assignFieldIllegalDateFormat(String fieldName){
        return (StringUtils.isBlank(fieldName)?"":fieldName).concat(ILLEGAL_DATE_FORMAT);
    }

    /**
     * @desc 针对枚举类型不在取值范围内错误通用提醒
     * @param fieldName
     * @return
     */
    public static final String assignFieldIllegalValueRange(String fieldName){
        return String.format(ILLEGAL_VALUE_RANGE,StringUtils.isBlank(fieldName)?"":fieldName);
    }

    /**
     * @desc 针对不通用字段使用，通用字段请使用常量（例如：编码不合法，请使用ILLEGAL_CODE）
     * @param fieldName
     * @return
     */
    public static final String assignFieldIllegal(String fieldName){
        return String.format(ILLEGAL_FIELD,StringUtils.isBlank(fieldName)?"":fieldName);
    }

    /**
     * @desc 针对不通用字段使用，通用字段请使用常量（例如：编码不合法，请使用ILLEGAL_CODE）
     * @param fieldName
     * @return
     */
    public static final String assignFieldNotNull(String fieldName){
        return String.format(FIELD_NOT_NULL,StringUtils.isBlank(fieldName)?"":fieldName);
    }

    /**
     * @desc 指定非法模块名称(id字段)
     * @param moduleEnum
     * @return
     */
    public static final String assignModuleNameForPK(ModuleEnum moduleEnum){
        if(Objects.isNull(moduleEnum)){
            return ILLEGAL_PRIMARY_KEY;
        }else{
            return moduleEnum.getName().concat(ILLEGAL_PRIMARY_KEY);
        }
    }

    /**
     * @dsec 指定非法模块名称（name字段）
     * @param moduleEnum
     * @return
     */
    public static final String assignModuleNameForName(ModuleEnum moduleEnum){
        if(Objects.isNull(moduleEnum)){
            return ILLEGAL_NAME;
        }else{
            return moduleEnum.getName().concat(ILLEGAL_NAME);
        }
    }

    /**
     * @desc 指定字段正整数
     * @param fieldName
     * @return
     */
    public static final String assignFieldNameForInteger(String fieldName,int assignInteger){
        return fieldName.concat(String.format(ILLEGAL_INTEGER,assignInteger));
    }

    /**
     * @desc 金额通用错误提示
     * @param fieldName
     * @return
     */
    public static final String assignFieldNameForMoney(String fieldName, BigDecimal bigDecimal){
        return fieldName.concat(String.format(ILLEGAL_MONEY,fieldName,bigDecimal.toString()));
    }

    // 异常通用提示
    protected static final String ADD_FAIL = "新增%s失败！";
    protected static final String UPDATE_FAIL = "更新%s失败!";
    protected static final String UPDATE_Field_FAIL = "%s中%s更新失败!";
    protected static final String DELETE_FAIL = "删除%s失败！";
    protected static final String QUERY_FAIL = "通过%s查询%s失败!";

    public static final String exceptionTipForAdd(ModuleEnum moduleEnum){
        return String.format(ADD_FAIL,moduleEnum.getName());
    }

    public static final String exceptionTipForUpdate(ModuleEnum moduleEnum){
            return String.format(UPDATE_FAIL,moduleEnum.getName());
    }

    public static final String exceptionTipForUpdate(ModuleEnum moduleEnum,String fieldName){
        return String.format(UPDATE_Field_FAIL,moduleEnum.getName(),fieldName);
    }

    public static final String exceptionTipForDelete(ModuleEnum moduleEnum){
        return String.format(DELETE_FAIL,moduleEnum.getName());
    }

    public static final String exceptionTipForQuery(String fieldName,ModuleEnum moduleEnum){
        return String.format(QUERY_FAIL,fieldName,moduleEnum.getName());
    }

    /**
     * @desc 特殊描述
     * @param fieldName
     * @param specialChar （特殊字符描述，不是模块，例如：小区面积并非小区）
     * @return
     */
    public static final String exceptionTipForQuery(String fieldName,String specialChar){
        return String.format(QUERY_FAIL,fieldName,specialChar);
    }
}
