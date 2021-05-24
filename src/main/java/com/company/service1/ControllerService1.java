package com.company.service1;

import com.company.service2.Service2;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/service1")
public class ControllerService1 {
    @GetMapping("/schedule")
    public String getSchedule(@RequestParam(value = "quantity", defaultValue = "1000") int quantity) {
        Schedule schedule = new Schedule(quantity);
        return Service2.write(schedule);
    }
}
