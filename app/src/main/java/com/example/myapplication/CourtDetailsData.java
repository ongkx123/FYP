package com.example.myapplication;

public class CourtDetailsData {

    String Name,Status;

    public CourtDetailsData(){

    }

    public CourtDetailsData(String name, String status) {
        Name = name;
        Status = status;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
