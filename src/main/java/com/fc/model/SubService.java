package com.fc.model;

import java.util.Date;

/**
 * 子服务注册信息
 */
public class SubService {
    /**
     * 是否退出全局会话：否
     */
    public static final int LOGOUTALL_NO = 0;
    /**
     * 是否退出全局会话：是
     */
    public static final int LOGOUTALL_YES = 1;

    /**
     * 主键
     */
    private int id;
    /**
     * 子服务ID
     */
    private String serviceId;
    /**
     * 服务密码
     */
    private String serviceSecret;
    /**
     * 退出登录请求路径
     */
    private String logoutUrl;
    /**
     * 是否用户登出子服务时 是否退出全局会话 0否   1是   默认为0
     */
    private int logoutAll;
    /**
     * 注册时间
     */
    private Date registerTime;
    /**
     * 修改时间
     */
    private Date modifyTime;


    public SubService(String serviceId, String serviceSecret,int logoutAll) {
        this.serviceId = serviceId;
        this.serviceSecret = serviceSecret;
        this.logoutAll = logoutAll;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceSecret() {
        return serviceSecret;
    }

    public void setServiceSecret(String serviceSecret) {
        this.serviceSecret = serviceSecret;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public int getLogoutAll() {
        return logoutAll;
    }

    public void setLogoutAll(int logoutAll) {
        this.logoutAll = logoutAll;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
