package com.example.demo.controller;

import com.example.demo.mqtt.MqttService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/door")
@RequiredArgsConstructor
@CrossOrigin("*") // Cho phép FE gọi API
public class DoorController {

    private final MqttService mqtt;

    @GetMapping("/open")
    public String openDoor() {
        mqtt.sendCommand("open");
        return "Opening door";
    }

    @GetMapping("/close")
    public String closeDoor() {
        mqtt.sendCommand("close");
        return "Closing door";
    }

    @GetMapping("/status")
    public String getStatus() {
        return mqtt.getDoorStatus();
    }

    @GetMapping("/distance")
    public int getDistance() {
        return mqtt.getDistance();
    }

    // ======= API QUAN TRỌNG: FE DÙNG /door/data =======
    @GetMapping("/data")
    public Map<String, Object> getAllData() {

        Map<String, Object> map = new HashMap<>();

        map.put("status", mqtt.getDoorStatus());
        map.put("distance", mqtt.getDistance());

        return map;
    }
}
