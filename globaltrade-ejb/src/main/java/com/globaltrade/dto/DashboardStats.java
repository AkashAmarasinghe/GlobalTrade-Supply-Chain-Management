package com.globaltrade.dto;
import java.io.Serializable;
public class DashboardStats implements Serializable {
    private long activeShipments;
    private long lowStockItems;
    private long customsDeadlines;
    private double averageVendorScore;
    private long openAlerts;
    public DashboardStats() {
    }
    public DashboardStats(long a,long l,long c,double v,long o) {
        activeShipments=a;
        lowStockItems=l;
        customsDeadlines=c;
        averageVendorScore=v;
        openAlerts=o;
    }
    public long getActiveShipments() {
        return activeShipments;
    }
    public long getLowStockItems() {
        return lowStockItems;
    }
    public long getCustomsDeadlines() {
        return customsDeadlines;
    }
    public double getAverageVendorScore() {
        return averageVendorScore;
    }
    public long getOpenAlerts() {
        return openAlerts;
    }
}
