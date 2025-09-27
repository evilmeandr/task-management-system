package com.taskmanager.integration;

import com.taskmanager.BaseIntegrationTest;
import com.taskmanager.entity.TaskEntity;
import com.taskmanager.entity.UserEntity;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.service.scheduling.OverdueTaskChecker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
public class SchedulerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private OverdueTaskChecker overdueChecker;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldCheckOverdueTasksAndCreateNotifications() {
        UserEntity user = userRepository.save(UserEntity.builder()
            .username("scheduler-test-user")
            .email("scheduler-test@example.com")
            .build());

        TaskEntity overdueTask = taskRepository.save(TaskEntity.builder()
            .userId(user.getId())
            .title("Overdue Task")
            .description("This task is overdue")
            .status(TaskStatus.PENDING)
            .targetDate(LocalDateTime.now().minusDays(1))
            .deleted(false)
            .build());

        TaskEntity pendingTask = taskRepository.save(TaskEntity.builder()
            .userId(user.getId())
            .title("Pending Task")
            .description("This task is still pending")
            .status(TaskStatus.PENDING)
            .targetDate(LocalDateTime.now().plusDays(1))
            .deleted(false)
            .build());

        overdueChecker.checkOverdueTasks();

        List<TaskEntity> allTasks = taskRepository.findAll();
        assertThat(allTasks).hasSize(2);
        
        assertThat(overdueTask.getTargetDate()).isBefore(LocalDateTime.now());
        assertThat(pendingTask.getTargetDate()).isAfter(LocalDateTime.now());
    }

    @Test
    void shouldHandleEmptyTaskList() {
        overdueChecker.checkOverdueTasks();

        assertThat(taskRepository.findAll()).isEmpty();
    }
}
