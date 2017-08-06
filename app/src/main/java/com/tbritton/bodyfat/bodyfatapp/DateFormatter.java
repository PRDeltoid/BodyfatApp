package com.tbritton.bodyfat.bodyfatapp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DateFormatter {
    public static String convert_to_nice_date(String date_string) {
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

    public static String convert_to_nice_time(String date_string) {
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

}
