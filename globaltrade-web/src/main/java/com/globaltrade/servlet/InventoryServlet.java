package com.globaltrade.servlet;

import com.globaltrade.bean.InventoryBean;
import com.globaltrade.bean.VendorBean;
import com.globaltrade.entity.InventoryItem;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/inventory")
public class InventoryServlet extends HttpServlet {

    @EJB
    private InventoryBean inventoryBean;

    @EJB
    private VendorBean vendorBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("items", inventoryBean.findAll());
        req.getRequestDispatcher("/inventory/inventory.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        InventoryItem item = new InventoryItem();

        try {
            item.setSku(req.getParameter("sku"));
            item.setItemName(req.getParameter("itemName"));
            item.setDescription(req.getParameter("description"));
            item.setQuantityOnHand(Integer.parseInt(req.getParameter("quantity")));
            item.setReorderLevel(Integer.parseInt(req.getParameter("reorderLevel")));
            item.setUnit(req.getParameter("unit"));
            Long vendorId = parseLong(req.getParameter("vendorId"));

            inventoryBean.create(item, vendorId);
            resp.sendRedirect(req.getContextPath() + "/inventory");
        } catch (Exception e) {
            req.setAttribute("formError", e.getMessage());
            req.setAttribute("vendors", vendorBean.findAll());
            req.getRequestDispatcher("/inventory/add-inventory.jsp").forward(req, resp);
        }
    }

    private Long parseLong(String value) {
        try {
            return value == null || value.isBlank() ? null : Long.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }
}
