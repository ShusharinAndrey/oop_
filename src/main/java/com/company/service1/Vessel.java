package com.company.service1;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Random;

public class Vessel {
    private static final String VESSEL = "Vessel_";
    private MyDate mDateArrival;
    private int mId;
    private Cargo mCargo;

    public Vessel() {
    }

    public Vessel(int numberVessel) {
        Random random = new Random();
        mDateArrival = new MyDate(random.nextInt(MyDate.MAX_TIME));
        mId = numberVessel;
        mCargo = new Cargo(random.nextDouble() * 1000, Cargo.Type.values()[random.nextInt(Cargo.Type.values().length)]);
    }

    public Vessel(MyDate date, int idVessel, Cargo cargo) {
        mDateArrival = date;
        mId = idVessel;
        mCargo = new Cargo(cargo);
    }

    public Vessel(Vessel vessel) {
        mDateArrival = vessel.mDateArrival;
        mId = vessel.mId;
        mCargo = new Cargo(vessel.mCargo);
    }

    public static int compare(Vessel vessel1, Vessel vessel2) {
        return Integer.compare(vessel1.mDateArrival.getDayMinute(), vessel2.mDateArrival.getDayMinute());
    }

    public MyDate getDateArrival() {
        return mDateArrival;
    }

    public void setDateArrival(MyDate arrival) {
        this.mDateArrival = arrival;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public Cargo getCargo() {
        return mCargo;
    }

    public void setCargo(Cargo mCargo) {
        this.mCargo = mCargo;
    }

    @JsonIgnore
    public String getNameVessel() {
        return VESSEL + mId;
    }

}
