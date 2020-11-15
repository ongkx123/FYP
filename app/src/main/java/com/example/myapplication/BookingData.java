package com.example.myapplication;

public class BookingData {

    String name,date,startTime,endTime,courtNo;

    public BookingData(){

    }

    public BookingData(String name, String date, String startTime, String endTime, String courtNo) {
        this.name = name;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.courtNo = courtNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCourtNo() {
        return courtNo;
    }

    public void setCourtNo(String courtNo) {
        this.courtNo = courtNo;
    }
}
