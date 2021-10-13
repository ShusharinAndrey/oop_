package com.company.service3;

import com.company.service1.Schedule;
import com.company.service2.Service2;
import com.company.service3.Analysis;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
public class Service3 {
    public static final String PATH_1 = "http://localhost:8180/service1/";
    private static final String PATH_2 = "http://localhost:8280/service2/";

    public static void main(String[] args) {
        Schedule schedule = new Schedule();
        RestTemplateBuilder builder = new RestTemplateBuilder();
        RestTemplate restTemplate = builder.build();
        String stringSchedule = restTemplate.getForObject(PATH_2 + "scheduleJson", String.class);
        Service2.read(stringSchedule, schedule);
        Analysis analysis = new Analysis(schedule);
        analysis.start();

        try {
            restTemplate.postForObject(PATH_2 + "statistic",
                    new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(analysis.getStatistic()), String.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
