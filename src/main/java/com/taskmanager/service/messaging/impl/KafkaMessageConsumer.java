package com.taskmanager.service.messaging.impl;

import com.taskmanager.service.messaging.MessageConsumer;
import com.taskmanager.service.messaging.TaskCreatedEvent;
import com.taskmanager.service.messaging.TaskOverdueEvent;
import com.taskmanager.service.NotificationService;
import com.taskmanager.model.Notification;
import com.taskmanager.model.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("kafka")
public class KafkaMessageConsumer implements MessageConsumer {
    
    private final NotificationService notificationService;
    
    @KafkaListener(topics = "task-created-events", groupId = "task-manager-group")
    public void consume(TaskCreatedEvent event) {
        try {
            log.debug("Received task created event: {}", event);
            
            Notification notification = Notification.builder()
                .id(java.util.UUID.randomUUID().toString())
                .userId(event.getUserId())
                .message("New task created: " + event.getTaskTitle())
                .type(NotificationType.TASK_CREATED)
                .read(false)
                .createdAt(java.time.LocalDateTime.now())
                .build();
            
            notificationService.createNotification(notification);
            log.debug("Created notification for user {}: {}", event.getUserId(), notification.getMessage());
            
        } catch (Exception e) {
            log.error("Failed to process task created event: {}", event, e);
        }
    }
    
    @Override
    public void consumeTaskCreatedEvent(TaskCreatedEvent event) {
        consume(event);
    }
    
    @KafkaListener(topics = "task-overdue-events", groupId = "task-manager-group")
    public void consume(TaskOverdueEvent event) {
        try {
            log.debug("Received task overdue event: {}", event);
            
            Notification notification = Notification.builder()
                .id(java.util.UUID.randomUUID().toString())
                .userId(event.getUserId())
                .message("Task overdue: " + event.getTaskTitle())
                .type(NotificationType.TASK_OVERDUE)
                .read(false)
                .createdAt(java.time.LocalDateTime.now())
                .build();
            
            notificationService.createNotification(notification);
            log.debug("Created overdue notification for user: {}", event.getUserId());
        } catch (Exception e) {
            log.error("Failed to process task overdue event: {}", event, e);
        }
    }
    
    @Override
    public void consumeTaskOverdueEvent(TaskOverdueEvent event) {
        consume(event);
    }
}
