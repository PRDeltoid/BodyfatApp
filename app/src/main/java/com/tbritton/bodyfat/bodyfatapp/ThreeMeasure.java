package com.tbritton.bodyfat.bodyfatapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class ThreeMeasure extends AppCompatActivity implements LogInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_measure);
    }

    public void log(View view) {
        EditText measure_one_text   = (EditText) findViewById(R.id.measure_1_text);
        EditText measure_two_text   = (EditText) findViewById(R.id.measure_2_text);
        EditText measure_three_text = (EditText) findViewById(R.id.measure_3_text);
        int measure_one   = Integer.parseInt(measure_one_text.getText().toString());
        int measure_two   = Integer.parseInt(measure_two_text.getText().toString());
        int measure_three = Integer.parseInt(measure_three_text.getText().toString());

        int sum = measure_one + measure_two + measure_three;

        LogDbHelper mDbHelper = new LogDbHelper(getApplicationContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(LogContract.LogEntry.COLUMN_NAME_FOLDSUM, sum);
        values.put(LogContract.LogEntry.COLUMN_NAME_FOLDTYPE, "3 Measure");

        long newRowId = db.insert(LogContract.LogEntry.TABLE_NAME, null, values);
    }
}
