package com.example.vendor;

public class AdminHotelOrder {

    String orderDate, orderNumber, orderStatus, orderTime, memberType, HotelName, Name, phone, total_order_amount;


    public AdminHotelOrder(){}

    public AdminHotelOrder(String orderDate,  String orderNumber, String orderStatus, String orderTime, String memberType, String HotelName, String Name, String phone , String total_order_amount) {
        this.orderDate = orderDate;
        this.orderNumber = orderNumber;
        this.orderStatus = orderStatus;
        this.orderTime = orderTime;
        this.memberType = memberType;
        this.HotelName = HotelName;
        this.phone = phone;
        this.Name = Name;
        this.total_order_amount = total_order_amount;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTotal_order_amount() {
        return total_order_amount;
    }

    public void setTotal_order_amount(String total_order_amount) {
        this.total_order_amount = total_order_amount;
    }

    public String getHotelName() {
        return HotelName;
    }

    public void setHotelName(String hotelName) {
        HotelName = hotelName;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof AdminHotelOrder){
            AdminHotelOrder temp = (AdminHotelOrder) obj;
            return this.HotelName.equals(temp.HotelName);

        }

        return false;
    }

    @Override
    public int hashCode() {
        return (this.HotelName.hashCode());
    }
}
