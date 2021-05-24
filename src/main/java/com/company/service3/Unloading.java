package com.company.service3;

import com.company.service1.MyDate;
import com.company.service1.MyDuration;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"name", "arrival", "waiting", "beginning", "duration"})
public class Unloading {
    private String mName;
    private MyDate mArrival;
    private MyDuration mWaiting;
    private MyDuration mDuration;

    Unloading() {

    }

    public Unloading(String name, MyDate arrival, MyDuration waiting, MyDuration myDuration) {
        mName = name;
        mArrival = arrival;
        mWaiting = waiting;
        mDuration = myDuration;
    }

    public static int compare(Unloading unloading1, Unloading unloading2) {
        return Integer.compare(unloading1.mArrival.getDayMinute() + unloading1.mWaiting.getDayMinute(), unloading2.mArrival.getDayMinute() + unloading2.mWaiting.getDayMinute());
    }

    public String getName() {
        return mName;
    }

    public String getArrival() {
        return mArrival.toString();
    }

    public String getBeginning() {
        return new MyDate(mArrival.getDayMinute() + mWaiting.getDayMinute()).toString();
    }

    public String getWaiting() {
        return mWaiting.toString();
    }

    public String getDuration() {
        return mDuration.toString();
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setArrival(String mArrival) {
        this.mArrival = new MyDate(mArrival);
    }

    public void setWaiting(String mWaiting) {
        this.mWaiting = new MyDuration(mWaiting);
    }

    public void setDuration(String mDuration) {
        this.mDuration = new MyDuration(mDuration);
    }

    public void setBeginning(String beginning) {
    }

}
