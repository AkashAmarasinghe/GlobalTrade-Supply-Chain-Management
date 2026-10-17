package com.globaltrade.servlet;

import com.globaltrade.bean.VendorBean;
import com.globaltrade.entity.Vendor;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/vendors")
public class VendorServlet extends HttpServlet {

    @EJB
    private VendorBean bean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("vendors", bean.findAll());
        request.getRequestDispatcher("/vendor/vendors.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        Vendor vendor = new Vendor();

        try {
            vendor.setVendorCode(request.getParameter("vendorCode"));
            vendor.setVendorName(request.getParameter("vendorName"));
            vendor.setContactPerson(request.getParameter("contactPerson"));
            vendor.setEmail(request.getParameter("email"));
            vendor.setPhone(request.getParameter("phone"));
            vendor.setCountry(request.getParameter("country"));

            bean.create(vendor);
            response.sendRedirect(request.getContextPath() + "/vendors");
        } catch (Exception e) {
            request.setAttribute("formError", e.getMessage());
            request.getRequestDispatcher("/vendor/add-vendor.jsp").forward(request, response);
        }
    }
}
