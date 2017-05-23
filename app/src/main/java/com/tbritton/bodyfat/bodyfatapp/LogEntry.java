package com.tbritton.bodyfat.bodyfatapp;

public class LogEntry {
    public LogEntry(int age, int sum, int foldtype, String sex, double weight, String date) {
        this.age      = age;
        this.sum      = sum;
        this.foldtype = foldtype;
        this.sex      = sex;
        this.weight   = weight;
        this.bodyfat_percent = BodyfatCalcHelper.calculate(sum, foldtype, sex);
        this.date     = date;
    }

    private int     age,
                    foldtype,
                    sum;
    private String  sex,
                    date;
    private double  weight,
                    bodyfat_percent;

    public int get_age() {
        return age;
    }

    public int get_foldtype() {
        return foldtype;
    }

    public int get_sum() {
        return sum;
    }

    public String get_sex() {
        return sex;
    }

    public double get_weight() {
        return weight;
    }

    public double get_bodyfat_percent() {
        return bodyfat_percent;
    }

    public String get_date() {
        return date;
    }
}