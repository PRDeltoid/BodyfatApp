package com.tbritton.bodyfat.bodyfatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ThreeMeasure extends AppCompatActivity {
    EditText measure_one_text,
             measure_two_text,
             measure_three_text;
    Button   log_button;
    final int MEASURE_TYPE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_measure);

        //Assign components to variables
        measure_one_text   = (EditText) findViewById(R.id.measure_1_text);
        measure_two_text   = (EditText) findViewById(R.id.measure_2_text);
        measure_three_text = (EditText) findViewById(R.id.measure_3_text);
        log_button = (Button) findViewById(R.id.three_measure_log_button);

        //Create an action for the log button
        log_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log(v);
                Intent main_activity_intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main_activity_intent);
            }
        });
    }

    public void log(View view) {
        //Get values from fields and convert them to integers using parseInt
        int measure_one   = Integer.parseInt(measure_one_text.getText().toString());
        int measure_two   = Integer.parseInt(measure_two_text.getText().toString());
        int measure_three = Integer.parseInt(measure_three_text.getText().toString());

        //Calculate sum
        int sum = measure_one + measure_two + measure_three;

        //Temporary hardcoded variables
        //These will be moved to Settings later
        int age = 25;
        double weight = 200.0;

        //Generate a date for the log
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        dateFormatter.setLenient(false);
        String date = dateFormatter.format(new Date());

        //Create our log entry to give to our logger
        LogEntry log_entry = new LogEntry(age, sum, MEASURE_TYPE, "Male", weight, date);
        LogDbHelper.log(getApplicationContext(), log_entry);
    }
}
