package com.tbritton.bodyfat.bodyfatapp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DateFormatter {
    public static String get_db_date_string(Date date) {
        //This func is here to help maintain a consistent database timestamp layout
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.US);
        dateFormatter.setLenient(false);
        return dateFormatter.format(date);
    }

    public static Date get_date_from_db_string(String date_string) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.ENGLISH);
        try {
            return format.parse(date_string);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static String get_display_datestring(Date date) {
        //Convert from long date to short date for readability
        DateFormat format = new SimpleDateFormat("EEE MMM dd yyyy", Locale.US);
        return format.format(date);
    }

    public static String get_display_timestring(Date date) {
        //Convert from long date to short date for readability
        DateFormat format = new SimpleDateFormat("kk:mm", Locale.US);

        return format.format(date);

    }
}
