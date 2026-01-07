package com.chethana.restaurant_management_system.model;

import java.sql.Timestamp;

public class InventoryModel {

    private int inventory_Id;
    private String item_name;
    private int quantity;
    private String unit;
    private Timestamp last_updated;

    public InventoryModel(int inventory_Id, String item_name, int quantity, String unit, Timestamp last_updated) {
        this.inventory_Id = inventory_Id;
        this.item_name = item_name;
        this.quantity = quantity;
        this.unit = unit;
        this.last_updated = last_updated;
    }

    public InventoryModel() {

    }

    public int getInventoryId() {
        return inventory_Id;
    }

    public void setInventoryId(int inventory_Id) {
        this.inventory_Id = inventory_Id;
    }

    public String getItemName() {
        return item_name;
    }

    public void setItemName(String item_name) {
        this.item_name = item_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Timestamp getLastUpdated() {
        return last_updated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.last_updated = lastUpdated;
    }
}
