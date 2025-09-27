package com.taskmanager.service.impl;

import com.taskmanager.entity.NotificationEntity;
import com.taskmanager.model.Notification;
import com.taskmanager.model.NotificationType;
import com.taskmanager.service.NotificationService;
import com.taskmanager.service.cache.CacheKeyGenerator;
import com.taskmanager.service.cache.CacheService;
import com.taskmanager.storage.NotificationStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.annotation.Async;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplAsyncTest {

    @Mock
    private NotificationStorage notificationStorage;

    @Mock
    private CacheService cacheService;

    @Mock
    private CacheKeyGenerator cacheKeyGenerator;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void createNotification_shouldBeAsync() throws NoSuchMethodException {
        Method method = NotificationServiceImpl.class.getMethod("createNotification", Notification.class);
        
        assertThat(method.isAnnotationPresent(Async.class)).isTrue();
        assertThat(method.getAnnotation(Async.class).value()).isEqualTo("taskExecutor");
    }

    @Test
    void createNotification_shouldExecuteAsync() {
        Notification notification = Notification.builder()
            .id(UUID.randomUUID().toString())
            .userId(UUID.randomUUID().toString())
            .message("Test notification")
            .type(NotificationType.TASK_CREATED)
            .read(false)
            .createdAt(LocalDateTime.now())
            .build();

        NotificationEntity entity = NotificationEntity.builder()
            .id(UUID.fromString(notification.getId()))
            .userId(UUID.fromString(notification.getUserId()))
            .message(notification.getMessage())
            .type(notification.getType())
            .read(notification.isRead())
            .createdAt(notification.getCreatedAt())
            .build();

        when(notificationStorage.save(any(NotificationEntity.class))).thenReturn(entity);

        Notification result = notificationService.createNotification(notification);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(notification.getId());
        assertThat(result.getMessage()).isEqualTo(notification.getMessage());
    }
}
