package com.tbritton.bodyfat.bodyfatapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class LogDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "log.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + LogContract.LogEntry.TABLE_NAME + " (" +
                    LogContract.LogEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    LogContract.LogEntry.COLUMN_NAME_AGE + " INTEGER NOT NULL," +
                    LogContract.LogEntry.COLUMN_NAME_DATETIME + " DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    LogContract.LogEntry.COLUMN_NAME_FOLDSUM + " INTEGER NOT NULL," +
                    LogContract.LogEntry.COLUMN_NAME_FOLDTYPE + " INTEGER NOT NULL," +
                    LogContract.LogEntry.COLUMN_NAME_WEIGHT + " REAL NOT NULL," +
                    LogContract.LogEntry.COLUMN_NAME_SEX + " TEXT NOT NULL," +
                    LogContract.LogEntry.COLUMN_NAME_BODYFAT + " REAL NOT NULL)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LogContract.LogEntry.TABLE_NAME;

    //database helper singleton
    private static LogDbHelper mDbHelper = null;

    public LogDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    static void log(Context context, LogEntry log_entry) {
        if (mDbHelper == null) {
            mDbHelper = new LogDbHelper(context);
        }
        //Open our database
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //Create a ContentValues variable to store our database information
        ContentValues values = new ContentValues();

        //Store the database information!
        values.put(LogContract.LogEntry.COLUMN_NAME_FOLDSUM,  log_entry.get_sum());
        values.put(LogContract.LogEntry.COLUMN_NAME_FOLDTYPE, log_entry.get_foldtype());
        values.put(LogContract.LogEntry.COLUMN_NAME_BODYFAT,  log_entry.get_bodyfat_percent());
        values.put(LogContract.LogEntry.COLUMN_NAME_AGE,      log_entry.get_age());
        values.put(LogContract.LogEntry.COLUMN_NAME_SEX,      log_entry.get_sex());
        values.put(LogContract.LogEntry.COLUMN_NAME_WEIGHT,   log_entry.get_weight());
        values.put(LogContract.LogEntry.COLUMN_NAME_DATETIME, log_entry.get_date());

        //Insert information into the database
        try {
            long record_id = db.insertOrThrow(LogContract.LogEntry.TABLE_NAME, null, values);
        } catch (SQLException sql_exception) {
            //Failed to insert. Show error message and exit method
            Toast.makeText(context, "Failed to Log", Toast.LENGTH_SHORT).show();
            return;
        }
        //Successfully logged the bodyweight
        Toast.makeText(context, "Logged.", Toast.LENGTH_SHORT).show();
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
