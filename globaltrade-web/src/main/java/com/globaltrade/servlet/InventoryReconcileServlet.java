package com.globaltrade.servlet;

import com.globaltrade.bean.InventoryBean;
import com.globaltrade.bean.InventoryReconciliationBean;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/inventory/reconcile")
public class InventoryReconcileServlet extends HttpServlet {

    @EJB
    private InventoryReconciliationBean reconciliationBean;

    @EJB
    private InventoryBean inventoryBean;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        try {
            Long itemId = Long.valueOf(req.getParameter("itemId"));
            int physicalCount = Integer.parseInt(req.getParameter("physicalCount"));
            reconciliationBean.reconcile(itemId, physicalCount);
            resp.sendRedirect(req.getContextPath() + "/inventory?reconciled=1");
        } catch (Exception e) {
            req.setAttribute("inventoryError", rootMessage(e));
            req.setAttribute("items", inventoryBean.findAll());
            req.getRequestDispatcher("/inventory/inventory.jsp").forward(req, resp);
        }
    }

    private String rootMessage(Throwable error) {
        Throwable current = error;
        while (current.getCause() != null && current.getCause() != current) {
            current = current.getCause();
        }
        return current.getMessage() == null ? error.getMessage() : current.getMessage();
    }
}
