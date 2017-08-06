package com.tbritton.bodyfat.bodyfatapp;


public class LogEntry {
    final private int     age,
                          foldtype,
                          database_index;
    final private int[]   folds;
    final private String  sex,
                          date;
    final private double  weight,
                          bodyfat;

    public LogEntry(int age, int[] folds, int foldtype, String sex, double weight, String date) {
        int sum = get_foldsum(folds);

        this.age      = age;
        this.folds    = folds;
        this.foldtype = foldtype;
        this.sex      = sex;
        this.weight   = weight;
        this.bodyfat  = BodyfatCalculator.calculate(sum, foldtype, age, sex);
        this.date     = date;
        this.database_index = -1;
    }

    public LogEntry(int age, int[] folds, int foldtype, String sex, double weight, String date, int index) {
        int sum = get_foldsum(folds);

        this.age      = age;
        this.folds    = folds;
        this.foldtype = foldtype;
        this.sex      = sex;
        this.weight   = weight;
        this.bodyfat  = BodyfatCalculator.calculate(sum, foldtype, age, sex);
        this.date     = date;
        this.database_index = index;
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

    public int get_foldsum(int[] folds) {
        int sum = 0;
        for (int f : folds)
            sum += f;
        return sum;
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

    public String get_date() {
        return date;
    }

    public int get_database_index() { return database_index; }
}
