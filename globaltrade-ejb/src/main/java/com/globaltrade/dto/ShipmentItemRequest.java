package com.globaltrade.dto;
import java.io.Serializable;
public class ShipmentItemRequest implements Serializable {
    private Long inventoryItemId;
    private int quantity;
    public ShipmentItemRequest() {
    }
    public ShipmentItemRequest(Long id,int quantity) {
        this.inventoryItemId=id;
        this.quantity=quantity;
    }
    public Long getInventoryItemId() {
        return inventoryItemId;
    }
    public void setInventoryItemId(Long v) {
        inventoryItemId=v;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int v) {
        quantity=v;
    }
}
