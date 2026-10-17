package com.globaltrade.servlet;
import com.globaltrade.bean.VendorBean;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
@WebServlet("/inventory/new") public class AddInventoryServlet extends HttpServlet {
    @EJB private VendorBean vendorBean;
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException {
        req.setAttribute("vendors",vendorBean.findAll());
        req.getRequestDispatcher("/inventory/add-inventory.jsp").forward(req,resp);
    }
}
