package com.example.finalproject;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;


public class details extends Fragment {

    dataHelper favDB ;
    View inflated;
    TextView ti, da, de, li;
    Button fav;




    private OnFragmentInteractionListener mListener;

    public details() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflated =  inflater.inflate(R.layout.fragment_details, container, false);


        //get our text views
         ti = (TextView) inflated.findViewById(R.id.aTitle);
         da = (TextView) inflated.findViewById(R.id.aDate);
         de = (TextView) inflated.findViewById(R.id.aDesc);
         li = (TextView) inflated.findViewById(R.id.aLink);

        fav = (Button) inflated.findViewById(R.id.fav);



        //if fragment is called from the favorites list, hide the favorite button
        if(getArguments().getBoolean("fav")){
            fav.setVisibility(View.INVISIBLE);
        }else{
            fav.setVisibility(View.VISIBLE);
        }

        //retrieve our article info sent to the fragment
        ti.setText("Title = "+getArguments().getString("title"));
        da.setText("Date = "+getArguments().getString("date"));
        de.setText("Desc = "+getArguments().getString("desc"));
        li.setText("Link ="+getArguments().getString("link"));


        //set click listener for our favorites button
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .remove(details.this).commit();
                //adds the article to favorites database
                putDB(getArguments().getString("title"), getArguments().getString("date"), getArguments().getString("desc"), getArguments().getString("link"));
                Toast.makeText(details.this.getContext() ,"Article added to favorites", Toast.LENGTH_LONG).show();

            }
        });

        //set on click listener for our link
        li.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .remove(details.this).commit();
                //opens web page of url
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getArguments().getString("link")));
                startActivity(i);

            }
        });

        return inflated;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    //puts the entry into the database and returns the db entry id
    public long putDB(String title, String date, String desc, String link){
        SQLiteDatabase db = favDB.getWritableDatabase();

        ContentValues cValues = new ContentValues();
        cValues.put(dataHelper.COL_TITLE, title);
        cValues.put(dataHelper.COL_DATE, date);
        cValues.put(dataHelper.COL_DESC, desc);
        cValues.put(dataHelper.COL_LINK, link);

        long id = db.insert(dataHelper.TABLE_NAME, null, cValues);
        db.close();
        return id;

    }
}
