package com.globaltrade.bean;
import com.globaltrade.dto.DashboardStats;
import com.globaltrade.entity.*;
import jakarta.ejb.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
@Stateless public class DashboardBean {
    @PersistenceContext(unitName="GlobalTradePU") private EntityManager em;
    @TransactionAttribute(TransactionAttributeType.SUPPORTS) public DashboardStats stats() {
        long active=em.createQuery("select count(s) from Shipment s where s.status not in ('DELIVERED','CANCELLED')",Long.class).getSingleResult();
        long low=em.createQuery("select count(i) from InventoryItem i where i.quantityOnHand <= i.reorderLevel",Long.class).getSingleResult();
        LocalDateTime limit=LocalDateTime.now().plusHours(48);
        long customs=em.createQuery("select count(c) from CustomsDocument c where c.status<>'APPROVED' and c.dueDate between :now and :lim",Long.class).setParameter("now",LocalDateTime.now()).setParameter("lim",limit).getSingleResult();
        Double avg=em.createQuery("select avg(v.performanceScore) from Vendor v where v.status='ACTIVE'",Double.class).getSingleResult();
        long alerts=em.createQuery("select count(a) from SystemAlert a where a.resolved=false",Long.class).getSingleResult();
        return new DashboardStats(active,low,customs,avg==null?0:avg,alerts);
    }
    public List<Shipment> priorityShipments() {
        return em.createQuery("select s from Shipment s left join fetch s.vendor where s.status in ('DELAYED','CUSTOMS_HOLD','IN_TRANSIT') order by s.expectedArrivalDate",Shipment.class).setMaxResults(8).getResultList();
    }
}
