package com.taskmanager.service.impl;

import com.taskmanager.service.NotificationService;
import com.taskmanager.storage.NotificationStorage;
import com.taskmanager.model.Notification;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationStorage notificationStorage;
    
    @Override
    public List<Notification> getAllNotificationsByUserId(String userId) {
        return notificationStorage.findByUserId(userId);
    }
    
    @Override
    public List<Notification> getPendingNotificationsByUserId(String userId) {
        return notificationStorage.findPendingByUserId(userId);
    }
    
    public Notification createNotification(Notification notification) {
        return notificationStorage.save(notification);
    }
}
