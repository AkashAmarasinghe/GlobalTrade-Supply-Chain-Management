package com.globaltrade.bean;
import com.globaltrade.dto.ShipmentItemRequest;
import com.globaltrade.entity.*;
import com.globaltrade.interceptor.*;
import jakarta.ejb.*;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.*;
import java.util.List;
@Stateless @TransactionManagement(TransactionManagementType.CONTAINER) @Interceptors( {
    PerformanceInterceptor.class,
    AuditInterceptor.class
}
) public class ShipmentBean {
    @PersistenceContext(unitName="GlobalTradePU") private EntityManager em;
    @EJB private InventoryBean inventoryBean;
    @TransactionAttribute(TransactionAttributeType.SUPPORTS) public List<Shipment> findAll() {
        return em.createQuery("select s from Shipment s left join fetch s.vendor order by s.createdAt desc",Shipment.class).getResultList();
    }
    @TransactionAttribute(TransactionAttributeType.SUPPORTS) public Shipment find(Long id) {
        return em.find(Shipment.class,id);
    }
    @TransactionAttribute(TransactionAttributeType.REQUIRED) public Shipment create(Shipment s,Long vendorId,Long createdByUserId,List<ShipmentItemRequest> items) {
        if(vendorId!=null)s.setVendor(em.getReference(Vendor.class,vendorId));
        if(createdByUserId!=null)s.setCreatedBy(em.getReference(User.class,createdByUserId));
        em.persist(s);
        em.flush();
        if(items!=null) {
            for(ShipmentItemRequest req:items) {
                if(req==null||req.getInventoryItemId()==null||req.getQuantity()<=0) continue;
                inventoryBean.reserveStock(req.getInventoryItemId(),req.getQuantity());
                ShipmentItem si=new ShipmentItem();
                si.setShipment(s);
                si.setInventoryItem(em.getReference(InventoryItem.class,req.getInventoryItemId()));
                si.setQuantity(req.getQuantity());
                em.persist(si);
            }
        }
        return s;
    }
    public void updateStatus(Long id,String status) {
        Shipment s=em.find(Shipment.class,id,LockModeType.PESSIMISTIC_WRITE);
        if(s==null)throw new IllegalArgumentException("Shipment not found");
        s.setStatus(status);
    }
}
