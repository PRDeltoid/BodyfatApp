package com.tbritton.bodyfat.bodyfatapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EntryViewActivity extends AppCompatActivity {
    private EditText measure_one_text,
             measure_two_text,
             measure_three_text,
             weight_text;
    public TextView date_text;
    private int entry_id;
    public LogEntry log_entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_view);

        ////////////////////////////////
        //Setup UI components / variables
        ////////////////////////////////
        measure_one_text   = (EditText) findViewById(R.id.measure_1_text);
        measure_two_text   = (EditText) findViewById(R.id.measure_2_text);
        measure_three_text = (EditText) findViewById(R.id.measure_3_text);
        weight_text        = (EditText) findViewById(R.id.weight_text);
        date_text          = (TextView) findViewById(R.id.date_textview);
        Toolbar entry_toolbar = (Toolbar) findViewById(R.id.entry_toolbar);
        setSupportActionBar(entry_toolbar);

        //Pull our entry, or create a new one if no entry ID exists
        entry_id = getIntent().getIntExtra("EXTRA_ENTRY_ID", -1);
        if(entry_id != -1) {
            //extra id was found
            log_entry = LogDatabaseHelper.pull_entry(getApplicationContext(), entry_id);
            populate_ui();
        } else {
            //no id, pull a blank object
            log_entry = new LogEntry();
        }

        /////////////////
        //Event listeners
        /////////////////
        date_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment date_picker_fragment = new DatePickerFragment();
                date_picker_fragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_entry_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if(entry_id == -1) {
                    log();
                } else {
                    update();
                }
                break;

            case R.id.action_delete:
                if(entry_id != -1) {
                    LogDatabaseHelper.delete_entry(getApplicationContext(), entry_id);
                }
                break;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
        setResult(RESULT_OK, null);
        finish();
        return true;
    }

    public static Intent get_start_intent(Context context, int entry_id) {
        Intent intent = new Intent(context, EntryViewActivity.class);
        intent.putExtra("EXTRA_ENTRY_ID", entry_id);
        return intent;
    }

    private void log() {
        LogEntry log_entry = create_entry_object();
        LogDatabaseHelper.log(getApplicationContext(), log_entry);
    }

    private void update() {
        entry_id = getIntent().getIntExtra("EXTRA_ENTRY_ID", -1);
        //If an entry_id is present, we are in "Edit" mode and should
        // update the entry instead of creating a new one
        if(entry_id != -1) {
            //Get the updated information
            LogEntry log_entry = create_entry_object();
            //Update it.
            LogDatabaseHelper.update_entry(getApplicationContext(), entry_id, log_entry);
        }
    }

    //Creates the entry object to be used for database operations
    //this method is used for both #log and #update
    //only data we "care" about updating is the folds and weight
    //everything else is already set via Settings and inference.
    private LogEntry create_entry_object() {
        //Get values from fields and convert them to integers using parseInt
        int measure_one   = Integer.parseInt(measure_one_text.getText().toString());
        int measure_two   = Integer.parseInt(measure_two_text.getText().toString());
        int measure_three = Integer.parseInt(measure_three_text.getText().toString());

        //Entry data
        int[] folds = {measure_one, measure_two, measure_three};
        double weight = Float.parseFloat(weight_text.getText().toString());

        //Set the data
        log_entry.set_folds(folds);
        log_entry.set_weight(weight);

        //Return the object
        return log_entry;
    }

    private void populate_ui() {
        int[] folds;
        try {
            folds = log_entry.get_folds();
        } catch(NullPointerException e) {
            folds = new int[] {0,0,0};
        }
        double weight = log_entry.get_weight();
        measure_one_text.setText(Integer.toString(folds[0]));
        measure_two_text.setText(Integer.toString(folds[1]));
        measure_three_text.setText(Integer.toString(folds[2]));
        weight_text.setText(Double.toString(weight));
        date_text.setText(DateFormatter.get_display_datestring(log_entry.get_date()));
    }

    public void refresh_ui() {
        date_text.setText(DateFormatter.get_display_datestring(log_entry.get_date()));

    }
}