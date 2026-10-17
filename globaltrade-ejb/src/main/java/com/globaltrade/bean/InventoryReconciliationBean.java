package com.globaltrade.bean;

import com.globaltrade.entity.InventoryItem;
import com.globaltrade.interceptor.AuditInterceptor;
import com.globaltrade.interceptor.PerformanceInterceptor;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Status;
import jakarta.transaction.UserTransaction;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@Interceptors({PerformanceInterceptor.class, AuditInterceptor.class})
public class InventoryReconciliationBean {
    @PersistenceContext(unitName = "GlobalTradePU")
    private EntityManager em;

    @Resource
    private UserTransaction userTransaction;

    @EJB
    private AlertBean alertBean;

    public void reconcile(Long itemId, int physicalCount) {
        try {
            userTransaction.begin();
            InventoryItem item = em.find(InventoryItem.class, itemId, LockModeType.PESSIMISTIC_WRITE);
            if (item == null) {
                throw new IllegalArgumentException("Inventory item not found");
            }
            if (physicalCount < 0) {
                throw new IllegalArgumentException("Physical count cannot be negative");
            }

            item.setQuantityOnHand(physicalCount);
            syncLowStockAlert(item);
            userTransaction.commit();
        } catch (Exception e) {
            try {
                if (userTransaction.getStatus() == Status.STATUS_ACTIVE) {
                    userTransaction.rollback();
                }
            } catch (Exception ignored) {
            }
            throw new EJBException("BMT reconciliation failed", e);
        }
    }

    private void syncLowStockAlert(InventoryItem item) {
        if (item.getQuantityOnHand() <= item.getReorderLevel()) {
            String severity = item.getQuantityOnHand() <= Math.max(1, item.getReorderLevel() / 2)
                    ? "HIGH" : "MEDIUM";
            alertBean.createIfAbsent(
                    severity,
                    "INVENTORY",
                    "Low stock: " + item.getSku(),
                    item.getItemName() + " has " + item.getQuantityOnHand() + " " + item.getUnit() +
                            " remaining; reorder level is " + item.getReorderLevel() + ".",
                    "InventoryItem",
                    item.getId());
        } else {
            alertBean.resolveForReference("INVENTORY", "InventoryItem", item.getId());
        }
    }
}
