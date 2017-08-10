package com.tbritton.bodyfat.bodyfatapp;

import java.util.Date;

class LogEntry {
    private int     age,
                    foldtype,
                    database_index;
    private String  sex;
    private Date    date;
    private int[]   folds;
    private double  weight,
                    bodyfat;

    public LogEntry(int age, int[] folds, int foldtype, String sex, double weight, Date date, int index) {
        setup_basic_fields();
        this.date     = date;
        this.database_index = index;
        this.folds    = folds;
        this.foldtype = foldtype;
        this.weight   = weight;
        this.bodyfat  = BodyfatCalculator.calculate(get_foldsum(), foldtype, age, sex);
        calculate_bodyfat();
    }

    public LogEntry() {
        setup_basic_fields();
        this.date     = new Date();
        int[] folds = {10, 10, 10};
        this.folds    = folds;
        this.foldtype = 3;
        this.weight   = 200;
        this.database_index = -1;
        calculate_bodyfat();
    }

    public void set_folds(int[] folds) {
        this.folds = folds;
        calculate_bodyfat();
    }

    public void set_fold(int fold, int measure) {
        this.folds[fold-1] = measure;
        calculate_bodyfat();
    }

    public void set_weight(double weight) {
        this.weight = weight;
        calculate_bodyfat();
    }

    public void set_date(Date date) {
        this.date = date;
    }

    public int get_age() {
        return age;
    }

    public int get_foldtype() {
        return foldtype;
    }

    public int[] get_folds() {
        return folds;
    }

    public String get_folds_string() {
        String folds_string = "";
        for(int f : folds) {
           folds_string += (Integer.toString(f) + ",");
        }

        return folds_string;
    }

    public String get_sex() {
        return sex;
    }

    public double get_weight() {
        return weight;
    }

    public double get_bodyfat_percent(){
        return bodyfat;
    }

    public Date get_date() {
        return date;
    }

    public int get_database_index() { return database_index; }

    private void setup_basic_fields() {
        this.age      = SettingsHelper.get_age();
        this.sex      = SettingsHelper.get_sex();
    }

    private int get_foldsum() {
        int sum = 0;
        for (int f : folds)
            sum += f;
        return sum;
    }

    private void calculate_bodyfat() {
        this.bodyfat = BodyfatCalculator.calculate(get_foldsum(), foldtype, age, sex);
    }
}
