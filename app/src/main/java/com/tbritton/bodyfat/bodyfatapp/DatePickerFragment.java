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
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day= c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        EntryViewActivity entry_view = (EntryViewActivity) getActivity();
        //this is lossy (doesn't save the time)
        //figure out a way to keep previously set time
        Date date = new Date(year, month, day);
        entry_view.log_entry.set_date(date);
        entry_view.refresh_ui();
    }


}
