package com.company.service2;

import com.company.service1.Schedule;
import com.company.service3.Statistic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.company.service3.Service3.PATH_1;

@SpringBootApplication
@RestController
@RequestMapping(value = "/service2")
public class ControllerService2 {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ControllerService2.class);
        Map<String, Object> map = new HashMap<>();
        map.put("server.port", "8280");
        map.put("server.host", "localhost");
        app.setDefaultProperties(map);
        app.run();
    }

    @GetMapping("/scheduleJson")
    public String getScheduleJson(@RequestParam(value = "quantity", defaultValue = "1000") int quantity) {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        RestTemplate restTemplate = builder.build();
        String stringSchedule = restTemplate.getForObject(PATH_1 + "schedule?quantity=" + quantity, String.class);

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

    @RequestMapping("")
    public String service2() {
        return """
                <!DOCTYPE HTML>
                <html lang="ru">
                <head>
                    <title>Порт</title>
                    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
                </head>
                <body>

                <b><label>Второй сервис</label>
                    <p><a href="/service2/scheduleJson">Создать расписание на 1000 кораблей и записать его в json file "Schedule"</a></p>
                    <form action="/service2/scheduleJson" method="GET" id="quantityFormJson">
                        <div>
                            <label for="quantityFieldJson">Создать расписание на</label>
                            <input name="quantity" id="quantityFieldJson">
                            <label for="quantityFieldJson">кораблей</label>
                            <button>Создать</button>
                        </div>
                    </form>
                    </p>
                </b>
                <p><a href="/service2/scheduleJsonByName">Прочитать расписание из json file "Schedule"</a></p>
                <form action="/service2/scheduleJsonByName" method="GET" id="quantityFormJsonByName">
                    <div>
                        <label for="quantityFieldJsonByName">Прочитать расписание из json file</label>
                        <input name="nameFile" id="quantityFieldJsonByName">
                        <button>Прочитать</button>
                    </div>
                </form>
                </p>
                </b>
                </body>
                </html>""";
    }
}
