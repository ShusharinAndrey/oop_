package com.company.service3;

import com.company.service1.MyDate;
import com.company.service1.Schedule;
import com.company.service1.Vessel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Analysis {
    public static final int MAX_DELAY_ARRIVALS = 7 * MyDate.MAX_HOUR * MyDate.MAX_MINUTE;
    private final ArrayList<Statistic> statistics = new ArrayList<>();
    public LinkedList<Vessel> queueBulk = new LinkedList<>();
    public LinkedList<Vessel> queueLiquid = new LinkedList<>();
    public LinkedList<Vessel> queueContainer = new LinkedList<>();

    public Analysis(Schedule schedule) {
        Random random = new Random();

        for (Vessel vessel : schedule.getVessels()) {
            vessel.getDateArrival().setMinute(vessel.getDateArrival().getMinute()
                    + random.nextInt(MAX_DELAY_ARRIVALS * 2) - MAX_DELAY_ARRIVALS);
        }

        schedule.getVessels().sort(Vessel::compare);
        for (Vessel vessel : schedule.getVessels()) {
            switch (vessel.getCargo().getType()) {
                case BULK -> queueBulk.add(new Vessel(vessel));
                case LIQUID -> queueLiquid.add(new Vessel(vessel));
                case CONTAINER -> queueContainer.add(new Vessel(vessel));
            }
        }
    }

    public void start() {

        for (int craneBulk = 1; craneBulk < 10; craneBulk++) {
            for (int craneLiquid = 1; craneLiquid < 10; craneLiquid++) {
                for (int craneContainer = 1; craneContainer < 10; craneContainer++) {
                    Statistic statistic = new Statistic(craneBulk, craneLiquid, craneContainer);

                    new CraneQueue(craneBulk, queueBulk, statistic).run();
                    new CraneQueue(craneLiquid, queueLiquid, statistic).run();
                    new CraneQueue(craneContainer, queueContainer, statistic).run();
                    statistic.calculateAverage();
                    statistics.add(statistic);
                    //   System.out.println(craneBulk + " " + craneLiquid + " " + craneContainer);
                }
            }
        }

    }

    public Statistic getStatistic() {
        statistics.sort(Statistic::compare);
        statistics.get(0).getOffloads().sort(Unloading::compare);
        return statistics.get(0);
    }
}
