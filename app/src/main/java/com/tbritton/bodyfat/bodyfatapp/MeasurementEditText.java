package com.tbritton.bodyfat.bodyfatapp;

import android.content.Context;
import android.util.AttributeSet;

public class MeasurementEditText extends android.support.v7.widget.AppCompatEditText {
    public MeasurementEditText(Context context) {
        super(context);
    }

    public MeasurementEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MeasurementEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int get_measurement() {
        int measurement;
        try {
            measurement = Integer.parseInt(this.getText().toString());
        } catch(Exception e) {
            measurement = 0;
        }
        return measurement;
    }

    public void setTextFromInteger(int i) {
        String s = Integer.toString(i);
        this.setText(s);
    }
}
