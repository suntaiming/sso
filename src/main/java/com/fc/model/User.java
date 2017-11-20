package com.fc.model;


import java.util.Date;

public class User {

    public final static int STATUS_BLOCK = 0;

    public final static int STATUS_NORMAL = 1;

    private int id;
    /**
     * 会员ID
     */
    private String userId;
    /**
     * 用户名 默认为手机号
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 手机号
     */
    private String phoneNumber;
    /**
     * 应用注册id（由哪个应用注册的用户）
     */
    private String serviceId;
    /**
     * 加密设备SN号
     */
    private String sn;

    /**
     * 用户状态：0停用 1正常  默认正常
     */
    private int status = STATUS_NORMAL;
    /**
     * 注册时间
     */
    private Date registerTime;




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }
}
