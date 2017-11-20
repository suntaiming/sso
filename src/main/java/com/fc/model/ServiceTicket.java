package com.fc.model;

public class ServiceTicket {
    private long id;
    /**
     * ST Id
     */
    private String ticketId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 服务Id
     */
    private String serviceId;
    /**
     * 跳转服务url
     */
    private String redirectServiceUrl;
    /**
     * ST
     */
    private String serviceTicket;
    /**
     * 记录时间
     */
    private long recordTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getRedirectServiceUrl() {
        return redirectServiceUrl;
    }

    public void setRedirectServiceUrl(String redirectServiceUrl) {
        this.redirectServiceUrl = redirectServiceUrl;
    }

    public String getServiceTicket() {
        return serviceTicket;
    }

    public void setServiceTicket(String serviceTicket) {
        this.serviceTicket = serviceTicket;
    }

    public long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(long recordTime) {
        this.recordTime = recordTime;
    }
}
