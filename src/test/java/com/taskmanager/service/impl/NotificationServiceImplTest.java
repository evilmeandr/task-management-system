package com.taskmanager.service.impl;

import com.taskmanager.model.Notification;
import com.taskmanager.model.NotificationType;
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
    private String userId = "user123";

    @BeforeEach
    void setUp() {
        testNotif = Notification.create(userId, "Test msg", NotificationType.TASK_CREATED);
    }

    @Test
    void getAllNotificationsByUserId_shouldReturnFromStorage() {
        List<Notification> expected = List.of(testNotif);
        when(notificationStorage.findByUserId(userId)).thenReturn(expected);

        List<Notification> result = notificationService.getAllNotificationsByUserId(userId);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void getPendingNotificationsByUserId_shouldReturnFromStorage() {
        List<Notification> expected = List.of(testNotif);
        when(notificationStorage.findPendingByUserId(userId)).thenReturn(expected);

        List<Notification> result = notificationService.getPendingNotificationsByUserId(userId);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void createNotification_shouldSaveAndReturn() {
        when(notificationStorage.save(any(Notification.class))).thenReturn(testNotif);

        Notification created = notificationService.createNotification(testNotif);

        assertThat(created).isEqualTo(testNotif);
    }
}
