package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    dataHelper msgDB ;
    ArrayAdapter<String> adapter;
    ArrayList<listItem> listItems=new ArrayList<listItem>();
    EditText ed;
    listAdapter messageList;

    ListView myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        msgDB = new dataHelper(this);
        myList = (ListView) findViewById(R.id.list);

        messageList = new listAdapter(this, listItems);
        myList.setAdapter(messageList);


        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {

                    return true;

                }




        });

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {


                //create alert dialogue that shows message info
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Do you want to delete this?");

                alert.setMessage("Row selected:" + messageList.getItem(position).getText() + "  " + messageList.getItem(position).getSender() + "\n" + "The database id:" + messageList.getItemId(position));

                alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        removeDB(messageList.getItem(position));
                        messageList.remove(messageList.getItem(position));
                        messageList.notifyDataSetChanged();
                        dialog.dismiss();

                    }
                });
                alert.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });
                alert.show();



            }



        });
        ed = (EditText) findViewById(R.id.txt);
/*
        Button sen = (Button) findViewById(R.id.sen);
        sen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postMessage(1);
            }
        });
        Button rec = (Button) findViewById(R.id.rec);
        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postMessage(2);
            }
        });


 */


    }

    //block of database related functions
    //removes the selected message from the db
    public void removeDB(listItem msg){
        SQLiteDatabase db = msgDB.getWritableDatabase();

        db.delete(dataHelper.TABLE_NAME," id = ?",new String[]{String.valueOf(msg.getId())});

        db.close();
    }
    //puts the entry into the database and returns the db entry id
    public long putDB(String msg, String msgType){
        SQLiteDatabase db = msgDB.getWritableDatabase();

        ContentValues cValues = new ContentValues();
        cValues.put(dataHelper.COL_TYPE, msgType);
        cValues.put(dataHelper.COL_MESSAGE, msg);

        long id = db.insert(dataHelper.TABLE_NAME, null, cValues);
        db.close();
        return id;

    }
    //posts them message and adds it to the adapter/database
    public void postMessage(int sender){
        long id;
        //will add the text to the array and will use appropriate picture then add items to list view
        listItem newMessage;
        if (sender==1){

            id = putDB(ed.getText().toString(),"send");
            messageList.add(new listItem(ed.getText().toString(),"send", id));
            messageList.notifyDataSetChanged();

        }else {

            id = putDB(ed.getText().toString(),"receive");
            messageList.add(new listItem(ed.getText().toString(), "receive", id));
            messageList.notifyDataSetChanged();
        }
        ed.setText("");

    }
    public void printCursor(Cursor c, int version){

        Log.e("cursor: " ,String.valueOf(version));
        Log.e("cursor: " ,String.valueOf(c.getColumnCount()));
        Log.e("cursor: " ,c.getColumnNames().toString());
        Log.e("cursor: " ,String.valueOf(c.getCount()));
        //get all the cursor results
        if (c.getCount() != 0) {
            if (c.moveToFirst()) {
                do {
                    Log.e("cursor: " ,c.getString(c.getColumnIndex(dataHelper.COL_ID)));

                    Log.e("cursor: " ,c.getString(c.getColumnIndex(dataHelper.COL_MESSAGE)));

                    Log.e("cursor: " ,c.getString(c.getColumnIndex(dataHelper.COL_TYPE)));

                } while (c.moveToNext());
            }
        }
    }
    public void loadData(){
        SQLiteDatabase db = msgDB.getWritableDatabase();

        //Cursor Results = db.query(false,dataHelper.TABLE_NAME,columns,null,selection?, selectionArg[])

        db.close();

    }
    private void openDB() {
        SQLiteDatabase db = msgDB.getWritableDatabase();
        try {
            Cursor c = db.rawQuery("SELECT * FROM " +
                            msgDB.TABLE_NAME
                    , null);

            if (c != null ) {
                if  (c.moveToFirst()) {
                    do {
                        String msg = c.getString(c.getColumnIndex(dataHelper.COL_MESSAGE));
                        String inOrOut = c.getString(c.getColumnIndex(dataHelper.COL_TYPE));
                        listItems.add((new listItem(msg ,inOrOut)));
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


