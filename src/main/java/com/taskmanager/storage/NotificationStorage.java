package com.taskmanager.storage;

import com.taskmanager.model.Notification;
import java.util.List;

public interface NotificationStorage {
    Notification save(Notification notification);
    List<Notification> findByUserId(String userId);
    List<Notification> findPendingByUserId(String userId);
}
