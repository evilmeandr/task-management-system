package com.taskmanager.integration;

import com.taskmanager.BaseIntegrationTest;
import com.taskmanager.entity.NotificationEntity;
import com.taskmanager.entity.UserEntity;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.model.NotificationType;
import com.taskmanager.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindAllAndPending() {
        UserEntity user = userRepository.save(UserEntity.builder()
                .username("notif_user")
                .email("notif_user@example.com")
                .build());
        UUID userId = user.getId();
        NotificationEntity unread = NotificationEntity.builder()
                .userId(userId)
                .message("Unread")
                .type(NotificationType.TASK_CREATED)
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();
        NotificationEntity read = NotificationEntity.builder()
                .userId(userId)
                .message("Read")
                .type(NotificationType.TASK_COMPLETED)
                .read(true)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(unread);
        notificationRepository.save(read);

        List<NotificationEntity> all = notificationRepository.findByUserId(userId);
        assertThat(all).hasSize(2);

        List<NotificationEntity> pending = notificationRepository.findPendingByUserId(userId);
        assertThat(pending).hasSize(1).extracting(NotificationEntity::getMessage).containsExactly("Unread");
    }
}


