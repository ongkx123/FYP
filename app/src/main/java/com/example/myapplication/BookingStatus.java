package com.example.myapplication;

public class BookingStatus {

    String court;
    String name;
    String status;

    public BookingStatus(){

    }

    public BookingStatus(String court,String name, String status) {
        this.court=court;
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = court;
    }
}
