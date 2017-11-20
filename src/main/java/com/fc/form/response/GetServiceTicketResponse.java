package com.fc.form.response;

/**
 * 获取ST响应模型
 */
public class GetServiceTicketResponse {
    private String userId;
    private String serviceId;
    private String redirectServiceUrl;
    private String serviceTicket;

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
}
