package com.taskmanager.service;

import com.taskmanager.model.Notification;
import java.util.List;

public interface NotificationService {
    List<Notification> getAllNotificationsByUserId(String userId);
    List<Notification> getPendingNotificationsByUserId(String userId);
    Notification createNotification(Notification notification);
}
