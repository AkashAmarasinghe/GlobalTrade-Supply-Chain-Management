package com.globaltrade.bean;

import com.globaltrade.dto.RouteOption;
import com.globaltrade.interceptor.AuditInterceptor;
import com.globaltrade.interceptor.PerformanceInterceptor;
import jakarta.ejb.Stateless;
import jakarta.interceptor.Interceptors;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Interceptors({PerformanceInterceptor.class, AuditInterceptor.class})
public class RouteOptimizationBean {

    public List<RouteOption> optimize(String origin, String destination, String mode, String goal) {
        String from = clean(origin, "Tokyo, Japan");
        String to = clean(destination, "Colombo, Sri Lanka");
        String transport = clean(mode, "Air Freight");
        String objective = clean(goal, "Balanced");

        List<RouteOption> routes = new ArrayList<>();
        boolean fastest = objective.toLowerCase().contains("fast");
        boolean lowestRisk = objective.toLowerCase().contains("risk");
        boolean sea = transport.toLowerCase().contains("sea");

        String firstTransit = sea ? "8d 12h" : "3d 18h";
        String secondTransit = sea ? "9d 8h" : "4d 6h";
        String thirdTransit = sea ? "11d 2h" : "5d 2h";
        String firstRisk = lowestRisk ? "LOW" : "LOW";
        String secondRisk = fastest ? "MEDIUM" : "LOW";

        routes.add(new RouteOption("Route A", from + " → Singapore → " + to,
                firstTransit, firstRisk, "MEDIUM", "RECOMMENDED"));
        routes.add(new RouteOption("Route B", from + " → Bangkok → " + to,
                secondTransit, secondRisk, "LOW", "ALTERNATIVE"));
        routes.add(new RouteOption("Route C", from + " → Dubai → " + to,
                thirdTransit, "MEDIUM", "HIGH", "BACKUP"));
        return routes;
    }

    private String clean(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
    }
}
