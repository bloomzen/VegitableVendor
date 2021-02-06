package com.example.vendor;

public class orderItemData {

    public String itemName, itemQuantity,itemFinalPrice, itemUnit, itemGms;

    public orderItemData(){}

    public orderItemData(String itemName, String itemQuantity, String itemFinalPrice , String itemUnit, String itemGms) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemFinalPrice = itemFinalPrice;
        this.itemUnit = itemUnit;
        this.itemGms = itemGms;
    }

    public orderItemData(String itemName, String itemQuantity,  String itemUnit, String itemGms) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;

        this.itemUnit = itemUnit;
        this.itemGms = itemGms;
    }

    public String getItemGms() {
        return itemGms;
    }

    public void setItemGms(String itemGms) {
        this.itemGms = itemGms;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(String itemUnit) {
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

    public String getItemFinalPrice() {
        return itemFinalPrice;
    }

    public void setItemFinalPrice(String itemFinalPrice) {
        this.itemFinalPrice = itemFinalPrice;
    }
}
