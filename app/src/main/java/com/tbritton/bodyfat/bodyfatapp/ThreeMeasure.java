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


public class ThreeMeasure extends AppCompatActivity implements LogInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_measure);
    }

    public void log(View view) {
        EditText measure_one_text   = (EditText) findViewById(R.id.measure_1_text);
        EditText measure_two_text   = (EditText) findViewById(R.id.measure_2_text);
        EditText measure_three_text = (EditText) findViewById(R.id.measure_3_text);
        int measure_one   = Integer.parseInt(measure_one_text.getText().toString());
        int measure_two   = Integer.parseInt(measure_two_text.getText().toString());
        int measure_three = Integer.parseInt(measure_three_text.getText().toString());

        int sum = measure_one + measure_two + measure_three;
        int age = 25; //Temporary hardcode
        double weight = 200.0; //Temporary hardcode
        String measure_type = "3 Measure"; //Temporary hardcode
        double bodyfat = -1;

        //Determine gender for the formula
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String gender = prefs.getString("gender", "Male");

        if(gender == "Male") {
            bodyfat = 495 / (1.10938 - (0.0008267 * sum) + (0.0000016 * sum * sum) - (0.0002574 * age)) - 450;
        } else { //Female
            bodyfat = 495 / (1.089733 - (0.0009245 * sum) + (0.0000025 * sum * sum) - (0.0000979 * age)) - 450;
        }

        //Get out DB context
        LogDbHelper mDbHelper = new LogDbHelper(getApplicationContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

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
