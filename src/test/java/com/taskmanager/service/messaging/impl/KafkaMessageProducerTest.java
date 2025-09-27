package com.taskmanager.service.messaging.impl;

import com.taskmanager.service.messaging.TaskCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaMessageProducerTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private KafkaMessageProducer messageProducer;

    private TaskCreatedEvent testEvent;

    @BeforeEach
    void setUp() {
        testEvent = TaskCreatedEvent.builder()
            .userId("user123")
            .taskTitle("Test Task")
            .taskDescription("Test Description")
            .build();
    }

    @Test
    void sendTaskCreatedEvent_shouldSendToKafka() {
        messageProducer.sendTaskCreatedEvent(testEvent);

        verify(kafkaTemplate).send(eq("task-created-events"), eq(testEvent));
    }

    @Test
    void sendTaskCreatedEvent_shouldHandleException() {
        doThrow(new RuntimeException("Kafka error")).when(kafkaTemplate).send(any(), any());

        messageProducer.sendTaskCreatedEvent(testEvent);

        verify(kafkaTemplate).send(eq("task-created-events"), eq(testEvent));
    }
}



