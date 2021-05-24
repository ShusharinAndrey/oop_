package com.company.service2;

import com.company.service1.Schedule;
import com.company.service3.Statistic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;

public class Service2 {

    public static void writeJson(String nameFile, Schedule schedule) {
        String filepath = System.getProperty("user.dir") + File.separator + nameFile + ".json";
        try {
            new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(new FileOutputStream(filepath), schedule);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeJson(String nameFile, Statistic statistic) {
        String filepath = System.getProperty("user.dir") + File.separator + nameFile + ".json";
        try {
            new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(new FileOutputStream(filepath), statistic);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Statistic read(String src) {
        try {
            return new Statistic(new ObjectMapper().readValue(src, Statistic.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeConsole(Schedule schedule) {
        System.out.println(write(schedule));
    }

    public static String write(Schedule schedule) {
        try {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(schedule);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void readJson(String nameFile, Schedule schedule) {
        String filepath = System.getProperty("user.dir") + File.separator + nameFile + ".json";
        try {
            read(new FileInputStream(filepath), schedule);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void readConsole(Schedule schedule) {
        read(System.in, schedule);
    }

//    public static void readString(Schedule schedule) {
//        read(System.in, schedule);
//    }

    public static void read(InputStream src, Schedule schedule) {
        if (schedule.getVessels() == null) {
            schedule.setVessels(new ArrayList<>());
        }
        try {
            schedule.getVessels().addAll(new ObjectMapper().readValue(src, Schedule.class).getVessels());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void read(String src, Schedule schedule) {
        if (schedule.getVessels() == null) {
            schedule.setVessels(new ArrayList<>());
        }
        try {
            schedule.getVessels().addAll(new ObjectMapper().readValue(src, Schedule.class).getVessels());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
