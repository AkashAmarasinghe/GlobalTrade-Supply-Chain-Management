package com.globaltrade.servlet;

import com.globaltrade.bean.CustomsBean;
import com.globaltrade.bean.ShipmentBean;
import com.globaltrade.entity.CustomsDocument;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/customs")
public class CustomsServlet extends HttpServlet {

    @EJB
    private CustomsBean customsBean;

    @EJB
    private ShipmentBean shipmentBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("documents", customsBean.findAll());
        req.getRequestDispatcher("/customs/customs.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        CustomsDocument document = new CustomsDocument();

        try {
            document.setDocumentType(req.getParameter("documentType"));
            document.setDocumentNumber(req.getParameter("documentNumber"));
            document.setDueDate(parseDate(req.getParameter("dueDate")));
            document.setNotes(req.getParameter("notes"));

            customsBean.create(Long.valueOf(req.getParameter("shipmentId")), document);
            resp.sendRedirect(req.getContextPath() + "/customs");
        } catch (Exception e) {
            req.setAttribute("formError", e.getMessage());
            req.setAttribute("shipments", shipmentBean.findAll());
            req.getRequestDispatcher("/customs/add-customs.jsp").forward(req, resp);
        }
    }

    private LocalDateTime parseDate(String value) {
        try {
            return value == null || value.isBlank() ? null : LocalDateTime.parse(value);
        } catch (Exception e) {
            return null;
        }
    }
}
