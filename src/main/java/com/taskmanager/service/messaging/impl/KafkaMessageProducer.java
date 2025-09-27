package com.taskmanager.service.messaging.impl;

import com.taskmanager.service.messaging.MessageProducer;
import com.taskmanager.service.messaging.TaskCreatedEvent;
import com.taskmanager.service.messaging.TaskOverdueEvent;
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
    private static final String TASK_CREATED_TOPIC = "task-created-events";
    private static final String TASK_OVERDUE_TOPIC = "task-overdue-events";
    
    @Override
    public void sendTaskCreatedEvent(TaskCreatedEvent event) {
        try {
            kafkaTemplate.send(TASK_CREATED_TOPIC, event);
            log.debug("Sent task created event to topic {}: {}", TASK_CREATED_TOPIC, event);
        } catch (Exception e) {
            log.error("Failed to send task created event: {}", event, e);
        }
    }
    
    @Override
    public void sendTaskOverdueEvent(TaskOverdueEvent event) {
        try {
            kafkaTemplate.send(TASK_OVERDUE_TOPIC, event);
            log.debug("Sent task overdue event to topic {}: {}", TASK_OVERDUE_TOPIC, event);
        } catch (Exception e) {
            log.error("Failed to send task overdue event: {}", event, e);
        }
    }
}


