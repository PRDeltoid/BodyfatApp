package com.tbritton.bodyfat.bodyfatapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment
                            implements DatePickerDialog.OnDateSetListener {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c= Calendar.getInstance();
        c.setTimeZone(SettingsHelper.get_timezone());
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day= c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        EntryViewActivity entry_view = (EntryViewActivity) getActivity();
        LogEntry log_entry = entry_view.log_entry;
        //create our date object from the selected time
        Calendar calendar = Calendar.getInstance();
        //load our old time first, so we can keep time of day
        calendar.setTime(log_entry.get_date());
        calendar.set(year, month, day);
        Date date = calendar.getTime();

        //Update the log entry object with our new date
        entry_view.log_entry.set_date(date);
        entry_view.refresh_ui();
    }


}
