package com.vlille.checker.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBOpenHelper /*extends AbstractDBOpenHelper*/ {

    private static final String TAG = DBOpenHelper.class.getSimpleName();

    public DBOpenHelper(Context ctx) {
        //super(ctx, DB.FILE, DB.VERSION);
    }

    protected void onCreateTables(SQLiteDatabase db) {
        Log.d(TAG, "onCreateTables");
        //createTables(db, Station.class);
        //createTables(db, Metadata.class);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade");
        //dropTables(db);
        //onCreate(db);
    }

}

