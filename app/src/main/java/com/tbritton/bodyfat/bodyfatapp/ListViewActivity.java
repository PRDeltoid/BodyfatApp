package com.tbritton.bodyfat.bodyfatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListViewActivity extends AppCompatActivity {
    ArrayList<LogEntry> weight_log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ListView listview = (ListView) findViewById(R.id.list_view);

        //Pull our log data object
        weight_log = LogDbHelper.pull_log(getApplicationContext()).as_arraylist();

        //Create an adapter for our data
        ListViewAdapter adapter = new ListViewAdapter();
        listview.setAdapter(adapter);

        //Create item click listener event
        //Click open a single entry view intent and passes the ID of the entry
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent single_entry_intent = new Intent(getApplicationContext(), EntryViewActivity.class);
                single_entry_intent.putExtra("EXTRA_ENTRY_ID",position);
                startActivity(single_entry_intent);
            }
        });
    }

    //Convert from long date to short date for readability
    private String convert_to_nice_date(String datestring) {
        DateFormat fromFormat = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy");
        DateFormat toFormat = new SimpleDateFormat("EEE MMM dd yyyy");

        Date date = null;
        try {
            date = fromFormat.parse(datestring);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return toFormat.format(date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_view, menu);
        return true;
    }

    //Code related to composing our list view
    private class ListViewAdapter extends BaseAdapter {

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
            return position;
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
            String bodyfat = new DecimalFormat("#.##").format(log_entry.get_bodyfat_percent()) + "%";
            String weight  = Double.toString(log_entry.get_weight()) + " lb";

            ((TextView) convertView.findViewById(R.id.date))
                    .setText(date);
            ((TextView) convertView.findViewById(R.id.bodyfat))
                    .setText(bodyfat);
            ((TextView) convertView.findViewById(R.id.weight))
                    .setText(weight);

            return convertView;
        }
    }
}
