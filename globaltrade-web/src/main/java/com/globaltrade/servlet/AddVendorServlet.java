package com.globaltrade.servlet;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
@WebServlet("/vendors/new") public class AddVendorServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException {
        req.getRequestDispatcher("/vendor/add-vendor.jsp").forward(req,resp);
    }
}
