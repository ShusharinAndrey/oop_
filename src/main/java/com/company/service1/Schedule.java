package com.company.service1;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;

public class Schedule {
    @JsonDeserialize(as = ArrayList.class, contentAs = Vessel.class)
    private ArrayList<Vessel> mVessels;

    public Schedule() {
    }

    public Schedule(int quantity) {
        mVessels = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            mVessels.add(new Vessel(i));
        }
        mVessels.sort(Vessel::compare);
    }

    public Schedule(Schedule schedule) {
        setVessels(schedule.mVessels);
    }

    public ArrayList<Vessel> getVessels() {
        return mVessels;
    }

    public void setVessels(ArrayList<Vessel> vessels) {
        mVessels = new ArrayList<>();
        for (Vessel vessel : vessels) {
            mVessels.add(new Vessel(vessel));
        }
    }
}
