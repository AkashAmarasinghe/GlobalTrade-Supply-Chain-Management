package com.globaltrade.timer;
import com.globaltrade.bean.*;
import com.globaltrade.entity.Shipment;
import com.globaltrade.exception.CarrierIntegrationException;
import com.globaltrade.remote.CarrierGatewayRemote;
import jakarta.ejb.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
@Singleton @Startup public class ShipmentTrackingTimer {
    @PersistenceContext(unitName="GlobalTradePU") private EntityManager em;
    @EJB private CarrierGatewayRemote carrier;
    @EJB private AlertBean alertBean;
    @Schedule(minute="*/15", hour="*", persistent=true) @TransactionAttribute(TransactionAttributeType.REQUIRED) public void pollShipmentStatus() {
        List<Shipment> list=em.createQuery("select s from Shipment s where s.status in ('CREATED','BOOKED','IN_TRANSIT','DELAYED')",Shipment.class).setMaxResults(100).getResultList();
        for(Shipment s:list) {
            try {
                String remote=carrier.fetchShipmentStatus(s.getTrackingReference());
                if(s.getExpectedArrivalDate()!=null && s.getExpectedArrivalDate().isBefore(LocalDateTime.now()) && !"DELIVERED".equals(s.getStatus())) {
                    if(!"DELAYED".equals(s.getStatus())) {
                        s.setStatus("DELAYED");
                        alertBean.createIfAbsent("HIGH","SHIPMENT","Shipment delay detected",s.getShipmentNumber()+" passed its expected arrival time.","Shipment",s.getId());
                    }
                }
                else if(remote!=null && !"DELAYED".equals(s.getStatus())) s.setStatus(remote);
            }
            catch(CarrierIntegrationException e) {
                alertBean.createIfAbsent("HIGH","CARRIER","Carrier integration failure",e.getMessage(),"Shipment",s.getId());
            }
        }
    }
}
