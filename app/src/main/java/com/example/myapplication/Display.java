package com.example.myapplication;

public class Display {

    String address;
    String phoneNo;
    String url;

    public Display(){

    }

    public Display(String address, String phoneNo, String url) {
        this.address = address;
        this.phoneNo = phoneNo;
        this.url = url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
