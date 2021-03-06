package com.tbritton.bodyfat.bodyfatapp;


final class BodyfatCalculator {
    public static double calculate(int sum, int foldtype, int age, String sex) {
        double bodyfat = 0.0; //0.0 by default
        switch(foldtype) {
            case 3:
                bodyfat = three_fold(sum, age, sex);
                break;
            case 5:
                bodyfat = five_fold(sum, age, sex);
                break;
            case 7:
                bodyfat = seven_fold(sum, age, sex);
            default:
                break; //Foldtype not found or supported
        }
        return bodyfat;
    }

    private static double three_fold(int sum, int age, String sex) {
        double bodyfat;
        if(sex.equals("Male")) {
            bodyfat = 495 / (1.10938- (0.0008267 * sum) + (0.0000016 * sum * sum) - (0.0002574 * age)) - 450;
        } else {
            bodyfat = 495 / (1.089733 - (0.0009245 * sum) + (0.0000025 * sum * sum) - (0.0000979 * age)) - 450;
        }
        return bodyfat;
    }

    private static double five_fold(int sum, int age, String sex) {
        return 10.0;
    }

    private static double seven_fold(int sum, int age, String sex) {
        return 10.0;
    }
}
