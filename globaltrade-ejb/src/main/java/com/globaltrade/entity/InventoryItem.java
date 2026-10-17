package com.globaltrade.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="inventory_items")
public class InventoryItem implements Serializable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, unique=true, length=50) private String sku;
    @Column(name="item_name", nullable=false, length=120) private String itemName;
    @Column(length=255) private String description;
    @Column(name="quantity_on_hand", nullable=false) private int quantityOnHand;
    @Column(name="reorder_level", nullable=false) private int reorderLevel=10;
    @Column(length=30) private String unit="units";
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="vendor_id") private Vendor vendor;
    @Column(name="last_updated", nullable=false) private LocalDateTime lastUpdated;
    @PrePersist @PreUpdate public void touch(){ lastUpdated=LocalDateTime.now(); }
    public Long getId(){return id;} public void setId(Long id){this.id=id;
    }
    public String getSku(){return sku;} public void setSku(String v){sku=v;}
    public String getItemName(){return itemName;} public void setItemName(String v){itemName=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public int getQuantityOnHand(){return quantityOnHand;} public void setQuantityOnHand(int v){quantityOnHand=v;}
    public int getReorderLevel(){return reorderLevel;} public void setReorderLevel(int v){reorderLevel=v;}
    public String getUnit(){return unit;} public void setUnit(String v){unit=v;}
    public Vendor getVendor(){return vendor;} public void setVendor(Vendor v){vendor=v;}
    public LocalDateTime getLastUpdated(){return lastUpdated;}
}
