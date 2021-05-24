package com.company.service3;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;

@JsonPropertyOrder({"craneBulk", "craneLiquid", "craneContainer", "penalty", "quantityUnloadingVessel", "maximumUnloadingDelay", "averageUnloadingDelay", "averageWaitingTimeQueue", "averageLengthUnloadingQueue", "offloads"})
public class Statistic {
    public static final int COST_CRANE = 30000;
    @JsonDeserialize(as = ArrayList.class, contentAs = Unloading.class)
    private final ArrayList<Unloading> mOffloads = new ArrayList<>();
    private int mCraneBulk;
    private int mCraneLiquid;
    private int mCraneContainer;
    private int mPenalty = 0;
    private int mAverageLengthUnloadingQueue;
    private int mQuantityLengthUnloadingQueue = 0;
    private int mAverageWaitingTimeQueue;
    private int mQuantityWaitingTimeQueue = 0;
    private int mMaximumUnloadingDelay = 0;
    private int mAverageUnloadingDelay;
    private int mQuantityUnloadingDelay = 0;

    public Statistic(int craneBulk, int craneLiquid, int craneContainer) {
        this.mCraneBulk = craneBulk;
        this.mCraneLiquid = craneLiquid;
        this.mCraneContainer = craneContainer;
        mPenalty = (craneBulk + craneLiquid + craneContainer) * COST_CRANE;
    }

    Statistic() {

    }

    public Statistic(Statistic statistic) {
        mOffloads.addAll(statistic.getOffloads());
        mCraneBulk = statistic.mCraneBulk;
        mCraneLiquid = statistic.mCraneLiquid;
        mCraneContainer = statistic.mCraneContainer;
        mPenalty = statistic.mPenalty;
        mAverageLengthUnloadingQueue = statistic.mAverageLengthUnloadingQueue;
        mQuantityLengthUnloadingQueue = statistic.mQuantityLengthUnloadingQueue;
        mAverageWaitingTimeQueue = statistic.mAverageWaitingTimeQueue;
        mQuantityWaitingTimeQueue = statistic.mQuantityWaitingTimeQueue;
        mMaximumUnloadingDelay = statistic.mMaximumUnloadingDelay;
        mAverageUnloadingDelay = statistic.mAverageUnloadingDelay;
        mQuantityUnloadingDelay = statistic.mQuantityUnloadingDelay;
    }

    public static int compare(Statistic statistic1, Statistic statistic2) {
        return Integer.compare(statistic1.mPenalty, statistic2.mPenalty);
    }

    public int getQuantityUnloadingVessel() {
        return mOffloads.size();
    }

    public void setQuantityUnloadingVessel(int mQuantityUnloadingVessel) {
    }

    public int getPenalty() {
        return mPenalty;
    }

    public void setPenalty(int mPenalty) {
        this.mPenalty = mPenalty;
    }

    public void addPenalty(int penalty) {
        this.mPenalty += penalty;
    }

    public ArrayList<Unloading> getOffloads() {
        return mOffloads;
    }

    public int getAverageLengthUnloadingQueue() {
        return mAverageLengthUnloadingQueue;
    }

    public void setAverageLengthUnloadingQueue(int mAverageLengthUnloadingQueue) {
        this.mAverageLengthUnloadingQueue = mAverageLengthUnloadingQueue;
    }

    public void calculateAverageLengthUnloadingQueue() {
        if (mQuantityLengthUnloadingQueue != 0) {
            mAverageLengthUnloadingQueue = mAverageLengthUnloadingQueue / mQuantityLengthUnloadingQueue;
        } else {
            mAverageLengthUnloadingQueue = 0;
        }
    }

    public void addAverageLengthUnloadingQueue(int averageLengthUnloadingQueue) {
        mQuantityLengthUnloadingQueue++;
        this.mAverageLengthUnloadingQueue += averageLengthUnloadingQueue;
    }

    public int getAverageWaitingTimeQueue() {
        return mAverageWaitingTimeQueue;
    }

    public void setAverageWaitingTimeQueue(int mAverageWaitingTimeQueue) {
        this.mAverageWaitingTimeQueue = mAverageWaitingTimeQueue;
    }

    public void calculateAverageWaitingTimeQueue() {
        if (mQuantityWaitingTimeQueue != 0) {
            mAverageWaitingTimeQueue = mAverageWaitingTimeQueue / mQuantityWaitingTimeQueue;
        } else {
            mAverageWaitingTimeQueue = 0;
        }
    }

    public void calculateAverage() {
        calculateAverageWaitingTimeQueue();
        calculateAverageUnloadingDelay();
        calculateAverageLengthUnloadingQueue();
    }

    public void addAverageWaitingTimeQueue(int averageWaitingTimeQueue) {
        mQuantityWaitingTimeQueue++;
        this.mAverageWaitingTimeQueue += averageWaitingTimeQueue;
    }

    public int getMaximumUnloadingDelay() {
        return mMaximumUnloadingDelay;
    }

    public void setMaximumUnloadingDelay(int mMaximumUnloadingDelay) {
        this.mMaximumUnloadingDelay = mMaximumUnloadingDelay;
    }

    public int getAverageUnloadingDelay() {
        return mAverageUnloadingDelay;
    }

    public void setAverageUnloadingDelay(int mAverageUnloadingDelay) {
        this.mAverageUnloadingDelay = mAverageUnloadingDelay;
    }

    public void calculateAverageUnloadingDelay() {
        if (mQuantityUnloadingDelay != 0 && getQuantityUnloadingVessel() != 0) {
            mAverageUnloadingDelay = mAverageUnloadingDelay / getQuantityUnloadingVessel();
        } else {
            mAverageUnloadingDelay = 0;
        }
    }

    public void addAverageUnloadingDelay(int averageUnloadingDelay) {
        mQuantityUnloadingDelay++;
        mMaximumUnloadingDelay = Math.max(mMaximumUnloadingDelay, averageUnloadingDelay);
        this.mAverageUnloadingDelay += averageUnloadingDelay;
    }

    public int getCraneBulk() {
        return mCraneBulk;
    }

    public void setCraneBulk(int mCraneBulk) {
        this.mCraneBulk = mCraneBulk;
    }

    public int getCraneLiquid() {
        return mCraneLiquid;
    }

    public void setCraneLiquid(int mCraneLiquid) {
        this.mCraneLiquid = mCraneLiquid;
    }

    public int getCraneContainer() {
        return mCraneContainer;
    }

    public void setCraneContainer(int mCraneContainer) {
        this.mCraneContainer = mCraneContainer;
    }

    public void setQuantityLengthUnloadingQueue(int mQuantityLengthUnloadingQueue) {
        this.mQuantityLengthUnloadingQueue = mQuantityLengthUnloadingQueue;
    }

    public void setQuantityWaitingTimeQueue(int mQuantityWaitingTimeQueue) {
        this.mQuantityWaitingTimeQueue = mQuantityWaitingTimeQueue;
    }

    public void setQuantityUnloadingDelay(int mQuantityUnloadingDelay) {
        this.mQuantityUnloadingDelay = mQuantityUnloadingDelay;
    }
}
