package com.example.finalproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    String url = "http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml";
    dataHelper favDB ;
    ArrayAdapter<String> adapter;
    ArrayList<listItem> listItems=new ArrayList<listItem>(),mem;
    EditText ed;
    Button refresh;
    ProgressBar bar ;
    listAdapter listAdap;
    ListView myList;
    bbc pop;

    private static final String PREFS_NAME = "com.example.finalproject";
    private static final String PREFS = "lang";
    private static final String PREFS2 = "filter";


    private SharedPreferences sharedPref;


    //our toolbar handler
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolmenu, menu);
        return true;
    }

    //our drawer handler
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent i;
        //find out which option was selected and act go to that page
        switch (item.getItemId()) {
            case R.id.home:

                return true;
            case R.id.favorites:

                i = new Intent(MainActivity.this, favorites.class);
                //start new intent
                startActivity(i);
                return true;
            case R.id.setting:

                i = new Intent(MainActivity.this, settingsActivity.class);
                //start new intent
                startActivity(i);

                return true;
            //display help dialogue to explain this page
            case R.id.help:


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.nav_fav: {
                Intent choice = new Intent( MainActivity.this, favorites.class);
                startActivity(choice);
                break;
            }
            case R.id.nav_set: {
                Intent choice = new Intent( MainActivity.this, settingsActivity.class);
                startActivity(choice);
                break;
            }case R.id.nav_help: {

                //create alert dialogue that tells you how to use this interface
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Help");

                //alert dialogue to choose if you ant to delete item
                alert.setMessage("click on an article title to view more info and an option of favoriting it \nA long click will remove the article from the list");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                break;
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
        setContentView(R.layout.activity_main);

        //set up our tool bar and drawer menu
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawlay);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer, myToolbar,R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView nav = (NavigationView)findViewById(R.id.navview);
        nav.setNavigationItemSelectedListener(this);

        sharedPref = getSharedPreferences("com.example.finalproject",Context.MODE_PRIVATE);


        //add second language

        //settings to change language or clear all favorites and clear prefs?
        //add shared prefs


        ed = (EditText) findViewById(R.id.txt);
        refresh = (Button) findViewById(R.id.refresh);
        myList = (ListView) findViewById(R.id.list);
        bar = (ProgressBar) findViewById(R.id.bar);

        //fills up our list from bbc feed
        pop = new bbc();
        pop.execute(url);


        //set our shared prefs for the filter text to show what was last filtered for
        ed.setText(sharedPref.getString(PREFS2,""));

        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listAdap.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //update our sharedprefs to contain this filter for next opening
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(PREFS2,ed.getText().toString());
                editor.commit();
            }
        });


        //long click listener for list items
        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                //long click removes the article from the list and shows a snackbar
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Article hidden", Snackbar.LENGTH_LONG);
                snackbar.show();
                listAdap.remove(listAdap.getItem(position));
                listAdap.notifyDataSetChanged();

                return true;
            }

        });

        //short click listener for list items
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                //short click sends user to fragment showing the article info
                Intent i = new Intent(MainActivity.this, itemDetails.class);
                //put our item info into the intent
                i.putExtra("fav", false);
                i.putExtra("title", listItems.get(position).getTitle());
                i.putExtra("date", listItems.get(position).getDate());
                i.putExtra("desc", listItems.get(position).getDesc());
                i.putExtra("link", listItems.get(position).getLink());

                //start new intent
                startActivity(i);
            }
        });

        //button click listener
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //refreshes our article list
                listItems.clear();
                listItems.addAll(mem);
                listAdap.notifyDataSetChanged();
            }
        });

    }


    //our asynctask class

    class bbc extends AsyncTask<String, Integer, String> {

        private ArrayList<String[]> results;

        @Override
        protected String doInBackground(String... urls) {
            try {
                //begin getting our results

                publishProgress(25);
                results = loadXmlFromNetwork(urls[0]);
                publishProgress(50);


                publishProgress(100);

                return results.toString();
            } catch (IOException e) {

                return "error";

            } catch (XmlPullParserException e) {
                return "error";

            }
        }

        //remove our progress bar after we are done
        @Override
        protected void onPostExecute(String result) {
            //go through our variable arrays, filling our list array
            for (int i=0;i<results.size();i++ ){

                //add our item to ourlist with a new item created from our results
                listItems.add(new listItem(results.get(i)[0],results.get(i)[2],results.get(i)[1],results.get(i)[3]));
            }
            //keep a copy of the initial list in case they want to go back
            mem = new ArrayList<listItem>();
            mem.addAll(listItems);
            //setting the adapter for the list
            listAdap = new listAdapter(MainActivity.this, listItems);
            myList.setAdapter(listAdap);
            bar.setVisibility(View.INVISIBLE);

        }

        //update our progress bar to indicate how close we are to finishing
        protected void publishProgress(int prog){
            bar.setVisibility(View.VISIBLE);
            bar.setProgress(prog);
        }


        private ArrayList<String[]> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;

            // Instantiate the parser
            StackOverflowXmlParser stackOverflowXmlParser = new StackOverflowXmlParser();
            ArrayList<String[]> l1;

            //try to get our xml then try to get all of its info
            try {
                stream = downloadUrl(urlString);
                l1 = stackOverflowXmlParser.parse(stream);

            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
            return l1;
        }


        private InputStream downloadUrl(String urlString) throws IOException {
            //convert our string to url then try to connect to it
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();

            return conn.getInputStream();
        }

        public class StackOverflowXmlParser {
            private final String ns = null;

            private ArrayList<String[]> readFeed(XmlPullParser myParser) throws XmlPullParserException, IOException {
                ArrayList<String[]> entries = new ArrayList<String[]>();

                int event;
                String text = null, name = null;
                String[] result = new String[4];
                event = myParser.getEventType();

                //go through the xml to get all of the info of the articles
                while (event != XmlPullParser.END_DOCUMENT) {
                    name = null;
                    switch (event){
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            name=myParser.getName();

                            //depending on the info we are pulling input it into the correct position in our array
                            if(name.equals("title")){
                                text = myParser.nextText();

                                result[0] = text;
                            }
                            else if(name.equals("pubDate")){
                                text = myParser.nextText();

                                result[1] =  text;
                                //our entry should now be complete so we add it to our list
                                entries.add(result);
                                result = new String[4];

                            }
                            else if(name.equals("description")){
                                text = myParser.nextText();

                                result[2] =  text;
                            }
                            else if(name.equals("link")){
                                text = myParser.nextText();

                                result[3] = text;

                            }
                            else{
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            name = myParser.getName();
                    }
                    event = myParser.next();
                }
                return entries;
            }


            //create our parser and hand it to the method that will get all the info
            public ArrayList<String[]> parse(InputStream in) throws XmlPullParserException, IOException {

                try {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(in, null);

                    return readFeed(parser);
                } finally {
                    in.close();
                }
            }

        }


    }


}


