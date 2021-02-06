package com.example.vendor;



public class CartData {

    //for cart
    private String Item_Name, Item_Price, Item_Final_Cost, Item_Quantity, Item_Unit, Item_Image, Item_Gms;


    public CartData(){}

    public CartData(String item_Name, String item_Quantity, String item_Unit , String item_Gms){
        Item_Name = item_Name;
        Item_Quantity = item_Quantity;
        Item_Unit = item_Unit;
        Item_Gms = item_Gms;
    }
    public CartData(String item_Name, String item_Price, String item_Final_Cost, String item_Quantity, String item_Unit, String item_Image, String itemGms) {
        Item_Name = item_Name;
        Item_Price = item_Price;
        Item_Final_Cost = item_Final_Cost;
        Item_Quantity = item_Quantity;
        Item_Unit = item_Unit;
        Item_Image = item_Image;
        Item_Gms = itemGms;
    }


    public String getItem_Gms() {
        return Item_Gms;
    }

    public void setItem_Gms(String item_Gms) {
        Item_Gms = item_Gms;
    }

    public String getItem_Name() {
        return Item_Name;
    }

    public void setItem_Name(String item_Name) {
        Item_Name = item_Name;
    }

    public String getItem_Price() {
        return Item_Price;
    }

    public void setItem_Price(String item_Price) {
        Item_Price = item_Price;
    }

    public String getItem_Final_Cost() {
        return Item_Final_Cost;
    }

    public void setItem_Final_Cost(String item_Final_Cost) {
        Item_Final_Cost = item_Final_Cost;
    }

    public String getItem_Quantity() {
        return Item_Quantity;
    }

    public void setItem_Quantity(String item_Quantity) {
        Item_Quantity = item_Quantity;
    }

    public String getItem_Unit() {
        return Item_Unit;
    }

    public void setItem_Unit(String item_Unit) {
        Item_Unit = item_Unit;
    }

    public String getItem_Image() {
        return Item_Image;
    }

    public void setItem_Image(String item_Image) {
        Item_Image = item_Image;
    }
}
