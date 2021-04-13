package com.example.finalproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class settingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


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
                i = new Intent(settingsActivity.this, MainActivity.class);
                //start new intent
                startActivity(i);
                return true;
            case R.id.favorites:

                i = new Intent(settingsActivity.this, favorites.class);
                //start new intent
                startActivity(i);

                return true;
            //display help dialogue to explain this page
            case R.id.help:
                //create alert dialogue that tells you how to use this interface
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Help");

                //alert dialogue to choose if you ant to delete item
                alert.setMessage("");
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
                Intent choice = new Intent( settingsActivity.this, MainActivity.class);
                startActivity(choice);
                break;
            }
            case R.id.nav_fav: {
                Intent choice = new Intent( settingsActivity.this, favorites.class);
                startActivity(choice);
                break;
            }case R.id.nav_help: {

                //create alert dialogue that tells you how to use this interface
                AlertDialog.Builder alert = new AlertDialog.Builder(settingsActivity.this);
                alert.setTitle("Help");

                //alert dialogue to choose if you ant to delete item
                alert.setMessage("You can use the buttons to delete your prefs/favorites and change your language");
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //set up our tool bar and drawer menu
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawlay);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer, myToolbar,R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView nav = (NavigationView)findViewById(R.id.navview);
        nav.setNavigationItemSelectedListener(this);



    }
}
