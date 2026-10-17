package com.globaltrade.servlet;

import com.globaltrade.bean.RouteOptimizationBean;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route-optimization")
public class RouteOptimizationServlet extends HttpServlet {
    @EJB private RouteOptimizationBean routeBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        load(req, "Tokyo, Japan", "Colombo, Sri Lanka", "Air Freight", "Balanced");
        req.getRequestDispatcher("/WEB-INF/views/route-optimization.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        load(req, req.getParameter("origin"), req.getParameter("destination"),
                req.getParameter("mode"), req.getParameter("goal"));
        req.getRequestDispatcher("/WEB-INF/views/route-optimization.jsp").forward(req, resp);
    }

    private void load(HttpServletRequest req, String origin, String destination, String mode, String goal) {
        req.setAttribute("origin", defaultValue(origin, "Tokyo, Japan"));
        req.setAttribute("destination", defaultValue(destination, "Colombo, Sri Lanka"));
        req.setAttribute("mode", defaultValue(mode, "Air Freight"));
        req.setAttribute("goal", defaultValue(goal, "Balanced"));
        req.setAttribute("routes", routeBean.optimize(origin, destination, mode, goal));
    }

    private String defaultValue(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
    }
}
