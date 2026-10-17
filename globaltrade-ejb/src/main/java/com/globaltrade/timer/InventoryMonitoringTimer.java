package com.globaltrade.timer;
import com.globaltrade.bean.*;
import com.globaltrade.entity.InventoryItem;
import jakarta.ejb.*;
import java.util.List;
@Singleton @Startup public class InventoryMonitoringTimer {
    @EJB private InventoryBean inventoryBean;
    @EJB private AlertBean alertBean;
    @Schedule(minute="*/30", hour="*", persistent=true) public void checkLowStock() {
        List<InventoryItem> low=inventoryBean.findLowStock();
        for(InventoryItem i:low) {
            alertBean.createIfAbsent(i.getQuantityOnHand()<=Math.max(1,i.getReorderLevel()/2)?"HIGH":"MEDIUM","INVENTORY","Low stock: "+i.getSku(),"Quantity "+i.getQuantityOnHand()+" is at/below reorder level "+i.getReorderLevel(),"InventoryItem",i.getId());
        }
    }
}
