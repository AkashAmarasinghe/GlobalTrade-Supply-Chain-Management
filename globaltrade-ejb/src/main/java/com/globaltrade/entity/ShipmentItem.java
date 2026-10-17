package com.globaltrade.entity;
import jakarta.persistence.*;
import java.io.Serializable;
@Entity @Table(name="shipment_items") public class ShipmentItem implements Serializable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.EAGER, optional=false) @JoinColumn(name="shipment_id", nullable=false) private Shipment shipment;
    @ManyToOne(fetch=FetchType.EAGER, optional=false) @JoinColumn(name="inventory_item_id", nullable=false) private InventoryItem inventoryItem;
    @Column(nullable=false) private int quantity;
    public Long getId() {
        return id;
    }
    public void setId(Long v) {
        id=v;
    }
    public Shipment getShipment() {
        return shipment;
    }
    public void setShipment(Shipment v) {
        shipment=v;
    }
    public InventoryItem getInventoryItem() {
        return inventoryItem;
    }
    public void setInventoryItem(InventoryItem v) {
        inventoryItem=v;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int v) {
        quantity=v;
    }
}
