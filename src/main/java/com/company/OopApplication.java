package com.company;

import com.company.service1.Schedule;
import com.company.service2.Service2;
import com.company.service3.Analysis;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
public class OopApplication {
    public static final String PATH = "http://localhost:8080/";

    public static void main(String[] args) {
        SpringApplication.run(OopApplication.class, args);

        Schedule schedule = new Schedule();
        RestTemplateBuilder builder = new RestTemplateBuilder();
        RestTemplate restTemplate = builder.build();
        String stringSchedule = restTemplate.getForObject(PATH + "service2/scheduleJson" , String.class);
        Service2.read(stringSchedule, schedule);
        Analysis analysis = new Analysis(schedule);
        analysis.start();

        try {
            restTemplate.postForObject(PATH + "service2/statistic",
                    new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(analysis.getStatistic()), String.class );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
