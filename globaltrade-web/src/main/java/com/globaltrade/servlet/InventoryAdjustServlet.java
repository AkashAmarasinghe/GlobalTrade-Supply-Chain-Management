package com.globaltrade.servlet;

import com.globaltrade.bean.InventoryBean;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/inventory/replenish")
public class InventoryAdjustServlet extends HttpServlet {

    @EJB
    private InventoryBean inventoryBean;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        try {
            Long itemId = Long.valueOf(req.getParameter("itemId"));
            int quantity = Integer.parseInt(req.getParameter("quantity"));
            inventoryBean.replenishStock(itemId, quantity);
            resp.sendRedirect(req.getContextPath() + "/inventory?updated=1");
        } catch (Exception e) {
            req.setAttribute("inventoryError", e.getMessage());
            req.setAttribute("items", inventoryBean.findAll());
            req.getRequestDispatcher("/inventory/inventory.jsp").forward(req, resp);
        }
    }
}
