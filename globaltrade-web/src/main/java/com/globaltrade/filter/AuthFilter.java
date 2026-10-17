package com.globaltrade.filter;

import com.globaltrade.audit.AuditContext;
import com.globaltrade.bean.AuditBean;
import jakarta.ejb.EJB;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebFilter("/*")
public class AuthFilter implements Filter {
    private static final Logger LOG = Logger.getLogger(AuthFilter.class.getName());

    @EJB
    private AuditBean auditBean;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getRequestURI().substring(request.getContextPath().length());

        boolean publicPath = path.equals("") || path.equals("/") || path.equals("/index.jsp") ||
                path.equals("/login") || path.equals("/login.jsp") ||
                path.startsWith("/assets/") || path.equals("/api/status");

        HttpSession session = request.getSession(false);
        boolean authenticated = session != null && session.getAttribute("userId") != null;

        if (publicPath || authenticated) {
            if (authenticated && !path.startsWith("/assets/")) {
                response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
                response.setHeader("Pragma", "no-cache");
            }

            if (authenticated) {
                Object username = session.getAttribute("username");
                AuditContext.setActor(username == null ? null : String.valueOf(username));
            }

            long requestStart = System.nanoTime();
            try {
                chain.doFilter(req, res);

                if (authenticated &&
                        "POST".equalsIgnoreCase(request.getMethod()) &&
                        !AuditContext.wasRecorded()) {
                    writeAuditFallback(request, response, path, requestStart);
                }
            } finally {
                AuditContext.clear();
            }
            return;
        }

        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }

    private void writeAuditFallback(HttpServletRequest request,
                                    HttpServletResponse response,
                                    String path,
                                    long requestStart) {
        AuditEvent event = eventFor(request, path);
        if (event == null || auditBean == null) {
            return;
        }

        boolean success = response.getStatus() < 400 &&
                request.getAttribute("formError") == null &&
                request.getAttribute("inventoryError") == null;

        String details = success ? event.successDetails : failureDetails(request);
        long elapsed = Math.max(0L, (System.nanoTime() - requestStart) / 1_000_000L);

        HttpSession session = request.getSession(false);
        String actor = session == null ? null : stringValue(session.getAttribute("username"));

        try {
            auditBean.log(actor, event.action, event.entityType, event.entityId,
                    details, elapsed, success);
            AuditContext.markRecorded();
        } catch (Exception auditError) {

            LOG.log(Level.WARNING, "Could not save fallback audit event for " + path, auditError);
        }
    }

    private AuditEvent eventFor(HttpServletRequest request, String path) {
        return switch (path) {
            case "/vendors", "/api/vendors" ->
                    new AuditEvent("VendorBean.create", "Vendor", null,
                            "Vendor created successfully");
            case "/inventory" ->
                    new AuditEvent("InventoryBean.create", "InventoryItem", null,
                            "InventoryItem created successfully");
            case "/inventory/replenish" -> {
                Long itemId = parseLong(request.getParameter("itemId"));
                String quantity = valueOrNA(request.getParameter("quantity"));
                yield new AuditEvent("InventoryBean.replenishStock", "InventoryItem", itemId,
                        "Added " + quantity + " unit(s) to inventory");
            }
            case "/inventory/reconcile" -> {
                Long itemId = parseLong(request.getParameter("itemId"));
                String quantity = valueOrNA(request.getParameter("physicalCount"));
                yield new AuditEvent("InventoryReconciliationBean.reconcile", "InventoryItem", itemId,
                        "Physical inventory count reconciled to " + quantity);
            }
            case "/shipments/new" ->
                    new AuditEvent("ShipmentBean.create", "Shipment", null,
                            "Shipment created successfully");
            case "/customs" ->
                    new AuditEvent("CustomsBean.create", "CustomsDocument", null,
                            "CustomsDocument created successfully");
            case "/route-optimization" ->
                    new AuditEvent("RouteOptimizationBean.optimize", "RouteOptimization", null,
                            "RouteOptimization operation completed");
            case "/vendor-performance" ->
                    new AuditEvent("VendorPerformanceBean.evaluate", "VendorPerformance", null,
                            "Vendor performance evaluation saved");
            default -> null;
        };
    }

    private String failureDetails(HttpServletRequest request) {
        Object error = request.getAttribute("formError");
        if (error == null) {
            error = request.getAttribute("inventoryError");
        }
        String message = stringValue(error);
        return message == null || message.isBlank() ? "Failed request" : "Failed: " + message;
    }

    private String stringValue(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    private String valueOrNA(String value) {
        return value == null || value.isBlank() ? "N/A" : value.trim();
    }

    private Long parseLong(String value) {
        try {
            return value == null || value.isBlank() ? null : Long.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }

    private static class AuditEvent {
        private final String action;
        private final String entityType;
        private final Long entityId;
        private final String successDetails;

        private AuditEvent(String action, String entityType, Long entityId, String successDetails) {
            this.action = action;
            this.entityType = entityType;
            this.entityId = entityId;
            this.successDetails = successDetails;
        }
    }
}
