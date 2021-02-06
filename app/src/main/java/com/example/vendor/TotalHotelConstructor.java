package com.example.vendor;

import androidx.annotation.Nullable;

public class TotalHotelConstructor {
    String itemName, hotelName,itemQuantity,itemUnit, memberShip,orderDate;

    public TotalHotelConstructor(){}




    public TotalHotelConstructor(String itemName, String hotelName, String itemQuantity, String itemUnit, String memberShip, String orderDate) {
        this.itemName = itemName;
        this.hotelName = hotelName;
        this.itemQuantity = itemQuantity;
        this.itemUnit = itemUnit;
        this.memberShip = memberShip;
        this.orderDate = orderDate;

    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
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

    public String getMemberShip() {
        return memberShip;
    }

    public void setMemberShip(String memberShip) {
        this.memberShip = memberShip;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }






}
