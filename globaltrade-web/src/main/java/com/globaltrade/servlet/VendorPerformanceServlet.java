package com.globaltrade.servlet;

import com.globaltrade.bean.VendorBean;
import com.globaltrade.bean.VendorPerformanceBean;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/vendor-performance")
public class VendorPerformanceServlet extends HttpServlet {

    @EJB
    private VendorBean vendorBean;

    @EJB
    private VendorPerformanceBean performanceBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        loadPage(request);
        request.getRequestDispatcher("/WEB-INF/views/vendor-performance.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long vendorId = Long.valueOf(request.getParameter("vendorId"));
            BigDecimal delivery = new BigDecimal(request.getParameter("deliveryScore"));
            BigDecimal quality = new BigDecimal(request.getParameter("qualityScore"));
            BigDecimal compliance = new BigDecimal(request.getParameter("complianceScore"));
            String remarks = request.getParameter("remarks");

            performanceBean.evaluate(vendorId, delivery, quality, compliance, remarks);
            request.getSession().setAttribute("flashSuccess", "Vendor evaluation saved successfully");
            response.sendRedirect(request.getContextPath() + "/vendor-performance");
        } catch (Exception e) {
            request.setAttribute("formError", e.getMessage() == null ? "Unable to save vendor evaluation" : e.getMessage());
            loadPage(request);
            request.getRequestDispatcher("/WEB-INF/views/vendor-performance.jsp").forward(request, response);
        }
    }

    private void loadPage(HttpServletRequest request) {
        request.setAttribute("vendors", vendorBean.findAll());
        request.setAttribute("evaluations", performanceBean.recent());
        Object flash = request.getSession().getAttribute("flashSuccess");
        if (flash != null) {
            request.setAttribute("formSuccess", flash);
            request.getSession().removeAttribute("flashSuccess");
        }
    }
}
