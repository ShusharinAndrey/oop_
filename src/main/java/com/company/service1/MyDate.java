package com.company.service1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"day", "hour", "minute"})
public class MyDate {
    public static final int MAX_MINUTE = 60;
    public static final int MAX_HOUR = 24;
    public static final int MAX_DAY = 30;
    public static final int MAX_TIME = MAX_DAY * MAX_HOUR * MAX_MINUTE;
    protected static final String SEPARATOR = ":";
    private static final int MIN_MINUTE = 0;
    private static final int MIN_HOUR = 0;
    private static final int MIN_DAY = 0;
    protected int mDay;
    protected int mHour;
    protected int mMinute;

    public MyDate() {
    }

    public MyDate(int minute) {
        this(MIN_DAY, MIN_HOUR, minute);
    }

    public MyDate(int day, int hour, int minute) {
        calculate(minute, hour, day);
    }

    public MyDate(String string) {
        calculate(Integer.parseInt(string.substring(0, 2)), Integer.parseInt(string.substring(3, 5)), Integer.parseInt(string.substring(6, 8)));
    }

    public MyDate(MyDate date) {
        mDay = date.mDay;
        mHour = date.mHour;
        mMinute = date.mMinute;
    }

    @JsonIgnore
    public int getDayMinute() {
        return (mDay * MAX_HOUR + mHour) * MAX_MINUTE + mMinute;
    }

    public int getDay() {
        return mDay;
    }

    private void setDay(int day) {
        if (day < getMinDay()) {
            day = getMinDay();
        } else if (day >= MAX_DAY) {
            day = day % MAX_DAY;
        }

        this.mDay = day;
    }

    private int getHour() {
        return mHour;
    }

    public void setHour(int hour) {
        calculate(hour, mDay);
    }

    public int getMinute() {
        return mMinute;
    }

    public void setMinute(int minute) {
        calculate(minute, mHour, mDay);
    }

    protected void calculate(int minute, int hour, int day) {
        if (minute < MIN_MINUTE) {
            minute = MIN_MINUTE;
        } else if (minute >= MAX_MINUTE) {
            hour += minute / MAX_MINUTE;
            minute = minute % MAX_MINUTE;
        }

        this.mMinute = minute;


        calculate(hour, day);
    }

    private void calculate(int hour, int day) {

        if (hour < MIN_HOUR) {
            hour = MIN_HOUR;
        } else if (hour >= MAX_HOUR) {
            day += hour / MAX_HOUR;
            hour = hour % MAX_HOUR;
        }

        this.mHour = hour;

        setDay(day);
    }

    protected int getMinDay() {
        return MIN_DAY;
    }

    @Override
    public String toString() {
        return "%02d%s%02d%s%02d".formatted(getDay(), SEPARATOR, mHour, SEPARATOR, mMinute);
    }

}
