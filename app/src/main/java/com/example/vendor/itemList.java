package com.example.vendor;

public class itemList {

    String itemName, itemQuantity, itemUnit;

    public  itemList(){}

    public itemList(String itemName, String itemQuantity, String itemUnit) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemUnit = itemUnit;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }
}
