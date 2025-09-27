package com.taskmanager.service.impl;

import com.taskmanager.config.EntityMapper;
import com.taskmanager.service.NotificationService;
import com.taskmanager.service.cache.CacheService;
import com.taskmanager.service.cache.CacheKeyGenerator;
import com.taskmanager.storage.NotificationStorage;
import com.taskmanager.model.Notification;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationStorage notificationStorage;
    private final CacheService cacheService;
    private final CacheKeyGenerator cacheKeyGenerator;
    
    @Override
    @Cacheable(value = "notifications", key = "'notifications:user:' + #userId")
    public List<Notification> getAllNotificationsByUserId(String userId) {
        log.debug("Getting all notifications for user: {} from database", userId);
        
        List<Notification> notifications = notificationStorage.findByUserId(UUID.fromString(userId)).stream()
            .map(EntityMapper::toModel)
            .toList();
        
        log.debug("Found {} notifications for user: {}", notifications.size(), userId);
        return notifications;
    }
    
    @Override
    @Cacheable(value = "notifications", key = "'pending:notifications:user:' + #userId")
    public List<Notification> getPendingNotificationsByUserId(String userId) {
        log.debug("Getting pending notifications for user: {} from database", userId);
        
        List<Notification> notifications = notificationStorage.findPendingByUserId(UUID.fromString(userId)).stream()
            .map(EntityMapper::toModel)
            .toList();
        
        log.debug("Found {} pending notifications for user: {}", notifications.size(), userId);
        return notifications;
    }
    
    @Override
    @Async("taskExecutor")
    @CachePut(value = "notifications", key = "#result.id")
    public Notification createNotification(Notification notification) {
        log.debug("Creating notification for user: {}", notification.getUserId());
        
        Notification savedNotification = EntityMapper.toModel(notificationStorage.save(EntityMapper.toEntity(notification)));
        
        String notificationsByUserKey = cacheKeyGenerator.generateNotificationsByUserKey(notification.getUserId());
        String pendingNotificationsKey = cacheKeyGenerator.generatePendingNotificationsByUserKey(notification.getUserId());
        cacheService.evict(notificationsByUserKey);
        cacheService.evict(pendingNotificationsKey);
        
        log.debug("Notification created successfully with ID: {}", savedNotification.getId());
        return savedNotification;
    }
}
