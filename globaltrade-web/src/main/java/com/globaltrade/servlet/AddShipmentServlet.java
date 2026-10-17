package com.globaltrade.servlet;

import com.globaltrade.bean.InventoryBean;
import com.globaltrade.bean.ShipmentBean;
import com.globaltrade.bean.VendorBean;
import com.globaltrade.dto.ShipmentItemRequest;
import com.globaltrade.entity.Shipment;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/shipments/new")
public class AddShipmentServlet extends HttpServlet {

    private static final Logger LOG =
            Logger.getLogger(AddShipmentServlet.class.getName());

    @EJB
    private ShipmentBean shipmentBean;

    @EJB
    private VendorBean vendorBean;

    @EJB
    private InventoryBean inventoryBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        load(req);
        req.getRequestDispatcher("/shipment/add-shipment.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Shipment shipment = new Shipment();

        try {
            shipment.setShipmentNumber(req.getParameter("shipmentNumber"));
            shipment.setCarrierName(req.getParameter("carrierName"));
            shipment.setTrackingReference(req.getParameter("trackingReference"));
            shipment.setOriginCountry(req.getParameter("originCountry"));
            shipment.setDestinationCountry(req.getParameter("destinationCountry"));
            shipment.setStatus(req.getParameter("status"));
            shipment.setDepartureDate(parseDate(req.getParameter("departureDate")));
            shipment.setExpectedArrivalDate(parseDate(req.getParameter("expectedArrivalDate")));

            Long vendorId = parseLong(req.getParameter("vendorId"));
            Long userId = (Long) req.getSession().getAttribute("userId");

            String[] ids = req.getParameterValues("itemId");
            String[] quantities = req.getParameterValues("quantity");

            List<ShipmentItemRequest> items = new ArrayList<>();

            if (ids != null && quantities != null) {
                for (int n = 0; n < Math.min(ids.length, quantities.length); n++) {

                    Long id = parseLong(ids[n]);
                    int quantity = parseInt(quantities[n]);

                    if (id != null && quantity > 0) {
                        items.add(new ShipmentItemRequest(id, quantity));
                    }
                }
            }

            shipmentBean.create(shipment, vendorId, userId, items);

            resp.sendRedirect(req.getContextPath() + "/shipments");

        } catch (Exception e) {

            Throwable actualException = getRootCause(e);

            LOG.log(
                    Level.WARNING,
                    "Handled "
                            + actualException.getClass().getSimpleName()
                            + ": "
                            + actualException.getMessage()
            );

            req.setAttribute("formError", actualException.getMessage());

            load(req);

            req.getRequestDispatcher("/shipment/add-shipment.jsp")
                    .forward(req, resp);
        }
    }

    private void load(HttpServletRequest req) {
        req.setAttribute("vendors", vendorBean.findAll());
        req.setAttribute("inventory", inventoryBean.findAll());
    }

    private Long parseLong(String value) {
        try {
            return value == null || value.isBlank()
                    ? null
                    : Long.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    private LocalDateTime parseDate(String value) {
        try {
            return value == null || value.isBlank()
                    ? null
                    : LocalDateTime.parse(value);
        } catch (Exception e) {
            return null;
        }
    }

    private Throwable getRootCause(Throwable throwable) {

        Throwable result = throwable;

        while (result.getCause() != null
                && result.getCause() != result) {
            result = result.getCause();
        }

        return result;
    }
}