package com.tbritton.bodyfat.bodyfatapp;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ThreeMeasure extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_measure);
    }

    public void log(View view) {
        //Get values from fields and convert them to integers using parseInt
        EditText measure_one_text   = (EditText) findViewById(R.id.measure_1_text);
        EditText measure_two_text   = (EditText) findViewById(R.id.measure_2_text);
        EditText measure_three_text = (EditText) findViewById(R.id.measure_3_text);
        int measure_one   = Integer.parseInt(measure_one_text.getText().toString());
        int measure_two   = Integer.parseInt(measure_two_text.getText().toString());
        int measure_three = Integer.parseInt(measure_three_text.getText().toString());

        //Calculate sum
        int sum = measure_one + measure_two + measure_three;
        int age = 25; //Temporary hardcode
        double weight = 200.0; //Temporary hardcode
        String measure_type = "3 Measure"; //Temporary hardcode
        double bodyfat = -1;

        //Create our log entry to give to our logger
        LogEntry log_entry = new LogEntry(22, sum, 3, "Male", 200.0);

        LogDbHelper.log(getApplicationContext(), log_entry);
        //Create and populate a log entry with values
        ContentValues values = new ContentValues();
        //Generate a date for the log
        //(This might be changable later)
        DateFormat dateFormatter = new SimpleDateFormat("YYYY-MM-DD HH:MM");
        dateFormatter.setLenient(false);
        String today = dateFormatter.format(new Date());

        values.put(LogContract.LogEntry.COLUMN_NAME_FOLDSUM, sum);
        values.put(LogContract.LogEntry.COLUMN_NAME_CALCULATE_BODYFAT, bodyfat);
        values.put(LogContract.LogEntry.COLUMN_NAME_FOLDTYPE, measure_type);
        values.put(LogContract.LogEntry.COLUMN_NAME_GENDER, gender);
        values.put(LogContract.LogEntry.COLUMN_NAME_AGE, age);
        values.put(LogContract.LogEntry.COLUMN_NAME_DATETIME, today);
        values.put(LogContract.LogEntry.COLUMN_NAME_WEIGHT, weight);

        //Commit the entry to the log
        long newRowId = db.insert(LogContract.LogEntry.TABLE_NAME, null, values);
    }
}
