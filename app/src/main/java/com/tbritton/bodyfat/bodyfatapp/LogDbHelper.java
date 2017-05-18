package com.tbritton.bodyfat.bodyfatapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LogDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "log.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + LogContract.LogEntry.TABLE_NAME + " (" +
                    LogContract.LogEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    LogContract.LogEntry.COLUMN_NAME_AGE + " INTEGER NOT NULL," +
                    LogContract.LogEntry.COLUMN_NAME_DATETIME + " DATETIME," +
                    LogContract.LogEntry.COLUMN_NAME_FOLDSUM + " INTEGER NOT NULL," +
                    LogContract.LogEntry.COLUMN_NAME_FOLDTYPE + " TEXT," +
                    LogContract.LogEntry.COLUMN_NAME_WEIGHT + " REAL NOT NULL)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LogContract.LogEntry.TABLE_NAME;


    public LogDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}


