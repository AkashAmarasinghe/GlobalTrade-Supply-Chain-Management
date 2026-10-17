package com.globaltrade.bean;

import com.globaltrade.entity.CustomsDocument;
import com.globaltrade.entity.Shipment;
import com.globaltrade.exception.CustomsComplianceException;
import com.globaltrade.interceptor.AuditInterceptor;
import com.globaltrade.interceptor.PerformanceInterceptor;
import com.globaltrade.interceptor.TradeComplianceInterceptor;
import com.globaltrade.timer.CustomsDeadlineTimer;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
@Interceptors({
        PerformanceInterceptor.class,
        AuditInterceptor.class,
        TradeComplianceInterceptor.class
})
public class CustomsBean {

    @PersistenceContext(unitName = "GlobalTradePU")
    private EntityManager em;

    @EJB
    private CustomsDeadlineTimer deadlineTimer;

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<CustomsDocument> findAll() {
        return em.createQuery(
                "select c from CustomsDocument c join fetch c.shipment order by c.dueDate asc",
                CustomsDocument.class
        ).getResultList();
    }

    public CustomsDocument create(Long shipmentId, CustomsDocument document) {
        Shipment shipment = em.find(Shipment.class, shipmentId);
        if (shipment == null) {
            throw new CustomsComplianceException("Cannot create customs document: shipment not found");
        }
        document.setShipment(shipment);
        em.persist(document);
        em.flush();
        if (document.getDueDate() != null) {
            deadlineTimer.scheduleReminder(document.getId(), document.getDueDate());
        }
        return document;
    }

    public void approve(Long id) {
        CustomsDocument document = em.find(CustomsDocument.class, id);
        if (document == null) {
            throw new CustomsComplianceException("Customs document not found");
        }
        document.setStatus("APPROVED");
        document.setApprovedAt(LocalDateTime.now());
    }
}
