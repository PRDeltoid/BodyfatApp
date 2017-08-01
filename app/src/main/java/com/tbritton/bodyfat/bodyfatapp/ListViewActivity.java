package com.tbritton.bodyfat.bodyfatapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
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

        weight_log = LogDbHelper.pull_log(getApplicationContext()).as_arraylist();

        ListViewAdapter adapter = new ListViewAdapter();
        listview.setAdapter(adapter);
    }

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

    private class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return weight_log.size();
        }

        @Override
        public LogEntry getItem(int position) {
            return weight_log.get(position);
        }

        @Override
        public long getItemId(int position) {
            return weight_log.get(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item, container, false);
            }

            //Get current log entry
            LogEntry log_entry = getItem(position);

            //Convert date to a more readable format
            String date = convert_to_nice_date(log_entry.get_date());


            ((TextView) convertView.findViewById(R.id.date))
                    .setText(date);
            ((TextView) convertView.findViewById(R.id.bodyfat))
                    .setText(Double.toString(log_entry.get_bodyfat_percent())+"%");
            ((TextView) convertView.findViewById(R.id.weight))
                    .setText(Double.toString(log_entry.get_weight())+" lb");

            return convertView;
        }
    }
}
