package com.globaltrade.timer;

import com.globaltrade.bean.AlertBean;
import com.globaltrade.bean.RouteOptimizationBean;
import com.globaltrade.dto.RouteOption;
import com.globaltrade.entity.Shipment;
import jakarta.ejb.EJB;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Singleton
@Startup
public class RouteOptimizationTimer {

    @PersistenceContext(unitName = "GlobalTradePU")
    private EntityManager em;

    @EJB
    private RouteOptimizationBean routeOptimizationBean;

    @EJB
    private AlertBean alertBean;

    @Schedule(hour = "*/6", minute = "5", second = "0", persistent = true)
    public void calculateRouteWindow() {
        List<Shipment> activeShipments = em.createQuery(
                        "select s from Shipment s where s.status in ('CREATED','BOOKED','IN_TRANSIT','DELAYED') " +
                                "order by s.createdAt desc",
                        Shipment.class)
                .setMaxResults(25)
                .getResultList();

        for (Shipment shipment : activeShipments) {
            List<RouteOption> options = routeOptimizationBean.optimize(
                    shipment.getOriginCountry(),
                    shipment.getDestinationCountry(),
                    "Air Freight",
                    "Balanced");

            if (!options.isEmpty()) {
                RouteOption best = options.get(0);
                alertBean.createIfAbsent(
                        "INFO",
                        "ROUTE",
                        "Route review: " + shipment.getShipmentNumber(),
                        best.getName() + " recommended; " + best.getPath() +
                                "; transit " + best.getTransit() + "; risk " + best.getRisk() + ".",
                        "Shipment",
                        shipment.getId());
            }
        }
    }
}
