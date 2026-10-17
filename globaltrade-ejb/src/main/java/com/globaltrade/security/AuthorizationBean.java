package com.globaltrade.security;

import jakarta.annotation.Resource;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;

import java.util.Set;

@Stateless
@DeclareRoles({
        "ADMIN", "LOGISTICS_COORDINATOR", "WAREHOUSE_MANAGER",
        "CUSTOMS_AGENT", "VENDOR", "CUSTOMER"
})
public class AuthorizationBean {

    @Resource
    private SessionContext ctx;

    @PermitAll
    public boolean isCallerInRole(String role) {
        return role != null && ctx.isCallerInRole(role);
    }

    @PermitAll
    public boolean isAuthorized(String role, String path, String httpMethod) {
        if (role == null || path == null) {
            return false;
        }
        if ("ADMIN".equals(role)) {
            return true;
        }

        String method = httpMethod == null ? "GET" : httpMethod.toUpperCase();
        boolean readOnly = "GET".equals(method) || "HEAD".equals(method) || "OPTIONS".equals(method);

        if (matches(path, "/access-control", "/security")) {
            return false;
        }
        if (matches(path, "/audit", "/monitoring", "/api/alerts")) {
            return "LOGISTICS_COORDINATOR".equals(role);
        }
        if (matches(path, "/route-optimization")) {
            return "LOGISTICS_COORDINATOR".equals(role);
        }
        if (matches(path, "/vendor-performance")) {
            return "LOGISTICS_COORDINATOR".equals(role);
        }
        if (matches(path, "/vendors", "/api/vendors")) {
            return "LOGISTICS_COORDINATOR".equals(role);
        }
        if (matches(path, "/customs")) {
            return Set.of("LOGISTICS_COORDINATOR", "CUSTOMS_AGENT").contains(role);
        }
        if (matches(path, "/inventory")) {
            if ("WAREHOUSE_MANAGER".equals(role)) {
                return true;
            }
            return "LOGISTICS_COORDINATOR".equals(role) && readOnly;
        }
        if (matches(path, "/shipments")) {
            if ("LOGISTICS_COORDINATOR".equals(role)) {
                return true;
            }
            return readOnly && Set.of("WAREHOUSE_MANAGER", "CUSTOMS_AGENT", "CUSTOMER").contains(role);
        }

        return true;
    }

    @RolesAllowed({"ADMIN", "LOGISTICS_COORDINATOR"})
    public String shipmentManagementPermission() {
        return "AUTHORIZED";
    }

    @RolesAllowed({"ADMIN", "WAREHOUSE_MANAGER"})
    public String inventoryManagementPermission() {
        return "AUTHORIZED";
    }

    @RolesAllowed({"ADMIN", "CUSTOMS_AGENT"})
    public String customsPermission() {
        return "AUTHORIZED";
    }

    private boolean matches(String path, String... roots) {
        for (String root : roots) {
            if (path.equals(root) || path.startsWith(root + "/")) {
                return true;
            }
        }
        return false;
    }
}
