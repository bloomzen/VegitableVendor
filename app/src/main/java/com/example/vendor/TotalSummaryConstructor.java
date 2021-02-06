package com.example.vendor;

import com.example.vendor.AdminScreen.TotalSummaryFragment;

public class TotalSummaryConstructor {
    String itemName, itemQuantity, itemUnit ;

    public TotalSummaryConstructor(){}

    public TotalSummaryConstructor(String itemName, String itemQuantity, String itemUnit) {
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
