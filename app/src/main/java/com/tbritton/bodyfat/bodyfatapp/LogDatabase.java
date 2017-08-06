package com.tbritton.bodyfat.bodyfatapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class LogDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "log.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + LogContract.LogEntry.TABLE_NAME + " (" +
                    LogContract.LogEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    LogContract.LogEntry.COLUMN_NAME_AGE + " INTEGER NOT NULL," +
                    LogContract.LogEntry.COLUMN_NAME_DATETIME + " DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    LogContract.LogEntry.COLUMN_NAME_FOLDMEASURES + " TEXT NOT NULL," +
                    LogContract.LogEntry.COLUMN_NAME_FOLDTYPE + " INTEGER NOT NULL," +
                    LogContract.LogEntry.COLUMN_NAME_WEIGHT + " REAL NOT NULL," +
                    LogContract.LogEntry.COLUMN_NAME_SEX + " TEXT NOT NULL," +
                    LogContract.LogEntry.COLUMN_NAME_BODYFAT + " REAL NOT NULL)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LogContract.LogEntry.TABLE_NAME;

    private static LogDatabase mDbHelper = null;

    private LogDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    static public SQLiteDatabase get_readable_db(Context context) {
        acquire_instance(context);
        return mDbHelper.getReadableDatabase();
    }

    static public SQLiteDatabase get_writable_db(Context context) {
        acquire_instance(context);
        return mDbHelper.getWritableDatabase();
    }

    static private void acquire_instance(Context context) {
        //Simple method to create our Singleton if it is un-initialized
        if(mDbHelper == null) {
            mDbHelper = new LogDatabase(context);
        }
    }
}
