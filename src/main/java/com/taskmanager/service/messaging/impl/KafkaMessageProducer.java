package com.taskmanager.service.messaging.impl;

import com.taskmanager.service.messaging.MessageProducer;
import com.taskmanager.service.messaging.TaskCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("kafka")
public class KafkaMessageProducer implements MessageProducer {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "task-created-events";
    
    @Override
    public void sendTaskCreatedEvent(TaskCreatedEvent event) {
        try {
            kafkaTemplate.send(TOPIC, event);
            log.debug("Sent task created event to topic {}: {}", TOPIC, event);
        } catch (Exception e) {
            log.error("Failed to send task created event: {}", event, e);
        }
    }
}


