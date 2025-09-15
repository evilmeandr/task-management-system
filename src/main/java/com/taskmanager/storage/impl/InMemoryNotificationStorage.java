package com.taskmanager.storage.impl;

import com.taskmanager.storage.NotificationStorage;
import com.taskmanager.model.Notification;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class InMemoryNotificationStorage implements NotificationStorage {
    private final Map<String, Notification> notifications = new ConcurrentHashMap<>();
    
    @Override
    public Notification save(Notification notification) {
        notifications.put(notification.getId(), notification);
        return notification;
    }
    
    @Override
    public List<Notification> findByUserId(String userId) {
        return notifications.values().stream()
            .filter(notification -> notification.getUserId().equals(userId))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Notification> findPendingByUserId(String userId) {
        return notifications.values().stream()
            .filter(notification -> notification.getUserId().equals(userId) && !notification.isRead())
            .collect(Collectors.toList());
    }
}
