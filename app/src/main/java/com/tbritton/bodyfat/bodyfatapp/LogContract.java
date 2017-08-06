package com.tbritton.bodyfat.bodyfatapp;

import android.provider.BaseColumns;

final class LogContract {
    private LogContract () {}

    public static class LogEntry implements BaseColumns {
        public static final String TABLE_NAME="log";
        public static final String COLUMN_NAME_SEX="sex";
        public static final String COLUMN_NAME_DATETIME="date";
        public static final String COLUMN_NAME_AGE="age";
        public static final String COLUMN_NAME_WEIGHT="weight";
        public static final String COLUMN_NAME_FOLDMEASURES="foldmeasures";
        public static final String COLUMN_NAME_FOLDTYPE="foldtype";
        public static final String COLUMN_NAME_BODYFAT="bodyfat";
    }
}
