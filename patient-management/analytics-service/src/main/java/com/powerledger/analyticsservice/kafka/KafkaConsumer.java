package com.powerledger.analyticsservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "patient", groupId = "analytics-service")
    public void consumeEvent(String event) {
        try {
//            PatientEvent patientEvent = objectMapper.readValue(event, PatientEvent.class);

            System.out.println("Received event by consumer:");
            System.out.println(event);

        } catch (Exception e) {
            log.error("Failed to parse PatientEvent from message: {}", event, e);
        }
    }
}
