package com.example.vendor;

public class HotelList {

    //to get hotel list

    private String hotelName;


    public  HotelList(){}

    public HotelList(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }
}
