package com.tbritton.bodyfat.bodyfatapp;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment
                                implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar= Calendar.getInstance();
        calendar.setTimeZone(SettingsHelper.get_timezone());
        return new TimePickerDialog(getActivity(), this, calendar.get(calendar.HOUR_OF_DAY), calendar.get(calendar.MINUTE), false);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        EntryViewActivity entry_view = (EntryViewActivity) getActivity();
        LogEntry log_entry = entry_view.log_entry;
        Calendar calendar = Calendar.getInstance();

        //create our date object from the selected time
        //load our old date first, so we can keep it's date value
        calendar.setTime(log_entry.get_date());
        calendar.set(calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(calendar.MINUTE, minute);
        calendar.setTimeZone(SettingsHelper.get_timezone());

        //Update the log entry object with our new date
        Date date = calendar.getTime();
        entry_view.log_entry.set_date(date);
        entry_view.refresh_ui();

    }
}
