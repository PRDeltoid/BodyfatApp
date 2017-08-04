package com.tbritton.bodyfat.bodyfatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EntryViewActivity extends AppCompatActivity {
    EditText measure_one_text,
             measure_two_text,
             measure_three_text,
             weight_text;
    final int MEASURE_TYPE = 3;
    int entry_id;


    private LogEntry create_entry_object() {
        //Get values from fields and convert them to integers using parseInt
        int measure_one   = Integer.parseInt(measure_one_text.getText().toString());
        int measure_two   = Integer.parseInt(measure_two_text.getText().toString());
        int measure_three = Integer.parseInt(measure_three_text.getText().toString());

        //Calculate sum
        int[] folds = {measure_one, measure_two, measure_three};

        //Temporary hardcoded variables
        //These will be moved to Settings later
        int age = 25;
        double weight = Float.parseFloat(weight_text.getText().toString());

        //Generate a date for the log
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        dateFormatter.setLenient(false);
        String date = dateFormatter.format(new Date());

        //Create our log entry to give to our logger
        LogEntry log_entry = new LogEntry(age, folds, MEASURE_TYPE, "Male", weight, date);

        return log_entry;
    }

    public void log() {
        LogEntry log_entry = create_entry_object();
        LogDbHelper.log(getApplicationContext(), log_entry);
    }

    public void update() {
        entry_id = getIntent().getIntExtra("EXTRA_ENTRY_ID", -1);
        //If an entry_id is present, we are in "Edit" mode and should
        // update the entry instead of creating a new one
        if(entry_id != -1) {
            //Get the updated information
            LogEntry log_entry = create_entry_object();
            //Update it.
            LogDbHelper.update_entry(getApplicationContext(), entry_id, log_entry);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_view);

        //Assign components to variables
        measure_one_text   = (EditText) findViewById(R.id.measure_1_text);
        measure_two_text   = (EditText) findViewById(R.id.measure_2_text);
        measure_three_text = (EditText) findViewById(R.id.measure_3_text);
        weight_text        = (EditText) findViewById(R.id.weight_text);

        //Initialize toolbar
        Toolbar entry_toolbar = (Toolbar) findViewById(R.id.entry_toolbar);
        setSupportActionBar(entry_toolbar);

        //Get the entry ID, if we're editing an existing entry
        entry_id = getIntent().getIntExtra("EXTRA_ENTRY_ID", -1);
        if(entry_id != -1) {
            //extra id was found
            //need to load in entry information to the form
            LogEntry entry = LogDbHelper.pull_entry(getApplicationContext(), entry_id);
            int[] folds = entry.get_folds();
            //temporary hardcode for the 3 measure case
            measure_one_text.setText(Integer.toString(folds[0]));
            measure_two_text.setText(Integer.toString(folds[1]));
            measure_three_text.setText(Integer.toString(folds[2]));
            weight_text.setText(Double.toString(entry.get_weight()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_entry_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent list_view_activity_intent = new Intent(getApplicationContext(), ListViewActivity.class);
        switch (item.getItemId()) {

            //PLACEHOLDER
            case R.id.action_save:
                if(entry_id == -1) {
                    log();
                } else {
                    update();
                }

                startActivity(list_view_activity_intent);
                return true;

            case R.id.action_delete:
                LogDbHelper db = new LogDbHelper(getApplicationContext());
                if(entry_id != -1) {
                    db.delete_entry(getApplicationContext(), entry_id);
                }
                startActivity(list_view_activity_intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

}
