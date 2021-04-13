package com.example.finalproject;

import java.util.Date;

public class listItem {
    private String title, desc, date = "unkown", link;
    private Date pub = null;
    private long id = 0;

    //constructors for date in string format
    public listItem(String ti, String de, String da, String li){
        title = ti;
        desc = de;
        date = da;
        link = li;
    }
    public listItem(String ti, String de, String da, String li, long i){
        title = ti;
        desc = de;
        date = da;
        link = li;
        id = i;
    }
    //constructors for items with date in date format
    public listItem(String ti, String de, Date da, String li){
        title = ti;
        desc = de;
        pub = da;
        link = li;
    }
    public listItem(String ti, String de, Date da, String li, long i){
        title = ti;
        desc = de;
        pub = da;
        link = li;
        id = i;
    }

    //constructors for unknown article dates
    public listItem(String ti, String de, String li){
        title = ti;
        desc = de;
        link = li;
    }
    public listItem(String ti, String de, String li, long i){
        title = ti;
        desc = de;
        link = li;
        id = i;
    }

    //getter functions
    public String getTitle(){
        return title;
    }
    public String getDesc(){
        return desc;
    }
    public String getDate(){
        if (pub == null) {
            return date;
        }else{
            return pub.toString();
        }
    }
    public String getLink(){ return link; }
    public long getId(){ return  id; }
}
