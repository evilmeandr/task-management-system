package com.taskmanager.storage.impl;

import com.taskmanager.entity.NotificationEntity;
import com.taskmanager.model.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class InMemoryNotificationStorageTest {

    private InMemoryNotificationStorage storage;
    private NotificationEntity unreadNotif;
    private NotificationEntity readNotif;
    private UUID userId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        storage = new InMemoryNotificationStorage();
        unreadNotif = NotificationEntity.builder().userId(userId).message("Unread msg").type(NotificationType.TASK_CREATED).read(false).build();
        readNotif = NotificationEntity.builder().userId(userId).message("Read msg").type(NotificationType.TASK_COMPLETED).read(true).build();
    }

    @Test
    void save_shouldStoreAndReturnNotification() {
        NotificationEntity saved = storage.save(unreadNotif);

        assertThat(saved).isEqualTo(unreadNotif);
    }

    @Test
    void findByUserId_shouldReturnAllNotifications() {
        storage.save(unreadNotif);
        storage.save(readNotif);

        List<NotificationEntity> found = storage.findByUserId(userId);

        assertThat(found).hasSize(2).contains(unreadNotif, readNotif);
    }

    @Test
    void findPendingByUserId_shouldReturnOnlyUnread() {
        storage.save(unreadNotif);
        storage.save(readNotif);

        List<NotificationEntity> found = storage.findPendingByUserId(userId);

        assertThat(found).hasSize(1).contains(unreadNotif);
    }
}
