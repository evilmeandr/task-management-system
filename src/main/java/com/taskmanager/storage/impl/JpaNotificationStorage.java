package com.taskmanager.storage.impl;

import com.taskmanager.entity.NotificationEntity;
import com.taskmanager.repository.NotificationRepository;
import com.taskmanager.storage.NotificationStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.UUID;

@Component
@Profile({"dev","postgres","redis"})
@RequiredArgsConstructor
public class JpaNotificationStorage implements NotificationStorage {
    private final NotificationRepository notificationRepository;

    @Override
    public NotificationEntity save(NotificationEntity notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public List<NotificationEntity> findByUserId(UUID userId) {
        return notificationRepository.findByUserId(userId);
    }

    @Override
    public List<NotificationEntity> findPendingByUserId(UUID userId) {
        return notificationRepository.findPendingByUserId(userId);
    }
}


