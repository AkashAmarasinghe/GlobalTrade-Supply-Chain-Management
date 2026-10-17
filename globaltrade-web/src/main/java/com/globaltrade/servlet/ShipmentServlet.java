package com.globaltrade.servlet;
import com.globaltrade.bean.ShipmentBean;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
@WebServlet("/shipments") public class ShipmentServlet extends HttpServlet {
    @EJB private ShipmentBean bean;
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException {
        req.setAttribute("shipments",bean.findAll());
        req.getRequestDispatcher("/shipment/shipments.jsp").forward(req,resp);
    }
}
