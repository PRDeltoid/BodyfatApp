package com.tbritton.bodyfat.bodyfatapp;


public final class BodyfatCalcHelper {
    public static double calculate(int sum, int foldtype, String sex) {
        //Initialize our bodyfat at 0.0 as a default
        double bodyfat = 0.0;
        int age = 20; //hardcode
        if(sex == "Male") {
            switch(foldtype) {
                case 3:
                    //Three Fold
                    //bodyfat = 1.10938 - (0.0008267*sum) + (0.0000016*sum*sum) - (0.0002574*age);
                    bodyfat = 495/(1.10938-(0.0008267*sum)+(0.0000016*sum*sum)-(0.0002574*age))-450;
                    break;
                case 7:
                    //Seven Fold
                    bodyfat = 1.112 - (0.00043499*sum) + (0.00000055*sum*sum) - (0.00028826*age);
                    break;
                default:
                    //Foldtype not supported/found
                    break;
            }
        } else { //sex == "Female"
            switch (foldtype) {
                case 3:
                    //Three Fold
                    //bodyfat = 1.0994921 - (0.0009929*sum) + (0.0000023*sum*sum) - (0.0001392*age);
                    bodyfat = 495/(1.089733-(0.0009245*sum)+(0.0000025*sum*sum)-(0.0000979*age))-450;
                    break;
                case 7:
                    //Seven fold
                    bodyfat = 1.097 - (0.00046971*sum) + (0.00000056*sum*sum) - (0.00012828*age);
                    break;
                default:
                    //Foldtype not supported/found
                    break;
            }
        }
        return bodyfat;
    }
}
