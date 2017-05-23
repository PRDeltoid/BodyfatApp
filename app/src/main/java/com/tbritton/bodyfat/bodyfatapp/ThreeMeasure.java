package com.tbritton.bodyfat.bodyfatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


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

        //Create our log entry to give to our logger
        LogEntry log_entry = new LogEntry(22, sum, 3, "Male", 200.0);

        LogDbHelper.log(getApplicationContext(), log_entry);
    }
}
