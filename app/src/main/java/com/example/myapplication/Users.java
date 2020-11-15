package com.example.myapplication;

public class Users {

    String address,image,name,password,phone,phoneOrder;

    public Users(){
    }

    public Users(String address, String image, String name, String password, String phone, String phoneOrder) {
        this.address = address;
        this.image = image;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.phoneOrder = phoneOrder;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneOrder() {
        return phoneOrder;
    }

    public void setPhoneOrder(String phoneOrder) {
        this.phoneOrder = phoneOrder;
    }
}