package com.example.myapplication;

public class SportCentre {
    String id;
    String latitude;
    String location;
    String longitude;
    String name;

    public SportCentre(){

    }

    public SportCentre(String id, String latitude, String location, String longitude, String name) {
        this.id = id;
        this.latitude = latitude;
        this.location = location;
        this.longitude = longitude;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
