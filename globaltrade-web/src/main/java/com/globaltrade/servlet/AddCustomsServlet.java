package com.globaltrade.servlet;
import com.globaltrade.bean.ShipmentBean;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
@WebServlet("/customs/new") public class AddCustomsServlet extends HttpServlet {
    @EJB private ShipmentBean shipmentBean;
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException {
        req.setAttribute("shipments",shipmentBean.findAll());
        req.getRequestDispatcher("/customs/add-customs.jsp").forward(req,resp);
    }
}
