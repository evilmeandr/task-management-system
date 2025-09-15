package com.taskmanager.service.impl;

import com.taskmanager.config.EntityMapper;
import com.taskmanager.service.NotificationService;
import com.taskmanager.storage.NotificationStorage;
import com.taskmanager.model.Notification;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationStorage notificationStorage;
    
    @Override
    public List<Notification> getAllNotificationsByUserId(String userId) {
        return notificationStorage.findByUserId(UUID.fromString(userId)).stream()
            .map(EntityMapper::toModel)
            .toList();
    }
    
    @Override
    public List<Notification> getPendingNotificationsByUserId(String userId) {
        return notificationStorage.findPendingByUserId(UUID.fromString(userId)).stream()
            .map(EntityMapper::toModel)
            .toList();
    }
    
    public Notification createNotification(Notification notification) {
        return EntityMapper.toModel(notificationStorage.save(EntityMapper.toEntity(notification)));
    }
}
