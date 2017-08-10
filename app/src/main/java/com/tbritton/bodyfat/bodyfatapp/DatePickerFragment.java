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
        //create our date object from the selected time
        Calendar c = Calendar.getInstance();
        c.set(year, month, day );
        Date date = c.getTime();

        //Update the log entry object with our new date
        EntryViewActivity entry_view = (EntryViewActivity) getActivity();
        entry_view.log_entry.set_date(date);
        entry_view.refresh_ui();
    }


}
