package com.tbritton.bodyfat.bodyfatapp;

import com.jjoe64.graphview.series.DataPoint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

class LogContainer {
    final private ArrayList<LogEntry> log;

    public LogContainer() {
        log = new ArrayList<>();
    }

    public void add(LogEntry entry) {
        log.add(entry);
    }

    public DataPoint[] as_datapoints() {
        DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.US);
        ArrayList<DataPoint> arrlist_datapoints = new ArrayList<>();
        //Iterate through log and create a datapoint for each entry
        for(LogEntry log_entry: log) {
            DataPoint datapoint = new DataPoint(log_entry.get_date(), log_entry.get_bodyfat_percent());
            arrlist_datapoints.add(datapoint);
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
