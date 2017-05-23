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

import static java.text.DateFormat.getDateTimeInstance;



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
        int measure_type = 3;
        //Generate a date for the log
        DateFormat dateFormatter = new SimpleDateFormat("YYYY-MM-DD HH:MM");
        dateFormatter.setLenient(false);
        String date = dateFormatter.format(new Date());

        //Create our log entry to give to our logger
        LogEntry log_entry = new LogEntry(age, sum, measure_type, "Male", weight, date);
        LogDbHelper.log(getApplicationContext(), log_entry);
    }
}
