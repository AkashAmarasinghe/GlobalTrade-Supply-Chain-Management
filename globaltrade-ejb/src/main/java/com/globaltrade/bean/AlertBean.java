package com.globaltrade.bean;

import com.globaltrade.entity.SystemAlert;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class AlertBean {
    @PersistenceContext(unitName = "GlobalTradePU")
    private EntityManager em;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public SystemAlert create(String severity, String category, String title, String message,
                              String refType, Long refId) {
        SystemAlert alert = new SystemAlert();
        alert.setSeverity(severity);
        alert.setCategory(category);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setReferenceType(refType);
        alert.setReferenceId(refId);
        em.persist(alert);
        return alert;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public SystemAlert createIfAbsent(String severity, String category, String title, String message,
                                      String refType, Long refId) {
        StringBuilder jpql = new StringBuilder(
                "select a from SystemAlert a where a.resolved=false and a.category=:category and a.title=:title");
        if (refType == null) {
            jpql.append(" and a.referenceType is null");
        } else {
            jpql.append(" and a.referenceType=:refType");
        }
        if (refId == null) {
            jpql.append(" and a.referenceId is null");
        } else {
            jpql.append(" and a.referenceId=:refId");
        }
        jpql.append(" order by a.createdAt desc");

        var query = em.createQuery(jpql.toString(), SystemAlert.class)
                .setParameter("category", category)
                .setParameter("title", title)
                .setMaxResults(1);
        if (refType != null) {
            query.setParameter("refType", refType);
        }
        if (refId != null) {
            query.setParameter("refId", refId);
        }

        List<SystemAlert> existing = query.getResultList();
        if (existing.isEmpty()) {
            return create(severity, category, title, message, refType, refId);
        }

        SystemAlert alert = existing.get(0);
        alert.setSeverity(severity);
        alert.setMessage(message);
        return alert;
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<SystemAlert> openAlerts(int max) {
        int safeMax = Math.max(1, Math.min(max, 100));
        return em.createQuery(
                        "select a from SystemAlert a where a.resolved=false order by a.createdAt desc",
                        SystemAlert.class)
                .setMaxResults(safeMax)
                .getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void resolve(Long id) {
        SystemAlert alert = em.find(SystemAlert.class, id);
        if (alert != null && !alert.isResolved()) {
            alert.setResolved(true);
            alert.setResolvedAt(LocalDateTime.now());
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public int resolveForReference(String category, String refType, Long refId) {
        if (refId == null) {
            return 0;
        }
        List<SystemAlert> alerts = em.createQuery(
                        "select a from SystemAlert a where a.resolved=false and a.category=:category " +
                        "and a.referenceType=:refType and a.referenceId=:refId",
                        SystemAlert.class)
                .setParameter("category", category)
                .setParameter("refType", refType)
                .setParameter("refId", refId)
                .getResultList();
        LocalDateTime now = LocalDateTime.now();
        for (SystemAlert alert : alerts) {
            alert.setResolved(true);
            alert.setResolvedAt(now);
        }
        return alerts.size();
    }
}
