package com.taskmanager.storage;

import com.taskmanager.entity.NotificationEntity;
import java.util.List;
import java.util.UUID;

public interface NotificationStorage {
    NotificationEntity save(NotificationEntity notification);
    List<NotificationEntity> findByUserId(UUID userId);
    List<NotificationEntity> findPendingByUserId(UUID userId);
}
