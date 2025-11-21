package com.example.demo.mqtt;

import lombok.Getter;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Service;

@Service
public class MqttService {

    private final String broker = "tcp://broker.emqx.io:1883";
    private final String clientId = "JavaBackend";

    private MqttClient client;

    @Getter
    private String doorStatus = "unknown";

    @Getter
    private int distance = 0;

    public MqttService() {
        try {
            client = new MqttClient(broker, clientId);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);

            client.connect(options);

            client.subscribe("home/door/status", this::handleStatus);
            client.subscribe("home/door/distance", this::handleDistance);

            System.out.println("Java Backend connected to MQTT Cloud!");

        } catch (Exception e) { e.printStackTrace(); }
    }

    private void handleStatus(String topic, MqttMessage msg) {
        doorStatus = msg.toString();
        System.out.println("Status: " + doorStatus);
    }

    private void handleDistance(String topic, MqttMessage msg) {
        distance = Integer.parseInt(msg.toString());
        System.out.println("Distance: " + distance);
    }

    public void sendCommand(String cmd) {
        try {
            client.publish("home/door/cmd", new MqttMessage(cmd.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
