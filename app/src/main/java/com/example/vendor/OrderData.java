package com.example.vendor;

public class OrderData {

    public String orderTime, orderNumber, orderDate , hotelName, memberType, total_order_amount;

    public OrderData(){}

    public OrderData(String orderNumber,String orderTime, String orderDate, String hotelName, String memberType, String total_order_amount) {
        this.orderTime = orderTime;
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.hotelName = hotelName;
        this.total_order_amount = total_order_amount;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getTotal_order_amount() {
        return total_order_amount;
    }

    public void setTotal_order_amount(String total_order_amount) {
        this.total_order_amount = total_order_amount;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelNames) {
        hotelName = hotelNames;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
}
