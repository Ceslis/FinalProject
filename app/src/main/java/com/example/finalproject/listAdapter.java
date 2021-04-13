package com.example.finalproject;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class listAdapter extends ArrayAdapter implements Filterable {
    private ArrayList<listItem> articles;
    private ArrayList<listItem> filteredArticles;
    private Activity context;


    public listAdapter(Activity context, ArrayList article) {
        super(context, R.layout.list_layout, article);
        this.context = context;
        this.articles = article;
        filteredArticles = new ArrayList<listItem>();

    }

    //here we have our filter to limit remove articles not fitting the title
    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        //move all itmes to another list so that we can rebuild a filtered set
        filteredArticles.addAll(articles);
        articles.clear();
        listItem temp;

        if (charText.length() == 0) {

            return;
        } else {
            //go through our list
            for (int i=0;i<filteredArticles.size();i++) {

                temp = filteredArticles.get(i);
                //add matching entries
                if (temp.getTitle().toLowerCase().contains(charText)) {
                        articles.add(temp);
                    }
            }
            //empty our temporary list for reuse
            filteredArticles.clear();
        }
        notifyDataSetChanged();


    }

    public int getCount(){
        return articles.size();
    }


    @Override
    public listItem getItem(int position) {
        return articles.get(position);
    }
    public long getItemId(int position){
        return articles.get(position).getId();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.list_layout, null, true);

        TextView title = (TextView) row.findViewById(R.id.title);


        title.setText(articles.get(position).getTitle());

        return  row;
    }
}
