package com.taskmanager.storage.impl;

import com.taskmanager.entity.NotificationEntity;
import com.taskmanager.storage.NotificationStorage;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.UUID;

@Component
@Profile("test")
public class InMemoryNotificationStorage implements NotificationStorage {
    private final Map<UUID, NotificationEntity> notifications = new ConcurrentHashMap<>();
    
    @Override
    public NotificationEntity save(NotificationEntity notification) {
        UUID id = notification.getId() != null ? notification.getId() : UUID.randomUUID();
        notification.setId(id);
        notifications.put(id, notification);
        return notification;
    }
    
    @Override
    public List<NotificationEntity> findByUserId(UUID userId) {
        return notifications.values().stream()
            .filter(notification -> notification.getUserId().equals(userId))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<NotificationEntity> findPendingByUserId(UUID userId) {
        return notifications.values().stream()
            .filter(notification -> notification.getUserId().equals(userId) && !notification.isRead())
            .collect(Collectors.toList());
    }
}
