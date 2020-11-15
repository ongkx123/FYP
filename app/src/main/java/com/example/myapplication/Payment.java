package com.example.myapplication;

public class Payment {

    String date;
    String endTime;
    String id;
    String location;
    String name;
    String noCourt;
    String paymentStatus;
    String startTime;
    String totalPrice;
    String userID;

    public Payment(){

    }

    public Payment(String date, String endTime, String id, String location, String name, String noCourt, String paymentStatus, String startTime, String totalPrice, String userID) {
        this.date = date;
        this.endTime = endTime;
        this.id = id;
        this.location = location;
        this.name = name;
        this.noCourt = noCourt;
        this.paymentStatus = paymentStatus;
        this.startTime = startTime;
        this.totalPrice = totalPrice;
        this.userID = userID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNoCourt() {
        return noCourt;
    }

    public void setNoCourt(String noCourt) {
        this.noCourt = noCourt;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
