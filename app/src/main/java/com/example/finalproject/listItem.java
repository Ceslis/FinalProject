package com.example.finalproject;

public class listItem {
    private String text;
    private String sender;
    private long id = 0;
    public listItem(String txt, String inOut){
        text = txt;
        sender = inOut;
    }
    public listItem(String txt, String inOut, long msgId){
        text = txt;
        sender = inOut;
        id = msgId;
    }
    public String getText(){
        return text;
    }
    public String getSender(){
        return sender;
    }
    public long getId(){
        return  id;
    }
}
