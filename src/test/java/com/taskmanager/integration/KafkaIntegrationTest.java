package com.taskmanager.integration;

import com.taskmanager.BaseIntegrationTest;
import com.taskmanager.entity.NotificationEntity;
import com.taskmanager.model.NotificationType;
import com.taskmanager.repository.NotificationRepository;
import com.taskmanager.service.TaskService;
import com.taskmanager.service.UserService;
import com.taskmanager.dto.TaskCreateDto;
import com.taskmanager.dto.UserRegistrationDto;
import com.taskmanager.model.User;
import com.taskmanager.service.messaging.TaskCreatedEvent;
import com.taskmanager.service.messaging.impl.KafkaMessageProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
public class KafkaIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    void shouldCreateNotificationAsyncViaKafka() throws InterruptedException {
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setUsername("kafka-test-user");
        userDto.setEmail("kafka-test@example.com");
        User user = userService.register(userDto);
        String userId = user.getId();

        TaskCreateDto taskDto = new TaskCreateDto();
        taskDto.setTitle("Kafka Integration Test Task");
        taskDto.setDescription("Test Description");
        taskDto.setTargetDate(LocalDateTime.now().plusDays(1));

        taskService.createTask(userId, taskDto);

        Thread.sleep(2000);

        List<NotificationEntity> notifications = notificationRepository.findByUserId(UUID.fromString(userId));
        assertThat(notifications).hasSize(1);
        
        NotificationEntity notification = notifications.get(0);
        assertThat(notification.getUserId().toString()).isEqualTo(userId);
        assertThat(notification.getMessage()).contains("Kafka Integration Test Task");
        assertThat(notification.getType()).isEqualTo(NotificationType.TASK_CREATED);
        assertThat(notification.isRead()).isFalse();
    }
}
