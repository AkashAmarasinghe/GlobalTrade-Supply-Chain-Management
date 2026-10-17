package com.globaltrade.timer;
import com.globaltrade.bean.AlertBean;
import com.globaltrade.entity.CustomsDocument;
import jakarta.annotation.Resource;
import jakarta.ejb.*;
import jakarta.persistence.*;
import java.time.*;
import java.util.Date;
@Singleton @Startup public class CustomsDeadlineTimer {
    @Resource private TimerService timerService;
    @PersistenceContext(unitName="GlobalTradePU") private EntityManager em;
    @EJB private AlertBean alertBean;
    public void scheduleReminder(Long documentId, LocalDateTime dueDate) {
        if(documentId==null||dueDate==null) return;
        LocalDateTime reminder=dueDate.minusHours(24);
        if(reminder.isBefore(LocalDateTime.now())) reminder=LocalDateTime.now().plusSeconds(5);
        Date when=Date.from(reminder.atZone(ZoneId.systemDefault()).toInstant());
        timerService.createSingleActionTimer(when,new TimerConfig(documentId,true));
    }
    @Timeout public void onDeadline(Timer timer) {
        Object info=timer.getInfo();
        if(!(info instanceof Long id))return;
        CustomsDocument d=em.find(CustomsDocument.class,id);
        if(d!=null && !"APPROVED".equals(d.getStatus())) alertBean.createIfAbsent("HIGH","CUSTOMS","Customs deadline approaching",d.getDocumentType()+" for "+d.getShipment().getShipmentNumber()+" is due soon.","CustomsDocument",d.getId());
    }
}
