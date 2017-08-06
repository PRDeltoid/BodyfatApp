package com.tbritton.bodyfat.bodyfatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ListViewActivity extends AppCompatActivity {
    private ListView listview;
    private ArrayList<LogEntry> weight_log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listview = (ListView) findViewById(R.id.list_view);

        //Pull our log data object
        populate_weight_log();

        //Create an adapter for our data
        ListViewAdapter adapter = new ListViewAdapter();
        listview.setAdapter(adapter);

        //Create item click listener event
        //Click open a single entry view intent and passes the ID of the entry
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent single_entry_intent = EntryViewActivity.get_start_intent(getApplicationContext(), (int) id);
                startActivityForResult(single_entry_intent, 1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_new_entry) {
            Intent new_entry_intent = new Intent(getApplicationContext(), EntryViewActivity.class);
            startActivityForResult(new_entry_intent, 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        populate_weight_log();
        BaseAdapter adapter = (BaseAdapter) listview.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private void populate_weight_log() {
        try {
            weight_log = LogDatabaseHelper.pull_log(getApplicationContext())
                                          .as_arraylist();
        } catch(NullPointerException e) {
            weight_log = new ArrayList<>();
        }
    }

    private String convert_to_nice_date(String date_string) {
        //Convert from long date to short date for readability
        DateFormat fromFormat = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.US);
        DateFormat toFormat = new SimpleDateFormat("EEE MMM dd yyyy", Locale.US);

        Date date = null;
        try {
            date = fromFormat.parse(date_string);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return toFormat.format(date);
    }

    private String convert_to_nice_time(String date_string) {
        //Convert from long date to short date for readability
        DateFormat fromFormat = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.US);
        DateFormat toFormat = new SimpleDateFormat("kk:mm", Locale.US);

        Date date = null;
        try {
            date = fromFormat.parse(date_string);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return toFormat.format(date);

    }

    private class ListViewAdapter extends BaseAdapter {
        //Code related to composing our list view

        @Override
        public int getCount() {
            return weight_log.size();
        }

        //Gets list item data
        @Override
        public LogEntry getItem(int position) {
            return weight_log.get(position);
        }

        //Get list item id
        @Override
        public long getItemId(int position) {
            return weight_log.get(position).get_database_index();
            //return position;
        }

        //Creates the the individual list item view
        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item, container, false);
            }

            //Get current log entry
            LogEntry log_entry = getItem(position);

            //Convert date to a more readable format
            String date    = convert_to_nice_date(log_entry.get_date());
            String time    = convert_to_nice_time(log_entry.get_date());
            String bodyfat = new DecimalFormat("#.##").format(log_entry.get_bodyfat_percent()) + "%";
            String weight  = Double.toString(log_entry.get_weight()) + " lb";

            ((TextView) convertView.findViewById(R.id.date))
                    .setText(date);
            ((TextView) convertView.findViewById(R.id.bodyfat))
                    .setText(bodyfat);
            ((TextView) convertView.findViewById(R.id.weight))
                    .setText(weight);
            ((TextView) convertView.findViewById(R.id.time))
                    .setText(time);

            return convertView;
        }
    }
}
