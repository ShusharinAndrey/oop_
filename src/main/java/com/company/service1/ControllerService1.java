package com.company.service1;

import com.company.service2.Service2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
@RequestMapping("/service1")
public class ControllerService1 {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ControllerService1.class);
        Map<String, Object> map = new HashMap<>();
        map.put("server.port", "8180");
        map.put("server.host", "localhost");
        app.setDefaultProperties(map);
        app.run();
    }

    @GetMapping("/schedule")
    public String getSchedule(@RequestParam(value = "quantity", defaultValue = "1000") int quantity) {
        Schedule schedule = new Schedule(quantity);
        return Service2.write(schedule);
    }

    @RequestMapping("")
    public String service1() {
        return """
                <!DOCTYPE HTML>
                <html lang="ru">
                <head>
                    <title>Порт</title>
                    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
                </head>
                <body>

                <p><b><label>Первый сервис</label></b>

                <p><a href="/service1/schedule">Создать расписание на 1000 кораблей</a></p>

                <form action="/service1/schedule" method="GET" id="quantityForm">
                    <div>
                        <label for="quantityField">Создать расписание на</label>
                        <input name="quantity" id="quantityField">
                        <label for="quantityField">кораблей</label>
                        <button>Создать</button>
                    </div>
                </form>
                </p>
                </body>
                </html>""";
    }
}
