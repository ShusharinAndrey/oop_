package com.company.service1;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Cargo {
    private double mWeight;
    private Type mType;
    private MyDuration mUnloadingTime;

    public Cargo() {
    }

    public Cargo(double weight, Type type) {
        this.mWeight = weight;
        this.mType = type;
        calculateUnloadingTime();
    }

    public Cargo(Cargo cargo) {
        mWeight = cargo.mWeight;
        mType = cargo.mType;
        calculateUnloadingTime();
    }

    public double getWeight() {
        return mWeight;
    }

    public void setWeight(double mWeight) {
        this.mWeight = mWeight;
        calculateUnloadingTime();
    }

    public Type getType() {
        return mType;
    }

    public void setType(Type mType) {
        this.mType = mType;
        calculateUnloadingTime();
    }

    @JsonIgnore
    public MyDuration getUnloadingTime() {
        return mUnloadingTime;
    }

    private void calculateUnloadingTime() {
        mUnloadingTime = new MyDuration((int) ((mType.ordinal() + 10) * mWeight / 10));
    }

    public enum Type {BULK, LIQUID, CONTAINER}
}
