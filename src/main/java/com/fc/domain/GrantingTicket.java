package com.fc.domain;

public class GrantingTicket {
    private String userId;
    private long loginTime;
    private String grantingTicket;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public String getGrantingTicket() {
        return grantingTicket;
    }

    public void setGrantingTicket(String grantingTicket) {
        this.grantingTicket = grantingTicket;
    }
}
