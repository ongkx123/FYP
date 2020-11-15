package com.example.myapplication;

import java.util.Comparator;

public class DisplaySportCentre{

    String name;
    String location;
    String meter;

    public DisplaySportCentre(){

    }

    public DisplaySportCentre(String name, String location, String meter) {
        this.name = name;
        this.location = location;
        this.meter = meter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMeter() {
        return meter;
    }

    public void setMeter(String meter) {
        this.meter = meter;
    }

    public  static Comparator<DisplaySportCentre> dsc = new Comparator<DisplaySportCentre>() {
       @Override
      public int compare(DisplaySportCentre o1, DisplaySportCentre o2) {

          return o1.getMeter().compareToIgnoreCase(o2.getMeter());
        }
    };

}

