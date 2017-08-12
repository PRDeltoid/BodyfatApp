package com.tbritton.bodyfat.bodyfatapp;

import android.content.Context;
import android.content.Intent;
import android.icu.util.Measure;
import android.media.audiofx.BassBoost;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class EntryViewActivity extends AppCompatActivity {
    private MeasurementEditText measure_one_text,
             measure_two_text,
             measure_three_text,
             measure_four_text,
             measure_five_text,
             measure_six_text,
             measure_seven_text,
             weight_text;
    private MeasurementEditText[] measurement_field_array;
    public TextView date_text,
                    time_text;
    private Spinner measure_type_spinner;
    private int entry_id,
                measure_type;
    public LogEntry log_entry;
    private boolean entry_has_id = false,
                    first_render_flag = true; //flag to determine if we are doing our initial render

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_view);

        ////////////////////////////////
        //Setup UI components / variables
        ////////////////////////////////
        measure_one_text   = (MeasurementEditText) findViewById(R.id.measure_1_text);
        measure_two_text   = (MeasurementEditText) findViewById(R.id.measure_2_text);
        measure_three_text = (MeasurementEditText) findViewById(R.id.measure_3_text);
        measure_four_text  = (MeasurementEditText) findViewById(R.id.measure_4_text);
        measure_five_text  = (MeasurementEditText) findViewById(R.id.measure_5_text);
        measure_six_text   = (MeasurementEditText) findViewById(R.id.measure_6_text);
        measure_seven_text = (MeasurementEditText) findViewById(R.id.measure_7_text);
        measurement_field_array = new MeasurementEditText[]{measure_one_text, measure_two_text, measure_three_text,
                                                            measure_four_text, measure_five_text, measure_six_text, measure_seven_text};
        weight_text        = (MeasurementEditText) findViewById(R.id.weight_text);
        date_text          = (TextView) findViewById(R.id.date_textview);
        time_text          = (TextView) findViewById(R.id.time_textview);

        /////////////////////////////
        //Measure Type Selector Code
        /////////////////////////////
        //TODO: Add other measure types to BodyfatCalculator class
        //TODO: Animation for collapsing items?
        //TODO: Clean up measure select UI

        //Set up the measure type spinner
        measure_type_spinner = (Spinner) findViewById(R.id.measure_type_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.measure_types_array, android.R.layout.simple_spinner_dropdown_item);
        measure_type_spinner.setAdapter(adapter);

        //Setup the toolbar
        Toolbar entry_toolbar = (Toolbar) findViewById(R.id.entry_toolbar);
        setSupportActionBar(entry_toolbar);

        //Flag for if the entry has an ID (aka we are editing an existing entry)
        set_entry_id_flag();

        //Setup the entry
        if(entry_has_id) {
            log_entry = LogDatabaseHelper.pull_entry(getApplicationContext(), entry_id);
            measure_type = log_entry.get_foldtype();
            populate_ui();
        } else {
            log_entry = new LogEntry(); //no id, pull a blank object
            measure_type = SettingsHelper.get_default_foldtype();
            populate_datetime_ui();     //only populate datetime as our other values are not set up yet
        }

        //Set the log entry foldtype and and display the measurement fields
        log_entry.set_foldtype(measure_type);
        show_measure_fields(measure_type);
        //Temporary(?) hardcode for setting the measure type spinner to the current measure type
        switch(measure_type) {
            case 3:
                measure_type_spinner.setSelection(0);
                break;
            case 5:
                measure_type_spinner.setSelection(1);
                break;
            case 7:
                measure_type_spinner.setSelection(2);
                break;
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
        measure_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //if it's the first render, flip the flag and exit before doing anything
                if(first_render_flag) {
                    first_render_flag = false;
                    return;
                }
                switch(measure_type_spinner.getSelectedItem().toString()) {
                    case "3 Measure":
                        measure_type = 3;
                        break;
                    case "5 Measure":
                        measure_type = 5;
                        break;
                    case "7 Measure":
                        measure_type = 7;
                        break;
                }
                log_entry.set_foldtype(measure_type);
                show_measure_fields(measure_type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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
                }
            }
        };
        weight_text.addTextChangedListener(refresh_on_change_watcher);
        //Iterate through the measurement text fields and add our change watcher
        for(MeasurementEditText measurement: get_active_measurement_fields()) {
            measurement.addTextChangedListener(refresh_on_change_watcher);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_entry_view, menu);
        return true;
    }

    //Toolbar actions
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

    private void show_measure_fields(int foldtype) {
        switch(foldtype) {
            case 3:
                measure_one_text.setVisibility(View.VISIBLE);
                measure_two_text.setVisibility(View.VISIBLE);
                measure_three_text.setVisibility(View.VISIBLE);
                measure_four_text.setVisibility(View.GONE);
                measure_five_text.setVisibility(View.GONE);
                measure_six_text.setVisibility(View.GONE);
                measure_seven_text.setVisibility(View.GONE);
                break;
            case 5:
                measure_one_text.setVisibility(View.VISIBLE);
                measure_two_text.setVisibility(View.VISIBLE);
                measure_three_text.setVisibility(View.VISIBLE);
                measure_four_text.setVisibility(View.VISIBLE);
                measure_five_text.setVisibility(View.VISIBLE);
                measure_six_text.setVisibility(View.GONE);
                measure_seven_text.setVisibility(View.GONE);
                break;
            case 7:
                measure_one_text.setVisibility(View.VISIBLE);
                measure_two_text.setVisibility(View.VISIBLE);
                measure_three_text.setVisibility(View.VISIBLE);
                measure_four_text.setVisibility(View.VISIBLE);
                measure_five_text.setVisibility(View.VISIBLE);
                measure_six_text.setVisibility(View.VISIBLE);
                measure_seven_text.setVisibility(View.VISIBLE);
                break;
        }
    }

    private LogEntry update_entry_object() {
        /////////////////////////////////////////////////////////////
        //Creates the entry object to be used for database operations
        //this method is used for both #log and #update
        //only data we "care" about updating is the folds and weight
        //everything else is already set via Settings and inference.
        ////////////////////////////////////////////////////////////
        ArrayList<Integer> folds = new ArrayList<>();
        double weight;

        //Get values from fields and convert them to integers using parseInt
        try {
            weight = Float.parseFloat(weight_text.getText().toString());
        } catch (Exception e) {
            weight = 0;
        }

        //Create folds array
        ArrayList<MeasurementEditText> active_fields = get_active_measurement_fields();
        for(MeasurementEditText field : active_fields) {
            folds.add(field.get_measurement());
        }

        //Set the data
        log_entry.set_folds(folds);
        log_entry.set_weight(weight);

        //Return the object
        return log_entry;
    }

    private void populate_ui() {
        ArrayList<Integer> folds = log_entry.get_folds();
        ArrayList<MeasurementEditText> measurement_fields = get_active_measurement_fields();
        double weight = log_entry.get_weight();

        //Iterate through our measurement fields array, and set it's text to the corresponding fold
        for(int i=0; i<measurement_fields.size(); i++) {
            measurement_fields.get(i).setTextFromInteger(folds.get(i));

        }
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

    private ArrayList<MeasurementEditText> get_active_measurement_fields() {
        ArrayList<MeasurementEditText> measurement_fields = new ArrayList<>();
        for(int i=0; i < measure_type; i++) {
            measurement_fields.add(measurement_field_array[i]);
        }
        return measurement_fields;
    }
}