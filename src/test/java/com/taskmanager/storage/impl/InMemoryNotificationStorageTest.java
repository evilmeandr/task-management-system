package com.taskmanager.storage.impl;

import com.taskmanager.model.Notification;
import com.taskmanager.model.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class InMemoryNotificationStorageTest {

    private InMemoryNotificationStorage storage;
    private Notification unreadNotif;
    private Notification readNotif;
    private String userId = "user123";

    @BeforeEach
    void setUp() {
        storage = new InMemoryNotificationStorage();
        unreadNotif = Notification.create(userId, "Unread msg", NotificationType.TASK_CREATED);
        readNotif = Notification.create(userId, "Read msg", NotificationType.TASK_COMPLETED);
        readNotif.setRead(true);
    }

    @Test
    void save_shouldStoreAndReturnNotification() {
        Notification saved = storage.save(unreadNotif);

        assertThat(saved).isEqualTo(unreadNotif);
    }

    @Test
    void findByUserId_shouldReturnAllNotifications() {
        storage.save(unreadNotif);
        storage.save(readNotif);

        List<Notification> found = storage.findByUserId(userId);

        assertThat(found).hasSize(2).contains(unreadNotif, readNotif);
    }

    @Test
    void findPendingByUserId_shouldReturnOnlyUnread() {
        storage.save(unreadNotif);
        storage.save(readNotif);

        List<Notification> found = storage.findPendingByUserId(userId);

        assertThat(found).hasSize(1).contains(unreadNotif);
    }
}
