package com.globaltrade.servlet;
import com.globaltrade.bean.AuditBean;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
@WebServlet("/audit") public class AuditServlet extends HttpServlet {
    @EJB private AuditBean bean;
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException {
        req.setAttribute("logs",bean.findRecent(200));
        req.getRequestDispatcher("/audit/audit-logs.jsp").forward(req,resp);
    }
}
