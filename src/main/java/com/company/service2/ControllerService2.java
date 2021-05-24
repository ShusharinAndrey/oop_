package com.company.service2;

import com.company.OopApplication;
import com.company.service1.Schedule;
import com.company.service3.Statistic;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping(value = "/service2")
public class ControllerService2 {

    @GetMapping("/scheduleJson")
    public String getScheduleJson(@RequestParam(value = "quantity", defaultValue = "1000") int quantity) {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        RestTemplate restTemplate = builder.build();
        String stringSchedule = restTemplate.getForObject(OopApplication.PATH + "service1/schedule?quantity=" + quantity, String.class);

        Schedule schedule = new Schedule();
        Service2.read(stringSchedule, schedule);
        Service2.writeJson("Schedule", schedule);

        return stringSchedule;
    }

    @GetMapping("/scheduleJsonByName")
    public String getScheduleJsonByName(@RequestParam(value = "nameFile", defaultValue = "Schedule") String filename) {
        Schedule schedule = new Schedule();
        Service2.readJson(filename, schedule);
        return Service2.write(schedule);
    }

    @PostMapping("/statistic")
    public void sendStatisticJson(@RequestBody String str) {
        Statistic statistics;
        statistics = Service2.read(str);
        Service2.writeJson("statistic", statistics);
    }
}
