package com.globaltrade.bean;

import com.globaltrade.entity.AuditLog;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class AuditBean {

    @PersistenceContext(unitName = "GlobalTradePU")
    private EntityManager em;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void log(String actor, String action, String entityType, Long entityId,
                    String details, long elapsed, boolean success) {
        AuditLog audit = new AuditLog();
        audit.setActor(limit(actor == null || actor.isBlank() ? "SYSTEM" : actor, 100));
        audit.setAction(limit(action == null || action.isBlank() ? "Unknown action" : action, 150));
        audit.setEntityType(limit(entityType, 100));
        audit.setEntityId(entityId);
        audit.setDetails(limit(details, 1000));
        audit.setExecutionTimeMs(Math.max(0L, elapsed));
        audit.setSuccess(success);
        em.persist(audit);

        em.flush();
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<AuditLog> findRecent(int max) {
        int safeMax = Math.max(1, Math.min(max, 500));
        return em.createQuery(
                        "select a from AuditLog a " +
                                "where (a.details is null or a.details not like 'Sample audit:%') " +
                                "and lower(a.action) not like '%.find%' " +
                                "and lower(a.action) not like '%.get%' " +
                                "and lower(a.action) not like '%.list%' " +
                                "and lower(a.action) not like '%.count%' " +
                                "and lower(a.action) not like '%.recent%' " +
                                "order by a.createdAt desc",
                        AuditLog.class)
                .setMaxResults(safeMax)
                .getResultList();
    }

    private String limit(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }
}
