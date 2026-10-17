package com.globaltrade.servlet;
import com.globaltrade.bean.*;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
@WebServlet("/dashboard") public class DashboardServlet extends HttpServlet {
    @EJB private DashboardBean dashboardBean;
    @EJB private AlertBean alertBean;
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException {
        req.setAttribute("stats",dashboardBean.stats());
        req.setAttribute("priorityShipments",dashboardBean.priorityShipments());
        req.setAttribute("alerts",alertBean.openAlerts(5));
        req.getRequestDispatcher("/dashboard.jsp").forward(req,resp);
    }
}
