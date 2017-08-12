package com.tbritton.bodyfat.bodyfatapp;


import java.util.TimeZone;

public class SettingsHelper {
    //Temporary placeholders until Settings are implemented
    public static int get_age() {
        return 25;
    }

    public static String get_sex() {
        return "Male";
    }

    public static TimeZone get_timezone() { return TimeZone.getTimeZone("America/Los_Angeles");}

    public static int get_default_foldtype() { return 3; }
}
