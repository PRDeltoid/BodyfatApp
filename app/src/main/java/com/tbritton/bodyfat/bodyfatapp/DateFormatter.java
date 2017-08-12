package com.tbritton.bodyfat.bodyfatapp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DateFormatter {
    public static String get_db_date_string(Date date) {
        //This func is here to help maintain a consistent database timestamp layout
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.US);
        formatter.setTimeZone(SettingsHelper.get_timezone());
        formatter.setLenient(false);
        return formatter.format(date);
    }

    public static Date get_date_from_db_string(String date_string) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.ENGLISH);
        format.setTimeZone(SettingsHelper.get_timezone());
        try {
            return format.parse(date_string);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static String get_display_datestring(Date date) {
        //Convert from long date to short date for readability
        DateFormat format = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        //Set the timezone
        format.setTimeZone(SettingsHelper.get_timezone());
        return format.format(date);
    }

    public static String get_display_timestring(Date date) {
        //Convert from long date to short date for readability
        DateFormat format = new SimpleDateFormat("hh:mm a", Locale.US);
        //Set the timezone
        format.setTimeZone(SettingsHelper.get_timezone());

        return format.format(date);

    }
}
