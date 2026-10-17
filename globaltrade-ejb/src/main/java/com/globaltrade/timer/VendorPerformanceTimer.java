package com.globaltrade.timer;
import com.globaltrade.bean.AlertBean;
import com.globaltrade.entity.Vendor;
import jakarta.ejb.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
@Singleton @Startup public class VendorPerformanceTimer {
    @PersistenceContext(unitName="GlobalTradePU") private EntityManager em;
    @EJB private AlertBean alertBean;
    @Schedule(hour="2", minute="0", second="0", persistent=true) public void evaluateThresholds() {
        List<Vendor> list=em.createQuery("select v from Vendor v where v.status='ACTIVE' and v.performanceScore < :min",Vendor.class).setParameter("min",BigDecimal.valueOf(70)).getResultList();
        for(Vendor v:list) alertBean.createIfAbsent("MEDIUM","VENDOR","Vendor performance review required",v.getVendorName()+" score is "+v.getPerformanceScore(),"Vendor",v.getId());
    }
}
