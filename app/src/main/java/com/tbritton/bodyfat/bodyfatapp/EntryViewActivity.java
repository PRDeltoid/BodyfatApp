package com.tbritton.bodyfat.bodyfatapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EntryViewActivity extends AppCompatActivity {
    private EditText measure_one_text,
             measure_two_text,
             measure_three_text,
             weight_text;
    public TextView date_text,
                    time_text;
    private Spinner measure_type_spinner;
    private int entry_id;
    public LogEntry log_entry;
    private boolean entry_has_id = false;

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
        time_text          = (TextView) findViewById(R.id.time_textview);
        measure_type_spinner = (Spinner) findViewById(R.id.measure_type_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.measure_types, android.R.layout.simple_spinner_dropdown_item);

        //adapter.setDropDownViewResource(android.R.);
        measure_type_spinner.setAdapter(adapter);

        Toolbar entry_toolbar = (Toolbar) findViewById(R.id.entry_toolbar);
        setSupportActionBar(entry_toolbar);

        if(entry_has_id) {
            log_entry = LogDatabaseHelper.pull_entry(getApplicationContext(), entry_id);
            populate_ui();
        } else {
            log_entry = new LogEntry(); //no id, pull a blank object
            populate_datetime_ui();     //only populate datetime as our other values are not set up yet
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
        time_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 DialogFragment time_picker_fragment = new TimePickerFragment();
                 time_picker_fragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        TextWatcher refresh_on_change_watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //if textfield is not empty, update the object with new values after change
                if(!(s.toString().isEmpty())) {
                    update_entry_object();
                    Toast.makeText(getApplicationContext(), "Change", Toast.LENGTH_SHORT).show();
                }
            }
        };
        measure_one_text.addTextChangedListener(refresh_on_change_watcher);
        measure_two_text.addTextChangedListener(refresh_on_change_watcher);
        measure_three_text.addTextChangedListener(refresh_on_change_watcher);
        weight_text.addTextChangedListener(refresh_on_change_watcher);

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
                if(!entry_has_id) {
                    log();
                } else {
                    update();
                }
                break;

            case R.id.action_delete:
                if(entry_has_id) {
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
        LogEntry log_entry = update_entry_object();
        LogDatabaseHelper.log(getApplicationContext(), log_entry);
    }

    private void update() {
        if(entry_has_id) {
            //Get the updated information
            LogEntry log_entry = update_entry_object();
            //Update it.
            LogDatabaseHelper.update_entry(getApplicationContext(), entry_id, log_entry);
        }
    }

    private void set_entry_id_flag() {
        entry_id = getIntent().getIntExtra("EXTRA_ENTRY_ID", -1);
        if(entry_id != -1) {
            entry_has_id = true; //set entry_has_id flag
        }
    }

    //Creates the entry object to be used for database operations
    //this method is used for both #log and #update
    //only data we "care" about updating is the folds and weight
    //everything else is already set via Settings and inference.
    private LogEntry update_entry_object() {
        int measure_one,
            measure_two,
            measure_three;
        double weight;


        //Get values from fields and convert them to integers using parseInt
        try {
            weight = Float.parseFloat(weight_text.getText().toString());
        } catch (Exception e) {
            weight = 0;
        }
        try {
            measure_one = Integer.parseInt(measure_one_text.getText().toString());
        } catch(Exception e) {
            measure_one = 0;
        }
        try {
            measure_two   = Integer.parseInt(measure_two_text.getText().toString());
        } catch(Exception e) {
            measure_two = 0;
        }
        try {
            measure_three   = Integer.parseInt(measure_three_text.getText().toString());
        } catch(Exception e) {
            measure_three = 0;
        }

        //Create fold array
        int[] folds = {measure_one, measure_two, measure_three};

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
        populate_datetime_ui();
    }

    private void populate_datetime_ui() {
        date_text.setText(DateFormatter.get_display_datestring(log_entry.get_date()));
        time_text.setText(DateFormatter.get_display_timestring(log_entry.get_date()));
    }

    public void refresh_ui() {
        populate_datetime_ui();
    }
}