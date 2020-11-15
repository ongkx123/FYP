package com.example.myapplication;

public class Type {

    String name;
    String image;

    public Type(){

    }

    public Type(String name, String url) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setUrl(String image) {
        this.image = image;
    }
}
