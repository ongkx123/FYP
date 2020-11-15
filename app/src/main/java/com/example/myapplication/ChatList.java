package com.example.myapplication;

public class ChatList {

    String sender;
    String url;
    String receiver;

    public ChatList(){

    }

    public ChatList(String sender, String url, String receiver) {
        this.sender = sender;
        this.url = url;
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
