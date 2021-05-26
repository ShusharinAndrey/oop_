package com.company.service1;

public class MyDuration extends MyDate {
    public MyDuration(int minute) {
        super(minute);
    }

    public MyDuration(MyDuration date) {
        mDay = date.mDay;
        mHour = date.mHour;
        mMinute = date.mMinute;
    }

    public MyDuration(String string) {
        calculate(Integer.parseInt(string.substring(0, 2)), Integer.parseInt(string.substring(3, 5)), Integer.parseInt(string.substring(6, 8)));
    }

    @Override
    public int getDay() {
        return mDay;
    }
}
