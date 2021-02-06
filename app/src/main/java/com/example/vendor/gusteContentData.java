package com.example.vendor;

public class gusteContentData {
    private String itemName, itemImage, itemQuantity, itemUnit, itemGuestPrice, itemPrice;

    public gusteContentData(){}

    public gusteContentData(String itemName, String itemImage, String itemQuantity, String itemUnit, String itemGuestPrice, String itemPrice) {
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.itemQuantity = itemQuantity;
        this.itemUnit = itemUnit;
        this.itemGuestPrice = itemGuestPrice;

        this.itemPrice = itemPrice;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
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

    public String getItemGuestPrice() {
        return itemGuestPrice;
    }

    public void setItemGuestPrice(String itemGuestPrice) {
        this.itemGuestPrice = itemGuestPrice;
    }
}
