package com.example.finalproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class favorites extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent i;
        //find out which option was selected and act go to that page
        switch (item.getItemId()) {
            case R.id.home:
                i = new Intent(favorites.this, MainActivity.class);
                //start new intent
                startActivity(i);
                return true;
            case R.id.setting:

                i = new Intent(favorites.this, settingsActivity.class);
                //start new intent
                startActivity(i);

                return true;
            //display help dialogue to explain this page
            case R.id.help:
                //create alert dialogue that tells you how to use this interface
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Help");

                //alert dialogue to choose if you ant to delete item
                alert.setMessage("click on an article title to view more info \nA long click will give the option to delete the article from the list");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.nav_home: {
                Intent choice = new Intent( favorites.this, MainActivity.class);
                startActivity(choice);
                break;
            }
            case R.id.nav_set: {
                Intent choice = new Intent( favorites.this, settingsActivity.class);
                startActivity(choice);
                break;
            }case R.id.nav_help: {

                //create alert dialogue that tells you how to use this interface
                AlertDialog.Builder alert = new AlertDialog.Builder(favorites.this);
                alert.setTitle("Help");

                //alert dialogue to choose if you ant to delete item
                alert.setMessage("click on an article title to view more info \nA long click will give the option to delete the article from the list");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });                break;
            }
        }
        //close navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawlay);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    dataHelper favDB ;
    ArrayAdapter<String> adapter;
    ArrayList<listItem> listItems=new ArrayList<listItem>();
    EditText ed;
    listAdapter listAdap;

    ListView myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        //set up our tool bar and drawer menu
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarF);
        setSupportActionBar(myToolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawlayF);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer, myToolbar,R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView nav = (NavigationView)findViewById(R.id.navviewF);
        nav.setNavigationItemSelectedListener(this);


        favDB = new dataHelper(this);

        myList = (ListView) findViewById(R.id.listfav);

        openDB();
        listAdap = new listAdapter(this, listItems);
        myList.setAdapter(listAdap);

        //click listener for deleting favorites
        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {

                //create alert dialogue that asks if you want to delete with this info
                AlertDialog.Builder alert = new AlertDialog.Builder(favorites.this);
                alert.setTitle("Do you want to delete this?");

                listItem temp = listAdap.getItem(position);

                //alert dialogue to choose if you ant to delete item
                alert.setMessage("Article selected:" + temp.getTitle() + "  " + temp.getDate() + "\n" + "The database id:" + listAdap.getItemId(position));
                alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //if yes, make changes to the database and update the list/view
                        removeDB(listAdap.getItem(position));
                        listAdap.remove(listAdap.getItem(position));
                        listAdap.notifyDataSetChanged();
                        dialog.dismiss();

                    }
                });
                alert.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //if no, just close the dialogue
                        dialog.dismiss();

                    }
                });
                alert.show();

                return true;

            }




        });

        //add on click for any list item to open display its details in a new window
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                Intent i = new Intent(favorites.this, itemDetails.class);
                //put our item info into the intent
                i.putExtra("fav", true);
                i.putExtra("title", listItems.get(position).getTitle());
                i.putExtra("date", listItems.get(position).getDate());
                i.putExtra("desc", listItems.get(position).getDesc());
                i.putExtra("link", listItems.get(position).getLink());

                //start new intent
                startActivity(i);

            }



        });

    }



        //block of database related functions
        //removes the selected message from the db
        public void removeDB(listItem article) {
            SQLiteDatabase db = favDB.getWritableDatabase();

            db.delete(dataHelper.TABLE_NAME, dataHelper.COL_ID + "=" + Long.toString( article.getId()), null);

        }

        //take items from the db and put them into our list
        private void openDB() {
            SQLiteDatabase db = favDB.getWritableDatabase();
            //try/catch incase our database doesn't exist or encounters an error
            try {
                Cursor c = db.rawQuery("SELECT * FROM " +
                                favDB.TABLE_NAME
                        , null);

                //iterate through our database to get all of its contentsa
                if (c != null ) {
                    if  (c.moveToFirst()) {
                        do {
                            String ti = c.getString(c.getColumnIndex(dataHelper.COL_TITLE)), da = c.getString(c.getColumnIndex(dataHelper.COL_DATE));
                            String de = c.getString(c.getColumnIndex(dataHelper.COL_DESC)), li = c.getString(c.getColumnIndex(dataHelper.COL_LINK));
                            listItems.add((new listItem(ti, da, de, li)));
                        }while (c.moveToNext());
                    }
                }
            } catch (SQLiteException se ) {
                Log.e(getClass().getSimpleName(), "Could not create or Open the database");
            } finally {
                if (db != null)
                    db.execSQL("DELETE FROM " + dataHelper.TABLE_NAME);
                db.close();
            }

        }

    //end of db functions block
}
