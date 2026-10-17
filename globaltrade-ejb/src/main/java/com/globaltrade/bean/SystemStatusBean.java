package com.globaltrade.bean;
import jakarta.ejb.Stateless;
@Stateless public class SystemStatusBean {
    public String getStatus() {
        return "GlobalTrade EJB backend is running";
    }
}
