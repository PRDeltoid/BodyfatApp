package com.tbritton.bodyfat.bodyfatapp;

import com.jjoe64.graphview.series.DataPoint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class LogContainer {
    final private ArrayList<LogEntry> log;

    public LogContainer() {
        log = new ArrayList<>();
    }

    public void add(LogEntry entry) {
        log.add(entry);
    }

    public DataPoint[] as_datapoints() {
        DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.US);
        ArrayList<DataPoint> arrlist_datapoints = new ArrayList<DataPoint>();
        //Iterate through log and create a datapoint for each entry
        for(LogEntry log_entry: log) {
            try {
                DataPoint datapoint = new DataPoint(df.parse(log_entry.get_date()), log_entry.get_bodyfat_percent());
                arrlist_datapoints.add(datapoint);
            } catch(ParseException e) {
                //Abort
                return new DataPoint[] { new DataPoint(0,0) };
            }
        }
        //Convert from ArrayList to Array (required by graph renderer)
        DataPoint data_points[] = new DataPoint[arrlist_datapoints.size()];
        data_points = arrlist_datapoints.toArray(data_points);
        return data_points;
    }

    public ArrayList<LogEntry> as_arraylist() {
        return log;
    }
}
