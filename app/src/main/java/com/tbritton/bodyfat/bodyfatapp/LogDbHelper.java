package com.tbritton.bodyfat.bodyfatapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;
import android.widget.Toast;

import com.jjoe64.graphview.series.DataPoint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static android.util.Pair.create;

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

    //Pulls all log entries as a LogContainer object
    static LogContainer pull_log(Context context){
        LogContainer log = new LogContainer();
        if(mDbHelper == null) {
            mDbHelper = new LogDbHelper(context);
        }
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
            LogContract.LogEntry.COLUMN_NAME_DATETIME,
            LogContract.LogEntry.COLUMN_NAME_BODYFAT,
            LogContract.LogEntry.COLUMN_NAME_AGE,
            LogContract.LogEntry.COLUMN_NAME_FOLDSUM,
            LogContract.LogEntry.COLUMN_NAME_FOLDTYPE,
            LogContract.LogEntry.COLUMN_NAME_SEX,
            LogContract.LogEntry.COLUMN_NAME_WEIGHT
        };

        //String selection = "*";
        //String[] selection_args = {"*"};
        //String sort_order = LogContract.LogEntry.COLUMN_NAME_DATETIME + " DESC";
        Cursor cursor = db.query(
                LogContract.LogEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        DateFormat date_formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String datetime,
                sex;
        double  weight;
        int     age,
                sum,
                foldtype;

        //Iterate through our cursor
        while (cursor.moveToNext()) {
            try {
                //Get the data
                datetime = cursor.getString(
                            cursor.getColumnIndexOrThrow(LogContract.LogEntry.COLUMN_NAME_DATETIME));
                age      = cursor.getInt(
                            cursor.getColumnIndexOrThrow(LogContract.LogEntry.COLUMN_NAME_AGE));
                sum      = cursor.getInt(
                            cursor.getColumnIndexOrThrow(LogContract.LogEntry.COLUMN_NAME_FOLDSUM));
                foldtype = cursor.getInt(
                            cursor.getColumnIndexOrThrow(LogContract.LogEntry.COLUMN_NAME_FOLDTYPE));
                sex      = cursor.getString(
                            cursor.getColumnIndexOrThrow(LogContract.LogEntry.COLUMN_NAME_SEX));
                weight   = cursor.getDouble(
                            cursor.getColumnIndexOrThrow(LogContract.LogEntry.COLUMN_NAME_WEIGHT));

                //Create a log entry and insert it into our log
                LogEntry log_entry = new LogEntry(age, sum, foldtype, sex, weight, date_formatter.parse(datetime).toString());
                log.add(log_entry);

            } catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        //Release cursor resources
        cursor.close();
        return log;

    }

    static LogEntry pull_entry(Context context, int entry_id) {
        if(mDbHelper == null) {
            mDbHelper = new LogDbHelper(context);
        }
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                LogContract.LogEntry.COLUMN_NAME_DATETIME,
                LogContract.LogEntry.COLUMN_NAME_BODYFAT,
                LogContract.LogEntry.COLUMN_NAME_AGE,
                LogContract.LogEntry.COLUMN_NAME_FOLDSUM,
                LogContract.LogEntry.COLUMN_NAME_FOLDTYPE,
                LogContract.LogEntry.COLUMN_NAME_SEX,
                LogContract.LogEntry.COLUMN_NAME_WEIGHT
        };
        String selection = LogContract.LogEntry._ID + " = " + entry_id;
        //String[] selection_args = {"*"};
        //String sort_order = LogContract.LogEntry.COLUMN_NAME_DATETIME + " DESC";
        Cursor cursor = db.query(
                LogContract.LogEntry.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                null
        );

        LogEntry log_entry;

        try {
            //Get the data
            String datetime = cursor.getString(
                    cursor.getColumnIndexOrThrow(LogContract.LogEntry.COLUMN_NAME_DATETIME));
            int age      = cursor.getInt(
                    cursor.getColumnIndexOrThrow(LogContract.LogEntry.COLUMN_NAME_AGE));
            int sum      = cursor.getInt(
                    cursor.getColumnIndexOrThrow(LogContract.LogEntry.COLUMN_NAME_FOLDSUM));
            int foldtype = cursor.getInt(
                    cursor.getColumnIndexOrThrow(LogContract.LogEntry.COLUMN_NAME_FOLDTYPE));
            String sex      = cursor.getString(
                    cursor.getColumnIndexOrThrow(LogContract.LogEntry.COLUMN_NAME_SEX));
            double weight   = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(LogContract.LogEntry.COLUMN_NAME_WEIGHT));

            DateFormat date_formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            //Create a log entry and insert it into our log
            log_entry = new LogEntry(age, sum, foldtype, sex, weight, date_formatter.parse(datetime).toString());

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        cursor.close();
        return log_entry;
    }

    static void delete_entry(Context context, int entry_id) {
        if(mDbHelper == null) {
            mDbHelper = new LogDbHelper(context);
        }
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String[] entry_id_string = {Integer.toString(entry_id)};

        db.delete(LogContract.LogEntry.TABLE_NAME, LogContract.LogEntry._ID + " = ?", entry_id_string);

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
}
