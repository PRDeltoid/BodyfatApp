package com.tbritton.bodyfat.bodyfatapp;


public final class BodyfatCalculator {
    public static double calculate(int sum, int foldtype, int age, String sex) {
        //Initialize our bodyfat at 0.0 as a default
        double bodyfat = 0.0;
        if(sex.equals("Male")) {
            switch(foldtype) {
                case 3:
                    //Three Fold
                    bodyfat = 495/(1.10938-(0.0008267*sum)+(0.0000016*sum*sum)-(0.0002574*age))-450;
                    break;
                default:
                    //Foldtype not supported/found
                    break;
            }
        } else { //sex == "Female"
            switch (foldtype) {
                case 3:
                    //Three Fold
                    bodyfat = 495/(1.089733-(0.0009245*sum)+(0.0000025*sum*sum)-(0.0000979*age))-450;
                    break;
                default:
                    //Foldtype not supported/found
                    break;
            }
        }
        return bodyfat;
    }
}
