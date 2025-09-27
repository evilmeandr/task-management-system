package com.taskmanager.service.messaging.impl;

import com.taskmanager.model.Notification;
import com.taskmanager.model.NotificationType;
import com.taskmanager.service.NotificationService;
import com.taskmanager.service.messaging.TaskCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaMessageConsumerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private KafkaMessageConsumer messageConsumer;

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
    void consume_shouldCreateNotification() {
        messageConsumer.consume(testEvent);

        verify(notificationService).createNotification(any(Notification.class));
    }

    @Test
    void consume_shouldHandleException() {
        doThrow(new RuntimeException("Database error")).when(notificationService).createNotification(any());

        messageConsumer.consume(testEvent);

        verify(notificationService).createNotification(any(Notification.class));
    }

    @Test
    void consumeTaskCreatedEvent_shouldCallConsume() {
        messageConsumer.consumeTaskCreatedEvent(testEvent);

        verify(notificationService).createNotification(any(Notification.class));
    }
}




