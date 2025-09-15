package com.taskmanager.repository;

import com.taskmanager.entity.NotificationEntity;
import com.taskmanager.model.NotificationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    void save_and_findByUserId() {
        UUID userId = UUID.randomUUID();
        notificationRepository.save(NotificationEntity.builder().userId(userId).message("A").type(NotificationType.TASK_CREATED).build());
        notificationRepository.save(NotificationEntity.builder().userId(userId).message("B").type(NotificationType.TASK_COMPLETED).read(true).build());

        List<NotificationEntity> all = notificationRepository.findByUserId(userId);
        assertThat(all).hasSize(2);
    }

    @Test
    void findPendingByUserId_onlyUnread() {
        UUID userId = UUID.randomUUID();
        notificationRepository.save(NotificationEntity.builder().userId(userId).message("Unread").type(NotificationType.TASK_CREATED).read(false).build());
        notificationRepository.save(NotificationEntity.builder().userId(userId).message("Read").type(NotificationType.TASK_COMPLETED).read(true).build());

        List<NotificationEntity> pending = notificationRepository.findPendingByUserId(userId);
        assertThat(pending).hasSize(1);
        assertThat(pending.get(0).isRead()).isFalse();
    }
}


