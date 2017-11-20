package com.fc.core;

/**
 * 项目核心配置
 */
public class CoreConfig {
    /**
     * ST凭证有效时长，单位秒，默认五分钟
     */
    public static final int ST_VALID_TIME = 5 * 60;

    /**
     * TGT授权凭证时长，单位秒，默认五小时
     */
    public static final int TGT_VALID_TIME = 5 * 60 * 60;

    /**
     * 是否允许子服务退出登录时销毁全局会话
     */
    public static final boolean IS_ALLOW_SUBSERVICE_LOGOUT = false;


    /*************************************************短信验证码相关**************************************************/
    /**
     * 创蓝（253）短信验证码 账号
     */
    public final static String SMS_ACCOUNT = "N6077177";
    /**
     * 创蓝（253）短信验证码 密码
     */
    public final static String SMS_PW = "2pNuGnXSjc61a5";
    /**
     * 手机短信模板
     */
    public static String smsMessage = "【牛盾密话】您好, 您的验证码是: %d,请在五分钟之内填写。";
    /**
     * 创蓝（253）短信验证码 是否需要状态报告（默认false），选填
     */
    public static final String SMS_REPORT = "true";
    /**
     * 创蓝（253）短信验证码   短信发送url
     */
    public static final String SMS_URL = "http://smssh1.253.com/msg/send/json";
    /**
     * 短信验证码有效时间
     */
    public static final long SMS_LOSE_TIME = 5 * 60 * 1000;
    /****************************************************************************************************************/
}
