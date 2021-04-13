package com.example.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dataHelper extends SQLiteOpenHelper {

    final static String FILENAME = "MessagesDB";
    protected static String DB_NAME = "messages_db";
    protected static final int VERSION_NUM = 1;
    public final static String TABLE_NAME = "FAVORITES";
    public final static String COL_TITLE = "TITLE";
    public final static String COL_DATE = "DATE";
    public final static String COL_DESC = "DESCRIPTION";
    public final static String COL_LINK = "LINK";
    public final static String COL_ID = "_ID";

    public dataHelper(Context ctx) {
        super(ctx, DB_NAME, null, VERSION_NUM);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ( id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE + " TEXT, " + COL_DATE + " TEXT, "  + COL_DESC + " TEXT, "  + COL_LINK + " TEXT);");
    }

    @Override

    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }
}