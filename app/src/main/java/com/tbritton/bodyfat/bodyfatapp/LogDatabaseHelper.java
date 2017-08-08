package com.tbritton.bodyfat.bodyfatapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.Date;

 class LogDatabaseHelper {
    private static final String[] ENTRY_PROJECTION = {
                    LogContract.LogEntry._ID,
                    LogContract.LogEntry.COLUMN_NAME_DATETIME,
                    LogContract.LogEntry.COLUMN_NAME_BODYFAT,
                    LogContract.LogEntry.COLUMN_NAME_AGE,
                    LogContract.LogEntry.COLUMN_NAME_FOLDMEASURES,
                    LogContract.LogEntry.COLUMN_NAME_FOLDTYPE,
                    LogContract.LogEntry.COLUMN_NAME_SEX,
                    LogContract.LogEntry.COLUMN_NAME_WEIGHT };

    static public LogContainer pull_log(Context context){
        //Pulls all log entries as a LogContainer object
        SQLiteDatabase db = LogDatabase.get_readable_db(context);

        Cursor cursor = create_cursor(db, null);
        LogContainer log = new LogContainer();

        //Iterate through our cursor
        while (cursor.moveToNext()) {
            LogEntry log_entry = create_entry_from_cursor(cursor);
            log.add(log_entry);
        }

        cursor.close();
        return log;
    }

    static public LogEntry pull_entry(Context context, int entry_id) {
        SQLiteDatabase db = LogDatabase.get_readable_db(context);

        String selection = LogContract.LogEntry._ID + " = " + entry_id;
        Cursor cursor = create_cursor(db, selection);

        //Move cursor to first position, our entry
        cursor.moveToNext();
        LogEntry log_entry = create_entry_from_cursor(cursor);

        cursor.close();
        return log_entry;
    }

    static void update_entry(Context context, int entry_id, LogEntry log_entry) {
        SQLiteDatabase db = LogDatabase.get_writable_db(context);

        ContentValues values = create_entry_content_values(log_entry);

        db.update(LogContract.LogEntry.TABLE_NAME, values, LogContract.LogEntry._ID + "=" + entry_id, null);
    }

    static void delete_entry(Context context, int entry_id) {
        SQLiteDatabase db = LogDatabase.get_writable_db(context);

        String[] entry_id_string = {Integer.toString(entry_id)};

        db.delete(LogContract.LogEntry.TABLE_NAME, LogContract.LogEntry._ID + " = ?", entry_id_string);
    }

    static void log(Context context, LogEntry log_entry) {
        SQLiteDatabase db = LogDatabase.get_writable_db(context);

        long record_id = save_entry(db, log_entry);

        if(record_id == -1) {
            Toast.makeText(context, "Failed to Log", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Logged. ID: " + record_id, Toast.LENGTH_SHORT).show();
        }
    }

    static private long save_entry(SQLiteDatabase db, LogEntry log_entry) {
        //Returns the record ID if successful, -1 otherwise
        ContentValues values = create_entry_content_values(log_entry);

        long record_id;
        //Insert information into the database
        try {
            record_id = db.insertOrThrow(LogContract.LogEntry.TABLE_NAME, null, values);
        } catch (SQLException sql_exception) {
            //Failed to insert. Show error message and exit method
            return -1;
        }
        return record_id;
    }

    static private ContentValues create_entry_content_values(LogEntry log_entry) {
        ContentValues values = new ContentValues();

        //Store the database information!
        values.put(LogContract.LogEntry.COLUMN_NAME_FOLDMEASURES,  log_entry.get_folds_string());
        values.put(LogContract.LogEntry.COLUMN_NAME_FOLDTYPE, log_entry.get_foldtype());
        values.put(LogContract.LogEntry.COLUMN_NAME_BODYFAT,  log_entry.get_bodyfat_percent());
        values.put(LogContract.LogEntry.COLUMN_NAME_AGE,      log_entry.get_age());
        values.put(LogContract.LogEntry.COLUMN_NAME_SEX,      log_entry.get_sex());
        values.put(LogContract.LogEntry.COLUMN_NAME_WEIGHT,   log_entry.get_weight());
        values.put(LogContract.LogEntry.COLUMN_NAME_DATETIME, DateFormatter.get_db_date_string(log_entry.get_date()));

        return values;
    }

    static private Cursor create_cursor(SQLiteDatabase db, String query) {
        return db.query(
                LogContract.LogEntry.TABLE_NAME,
                ENTRY_PROJECTION,
                query,
                null,
                null,
                null,
                null
        );
    }

    static private LogEntry create_entry_from_cursor(Cursor cursor) {
        LogEntry log_entry;
        String date_string,
                sex,
                folds_string;
        double  weight;
        int     age,
                foldtype,
                index;
        Date    date;

        try {
            //Get the data
            date_string = cursor.getString(
                    cursor.getColumnIndexOrThrow(LogContract.LogEntry.COLUMN_NAME_DATETIME));
            age = cursor.getInt(
                    cursor.getColumnIndexOrThrow(LogContract.LogEntry.COLUMN_NAME_AGE));
            folds_string = cursor.getString(
                    cursor.getColumnIndexOrThrow(LogContract.LogEntry.COLUMN_NAME_FOLDMEASURES));
            foldtype = cursor.getInt(
                    cursor.getColumnIndexOrThrow(LogContract.LogEntry.COLUMN_NAME_FOLDTYPE));
            sex = cursor.getString(
                    cursor.getColumnIndexOrThrow(LogContract.LogEntry.COLUMN_NAME_SEX));
            weight = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(LogContract.LogEntry.COLUMN_NAME_WEIGHT));
            index = cursor.getInt(
                    cursor.getColumnIndexOrThrow(LogContract.LogEntry._ID));

            date = DateFormatter.get_date_from_db_string(date_string);

            int folds[] = parse_fold_string(folds_string);
            //Create a log entry and insert it into our log
            log_entry = new LogEntry(
                            age,
                            folds,
                            foldtype,
                            sex,
                            weight,
                            date,
                            index
                        );

        } catch (Exception e) {
            //If there is an error, just return a placeholder entry to prevent crashing
            log_entry = new LogEntry();
        }
        return log_entry;
    }

    static private int[] parse_fold_string(String folds_string) {
        String[] s = folds_string.split(",");
        int[] numbers = new int[s.length];
        for (int curr = 0; curr < s.length; curr++)
            numbers[curr] = Integer.parseInt(s[curr]);
        return numbers;
    }
}