package com.globaltrade.servlet;

import com.globaltrade.bean.AuthBean;
import com.globaltrade.entity.Role;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/access-control")
public class AccessControlServlet extends HttpServlet {
    @EJB
    private AuthBean authBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, List<String>> permissionCatalog = new LinkedHashMap<>();
        permissionCatalog.put("ADMIN", List.of("Shipments", "Inventory", "Vendors", "Customs", "Audit", "Security"));
        permissionCatalog.put("LOGISTICS_COORDINATOR", List.of("Shipments", "Routes", "Vendors", "Inventory Read", "Audit"));
        permissionCatalog.put("WAREHOUSE_MANAGER", List.of("Inventory", "Shipment Items", "Replenishment"));
        permissionCatalog.put("CUSTOMS_AGENT", List.of("Customs", "Shipment Read", "Approvals"));
        permissionCatalog.put("VENDOR", List.of("Own Profile", "Own Documents", "Performance Read"));
        permissionCatalog.put("CUSTOMER", List.of("Shipment Tracking", "Delivery Status"));

        Map<String, List<String>> permissions = new LinkedHashMap<>();
        for (Role role : authBean.findRoles()) {
            permissions.put(role.getRoleName(),
                    permissionCatalog.getOrDefault(role.getRoleName(), List.of("No application permissions configured")));
        }

        req.setAttribute("permissions", permissions);
        req.setAttribute("databaseRoleCount", permissions.size());
        req.setAttribute("activeUserCount", authBean.countActiveUsers());
        req.getRequestDispatcher("/WEB-INF/views/access-control.jsp").forward(req, resp);
    }
}
