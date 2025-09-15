package com.taskmanager.service.impl;

import com.taskmanager.model.Notification;
import com.taskmanager.model.NotificationType;
import com.taskmanager.entity.NotificationEntity;
import com.taskmanager.storage.NotificationStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationStorage notificationStorage;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private Notification testNotif;
    private String userId = "00000000-0000-0000-0000-000000000000";

    @BeforeEach
    void setUp() {
        testNotif = Notification.builder().userId(userId).message("Test msg").type(NotificationType.TASK_CREATED).read(false).build();
    }

    @Test
    void getAllNotificationsByUserId_shouldReturnFromStorage() {
        List<NotificationEntity> expected = List.of(NotificationEntity.builder().message("Test msg").build());
        when(notificationStorage.findByUserId(java.util.UUID.fromString(userId))).thenReturn(expected);

        List<Notification> result = notificationService.getAllNotificationsByUserId(userId);

        assertThat(result).hasSize(1);
    }

    @Test
    void getPendingNotificationsByUserId_shouldReturnFromStorage() {
        List<NotificationEntity> expected = List.of(NotificationEntity.builder().message("Test msg").build());
        when(notificationStorage.findPendingByUserId(java.util.UUID.fromString(userId))).thenReturn(expected);

        List<Notification> result = notificationService.getPendingNotificationsByUserId(userId);

        assertThat(result).hasSize(1);
    }

    @Test
    void createNotification_shouldSaveAndReturn() {
        when(notificationStorage.save(any(NotificationEntity.class))).thenReturn(NotificationEntity.builder().message("Test msg").build());

        Notification created = notificationService.createNotification(testNotif);

        assertThat(created.getMessage()).isEqualTo("Test msg");
    }
}
