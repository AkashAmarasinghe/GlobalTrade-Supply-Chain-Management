package com.globaltrade.bean;

import com.globaltrade.entity.InventoryItem;
import com.globaltrade.entity.Vendor;
import com.globaltrade.exception.InsufficientInventoryException;
import com.globaltrade.interceptor.AuditInterceptor;
import com.globaltrade.interceptor.PerformanceInterceptor;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
@Interceptors({PerformanceInterceptor.class, AuditInterceptor.class})
public class InventoryBean {
    @PersistenceContext(unitName = "GlobalTradePU")
    private EntityManager em;

    @EJB
    private AlertBean alertBean;

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<InventoryItem> findAll() {
        return em.createQuery(
                "select i from InventoryItem i left join fetch i.vendor order by i.itemName",
                InventoryItem.class).getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<InventoryItem> findLowStock() {
        return em.createQuery(
                "select i from InventoryItem i left join fetch i.vendor " +
                "where i.quantityOnHand <= i.reorderLevel order by i.quantityOnHand",
                InventoryItem.class).getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public InventoryItem find(Long id) {
        return id == null ? null : em.find(InventoryItem.class, id);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public InventoryItem create(InventoryItem item, Long vendorId) {
        validateInventoryValues(item.getQuantityOnHand(), item.getReorderLevel());
        if (vendorId != null) {
            item.setVendor(em.getReference(Vendor.class, vendorId));
        }
        em.persist(item);
        em.flush();
        syncLowStockAlert(item);
        return item;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void reserveStock(Long itemId, int qty) {
        InventoryItem item = em.find(InventoryItem.class, itemId, LockModeType.PESSIMISTIC_WRITE);
        if (item == null) {
            throw new IllegalArgumentException("Inventory item not found: " + itemId);
        }
        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (item.getQuantityOnHand() < qty) {
            throw new InsufficientInventoryException(
                    "Insufficient stock for " + item.getSku() + ". Available=" +
                    item.getQuantityOnHand() + ", requested=" + qty);
        }
        item.setQuantityOnHand(item.getQuantityOnHand() - qty);
        syncLowStockAlert(item);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateQuantity(Long itemId, int newQty) {
        if (newQty < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        InventoryItem item = em.find(InventoryItem.class, itemId, LockModeType.PESSIMISTIC_WRITE);
        if (item == null) {
            throw new IllegalArgumentException("Inventory item not found");
        }
        item.setQuantityOnHand(newQty);
        syncLowStockAlert(item);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public InventoryItem replenishStock(Long itemId, int addQty) {
        if (addQty <= 0) {
            throw new IllegalArgumentException("Replenishment quantity must be greater than zero");
        }
        InventoryItem item = em.find(InventoryItem.class, itemId, LockModeType.PESSIMISTIC_WRITE);
        if (item == null) {
            throw new IllegalArgumentException("Inventory item not found");
        }
        item.setQuantityOnHand(item.getQuantityOnHand() + addQty);
        syncLowStockAlert(item);
        return item;
    }

    private void validateInventoryValues(int quantity, int reorderLevel) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        if (reorderLevel < 0) {
            throw new IllegalArgumentException("Reorder level cannot be negative");
        }
    }

    private void syncLowStockAlert(InventoryItem item) {
        if (item.getId() == null) {
            return;
        }
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
