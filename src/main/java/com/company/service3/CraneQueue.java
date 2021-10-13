package com.company.service3;

import com.company.service1.MyDate;
import com.company.service1.MyDuration;
import com.company.service1.Vessel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CraneQueue {
    public static final int PENALTY_HOUR_DOWNTIME = 100;
    private final int mQuantity;
    private final ArrayList<Thread> mThreads;
    private final ArrayList<Crane> mCranes;
    private final LinkedList<Crane> mNotFreeCranes;
    private final LinkedList<Vessel> mSchedule;
    private final LinkedList<Vessel> mQueueVessel;
    private final Statistic mStatistic;
    private final CyclicBarrier BARRIER;
    private volatile Integer mGlobalTime = 0;

    public CraneQueue(int quantity, LinkedList<Vessel> schedule, Statistic statistic) {
        BARRIER = new CyclicBarrier(quantity, new Clock());

        mStatistic = statistic;
        mCranes = new ArrayList<>();
        mNotFreeCranes = new LinkedList<>();
        mThreads = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            mCranes.add(new Crane());
            mThreads.add(new Thread(mCranes.get(i)));
        }

        mQuantity = quantity;

        mSchedule = new LinkedList<>();
        for (Vessel vessel : schedule) {
            mSchedule.add(new Vessel(vessel));
        }

        mQueueVessel = new LinkedList<>();
    }


    public void run() {
        for (int i = 0; i < mQuantity; i++) {
            mThreads.get(i).start();
        }

        for (int i = 0; i < mQuantity; i++) {
            try {
                mThreads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        endTime();
    }


    private void endTime() {
//        System.out.println(mSchedule.size() + mQueueVessel.size());
        for (Vessel vessel : mSchedule) {
            addStatistic(vessel);
        }
        for (Vessel vessel : mQueueVessel) {
            addStatistic(vessel);
        }
    }

    private void addStatistic(Vessel vessel) {
        int waitingOffload = Math.abs(MyDate.MAX_TIME - vessel.getDateArrival().getDayMinute());
        mStatistic.addPenalty((waitingOffload / MyDate.MAX_MINUTE) * PENALTY_HOUR_DOWNTIME);
        mStatistic.addAverageWaitingTimeQueue(waitingOffload);
    }


    private class Clock implements Runnable {
        @Override
        public void run() {
            synchronized (mGlobalTime) {
                int minNextTime = mCranes.get(0).getEndTime();
                for (int i = 1; i < mQuantity; i++) {
                    minNextTime = Math.min(minNextTime, mCranes.get(i).getEndTime());
                }
                mGlobalTime = minNextTime + 1;
                //           System.out.println("Clock"+Thread.currentThread().getName());
                int timeArrivalVessels = 0;
                synchronized (mSchedule) {
//                    System.out.println(mGlobalTime);
                    Vessel vessel;
                    while (timeArrivalVessels < mGlobalTime && mSchedule.size() > 0) {
                        vessel = mSchedule.peekFirst();
                        timeArrivalVessels = vessel.getDateArrival().getDayMinute();
                        if (timeArrivalVessels < mGlobalTime) {
                            mQueueVessel.addLast(mSchedule.pollFirst());
                        }
                    }
                    synchronized (mStatistic) {
                        mStatistic.addAverageLengthUnloadingQueue(mQueueVessel.size());
                    }
                }
                for (int i = 0; i < mQuantity; i++) {
                    mCranes.get(i).setTime(mGlobalTime);
                }
            }
        }
    }

    public class Crane implements Runnable {
        public static final int MAX_DELAY_OFFLOADS = 1440;
        private volatile int mDelay;
        private volatile String mNameVessel;
        private volatile MyDate mDateArrival;
        private volatile int mUnloadingTime;
        private volatile int mDuration;
        private volatile int mWaitingOffload;
        private volatile boolean mIsFirst;
        private volatile boolean mIsFree;
        private volatile int mTime;

        Crane() {
            mTime = 0;
            mIsFree = true;
            mIsFirst = false;
        }

        private synchronized void add(Vessel vessel) {
            Random random = new Random();
            mIsFirst = true;
            mDuration = 0;
            if (random.nextInt(100) > 80) {
                mDelay = random.nextInt(MAX_DELAY_OFFLOADS);
            } else {
                mDelay = 0;
            }
            synchronized (mStatistic) {
                mStatistic.addAverageUnloadingDelay(mDelay);
                mStatistic.addPenalty((mDelay / MyDate.MAX_MINUTE) * PENALTY_HOUR_DOWNTIME);
            }
//                System.out.println(mTime + " " + vessel.getDateArrival().getDayMinute());
            if (mTime > vessel.getDateArrival().getDayMinute()) {
                mWaitingOffload = mTime - vessel.getDateArrival().getDayMinute();
            } else {
                mTime = vessel.getDateArrival().getDayMinute();
            }
            mNameVessel = vessel.getNameVessel();
            mDateArrival = new MyDate(vessel.getDateArrival());
            mUnloadingTime = vessel.getCargo().getUnloadingTime().getDayMinute();
        }

        private void delay_() {
//            for (int i = 0; i < 10000; i++) {
//                System.out.println(i);
//            }
        }

        @Override
        public void run() {
            do {
                if (!Thread.interrupted()) {

                    if (!mIsFree) {
                        try {
                            delay_();
                            //                   System.out.println(Thread.currentThread().getName());
                            BARRIER.await();
                        } catch (InterruptedException | BrokenBarrierException e) {
//                            e.printStackTrace();
                        }
                    } else {
//                        System.out.println(Thread.currentThread().getName()+" find vessel1");
                        synchronized (mSchedule) {
                            if (mSchedule.size() > 0 || mQueueVessel.size() > 0) {
//                                    System.out.println(Thread.currentThread().getName()+" find vessel2");
                                synchronized (mNotFreeCranes) {
                                    delay_();
                                    if (mNotFreeCranes.size() > 0) {
                                        mIsFirst = false;
                                        Crane crane = mNotFreeCranes.pollFirst();
                                        crane.setSecond();
                                    } else {
                                        Vessel vessel;
                                        if (mQueueVessel.size() > 0) {
                                            vessel = mQueueVessel.pollFirst();
                                        } else {
                                            vessel = mSchedule.pollFirst();
                                        }
                                        add(vessel);
                                        mNotFreeCranes.add(this);
                                    }
                                    mIsFree = false;
                                }
                            }
                        }
                    }
                } else {
//                    System.out.println("Stop with interrupt");
                    return;
                }
            }
            while ((mQueueVessel.size() > 0 || mSchedule.size() > 0) && mGlobalTime < MyDate.MAX_TIME);
//            System.out.println("stop1");
            BARRIER.reset();
//            System.out.println("stop2");
        }

        private synchronized void setFree() {
            if (mIsFirst) {
                synchronized (mStatistic) {
                    mTime = getEndTime();
                    mStatistic.addPenalty((mWaitingOffload / MyDate.MAX_MINUTE) * PENALTY_HOUR_DOWNTIME);
                    mStatistic.getOffloads().add(new Unloading(mNameVessel, mDateArrival, new MyDuration(mWaitingOffload), new MyDuration(mDuration + mDelay + mUnloadingTime)));
                    mStatistic.addAverageWaitingTimeQueue(mWaitingOffload);
                    mIsFirst = false;
                    mIsFree = true;
                    synchronized (mNotFreeCranes) {
                        mNotFreeCranes.removeIf(crane -> crane == this);
                    }
                }
            }
            mIsFree = true;
//            System.out.print("true "+ mIsFirst+" ");
        }

        private synchronized void setSecond() {
            mUnloadingTime /= 2;
        }

        private synchronized void setTime(int time) {
            int delta = time - mTime;
            if (delta > 0) {
                if (delta >= mUnloadingTime) {
                    delta -= mUnloadingTime;
                    mDuration += mUnloadingTime;
                    mTime += mUnloadingTime;
                    mUnloadingTime = 0;
                    if (delta >= mDelay) {
                        mDuration += mDelay;
                        mTime += mDelay;
                        mDelay = 0;
                        setFree();
                    } else {
                        mDelay -= delta;
                        mDuration += delta;
                        mTime += delta;
                    }

                } else {
                    mUnloadingTime -= delta;
                    mDuration += delta;
                    mTime += delta;
                }
            }
        }

        private synchronized int getUnloadingTime() {
            return mUnloadingTime + mDelay;
        }

        private synchronized int getEndTime() {
            if (mIsFirst) {
                return getUnloadingTime() + mTime;
            } else {
                return mTime;
            }
        }
    }

}
