package com.taskmanager.integration;

import com.taskmanager.BaseIntegrationTest;
import com.taskmanager.entity.TaskEntity;
import com.taskmanager.entity.UserEntity;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TaskRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindPendingAndExcludeDeleted() {
        UserEntity user = userRepository.save(UserEntity.builder()
                .username("task_user")
                .email("task_user@example.com")
                .build());
        UUID userId = user.getId();

        TaskEntity pending = TaskEntity.builder()
                .userId(userId)
                .title("T1")
                .description("D1")
                .status(TaskStatus.PENDING)
                .creationDate(LocalDateTime.now())
                .targetDate(LocalDateTime.now().plusDays(1))
                .deleted(false)
                .build();
        TaskEntity completed = TaskEntity.builder()
                .userId(userId)
                .title("T2")
                .description("D2")
                .status(TaskStatus.COMPLETED)
                .creationDate(LocalDateTime.now())
                .targetDate(LocalDateTime.now().plusDays(1))
                .deleted(false)
                .build();
        TaskEntity deleted = TaskEntity.builder()
                .userId(userId)
                .title("T3")
                .description("D3")
                .status(TaskStatus.PENDING)
                .creationDate(LocalDateTime.now())
                .targetDate(LocalDateTime.now().plusDays(1))
                .deleted(true)
                .build();

        taskRepository.save(pending);
        taskRepository.save(completed);
        taskRepository.save(deleted);

        List<TaskEntity> all = taskRepository.findByUserIdAndDeletedFalse(userId);
        assertThat(all).extracting(TaskEntity::getTitle).contains("T1", "T2").doesNotContain("T3");

        List<TaskEntity> pendings = taskRepository.findPendingByUserIdAndDeletedFalse(userId, TaskStatus.PENDING);
        assertThat(pendings).extracting(TaskEntity::getTitle).containsExactly("T1");
    }
}


