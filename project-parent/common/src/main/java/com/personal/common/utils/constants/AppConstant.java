package com.personal.common.utils.constants;

/**
 * 系统常量设置
 * @author ylw
 * @date 18-6-7 下午1:43
 * @param
 * @return
 */
public class AppConstant {
    /**
     * token过期时间设置(30天，秒数)
     */
    public final static int TOKEN_EXP_TIME = 2592000;
    /**
     * 短信校验码默认90秒
     */
    public final static int CHECK_CODE_EXP_TIME = 90;

    public final static String TOEKN_SALT = "token-salt-618";
    public final static String TOEKN_RPEPIX = "token_appu_";
    public final static String CHECK_CODE_SALT = "check-code-salt-618";
    public final static String CHECK_CODE_RPEPIX = "check_code_";
}
