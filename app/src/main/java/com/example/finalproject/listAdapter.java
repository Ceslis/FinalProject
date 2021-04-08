package com.example.finalproject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class listAdapter extends ArrayAdapter {
    private ArrayList<listItem> messages;
    private Integer[] imageid;
    private Activity context;

    public listAdapter(Activity context, ArrayList message, Integer[] imageid) {
        super(context, R.layout.list_layout, message);
        this.context = context;
        this.messages = message;
        this.imageid = imageid;

    }
    public listAdapter(Activity context, ArrayList message) {
        super(context, R.layout.list_layout, message);
        this.context = context;
        this.messages = message;
        this.imageid = null;

    }
    public int getCount(){
        return messages.size();
    }


    @Override
    public listItem getItem(int position) {
        //return super.getItem(position);
        return messages.get(position);
    }
    public long getItemId(int position){
        return messages.get(position).getId();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.list_layout, null, true);
        TextView message = (TextView) row.findViewById(R.id.msg);

        //positions for 2 people sending messages
        ImageView imagePerson = (ImageView) row.findViewById(R.id.imageView);


        message.setText(messages.get(position).getText());
        if (messages.get(position).getSender().equals("receive")) {
            imagePerson.setImageResource(imageid[1]);
        }

        return  row;
    }
}
