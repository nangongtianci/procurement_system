package com.msb.common.utils.exceptions.enums;

/**
 * Created by tangm on 2017/4/4.
 */
public enum BizExceptionEnum {
    TIMEOUT(-3, "请求响应超时"),
    UNKOWN(-6, "未知异常"),
    DECODE(-7, "解码失败"),
    REQUEST_EXP(-10, "请求已过期"),
    SERVICE_INVOKING_FAIL(-11, "内部服务调用失败，请重试！"),

    PRIMARY_KEY_NULL(11, "找不到记录"),

    DATA_QUERY_ERROR(101, "查询数据失败"),
    DATA_UPDATED_ERROR(102, "更新数据失败"),
    DATA_DELETED_ERROR(103, "删除数据失败"),
    DATA_INPUT_ERROR(104, "数据未输入"),

    DB_SELECT_RESULT_0(1000, "找不到记录"),
    DB_INSERT_RESULT_0(1001, "数据库操作,insert返回0"),
    DB_UPDATE_RESULT_0(1002, "数据库操作,update返回0"),
    DB_DELETE_RESULT_0(1003, "数据库操作,delete返回0"),
    DB_REMOVE_RESULT_0(1004, "数据库操作,delete返回0"),


    INVALID_CLIENT_TYPE(300, "客户端无效"),
    INVALID_CLIENT_ID(301, "clientID无效"),
    INVALID_TOKEN(302, "token无效"),
    INVALID_USER_NAME(303, "用户名或密码错误"),
    PARAMETER_ERROR(304, "参数错误"),
    MD5_ERROR(307, "MD5校验失败"),
    UID_IS_NULL(308,"获取当前用户失败"),
    COMMON_PARAM_SET_ERROR(309,"AOP通用参数设置失败");

    private int code;
    private String msg;

    /**
     * 构造方法
     * @param code
     * @param msg
     */
    BizExceptionEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
