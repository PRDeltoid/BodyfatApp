package com.tbritton.bodyfat.bodyfatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EntryViewActivity extends AppCompatActivity {
    EditText measure_one_text,
             measure_two_text,
             measure_three_text,
             weight_text;
    Button   log_button;
    final int MEASURE_TYPE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_view);

        //Initialize toolbar
        Toolbar entry_toolbar = (Toolbar) findViewById(R.id.entry_toolbar);
        setSupportActionBar(entry_toolbar);

        //Get the entry ID, if we're editing an existing entry
        int entryid = getIntent().getIntExtra("EXTRA_ENTRYID", -1);
        if(entryid == -1) {
            LogDbHelper mDbHelper = new LogDbHelper(getApplicationContext());
            LogEntry log_entry = mDbHelper.pull_entry(getApplicationContext(), entryid);
        }

        //Assign components to variables
        measure_one_text   = (EditText) findViewById(R.id.measure_1_text);
        measure_two_text   = (EditText) findViewById(R.id.measure_2_text);
        measure_three_text = (EditText) findViewById(R.id.measure_3_text);
        weight_text        = (EditText) findViewById(R.id.weight_text);
        log_button = (Button) findViewById(R.id.three_measure_log_button);

        //Create an action for the log button
        log_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log();
                Intent main_activity_intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main_activity_intent);
            }
        });
    }

    public void log() {
        //Get values from fields and convert them to integers using parseInt
        int measure_one   = Integer.parseInt(measure_one_text.getText().toString());
        int measure_two   = Integer.parseInt(measure_two_text.getText().toString());
        int measure_three = Integer.parseInt(measure_three_text.getText().toString());

        //Calculate sum
        int sum = measure_one + measure_two + measure_three;

        //Temporary hardcoded variables
        //These will be moved to Settings later
        int age = 25;
        double weight = Integer.parseInt(weight_text.getText().toString()); //200.0;

        //Generate a date for the log
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        dateFormatter.setLenient(false);
        String date = dateFormatter.format(new Date());

        //Create our log entry to give to our logger
        LogEntry log_entry = new LogEntry(age, sum, MEASURE_TYPE, "Male", weight, date);
        LogDbHelper.log(getApplicationContext(), log_entry);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //PLACEHOLDER
            case R.id.action_save:
                log();
                Intent main_activity_intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main_activity_intent);
                return true;

            case R.id.action_delete:
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}
