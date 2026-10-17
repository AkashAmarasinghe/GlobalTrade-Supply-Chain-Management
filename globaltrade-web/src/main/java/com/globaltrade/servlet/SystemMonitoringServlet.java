package com.globaltrade.servlet;

import com.globaltrade.bean.AlertBean;
import com.globaltrade.bean.AuditBean;
import com.globaltrade.bean.DashboardBean;
import com.globaltrade.bean.SystemStatusBean;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/monitoring")
public class SystemMonitoringServlet extends HttpServlet {
    @EJB private SystemStatusBean statusBean;
    @EJB private DashboardBean dashboardBean;
    @EJB private AlertBean alertBean;
    @EJB private AuditBean auditBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("backendStatus", statusBean.getStatus());
        req.setAttribute("stats", dashboardBean.stats());
        req.setAttribute("alerts", alertBean.openAlerts(10));
        req.setAttribute("logs", auditBean.findRecent(10));
        req.getRequestDispatcher("/WEB-INF/views/system-monitoring.jsp").forward(req, resp);
    }
}
