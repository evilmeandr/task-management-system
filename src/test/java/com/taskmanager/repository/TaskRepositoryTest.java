package com.taskmanager.repository;

import com.taskmanager.entity.TaskEntity;
import com.taskmanager.model.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void save_and_findByUserIdAndDeletedFalse() {
        UUID userId = UUID.randomUUID();
        TaskEntity t1 = TaskEntity.builder().userId(userId).title("T1").targetDate(LocalDateTime.now().plusDays(1)).build();
        TaskEntity t2 = TaskEntity.builder().userId(userId).title("T2").targetDate(LocalDateTime.now().plusDays(2)).status(TaskStatus.COMPLETED).build();
        taskRepository.save(t1);
        taskRepository.save(t2);

        List<TaskEntity> found = taskRepository.findByUserIdAndDeletedFalse(userId);
        assertThat(found).hasSize(2);
    }

    @Test
    void findPendingByUserIdAndDeletedFalse_onlyPending() {
        UUID userId = UUID.randomUUID();
        TaskEntity pending = TaskEntity.builder().userId(userId).title("P").targetDate(LocalDateTime.now().plusDays(1)).status(TaskStatus.PENDING).build();
        TaskEntity done = TaskEntity.builder().userId(userId).title("D").targetDate(LocalDateTime.now().plusDays(1)).status(TaskStatus.COMPLETED).build();
        taskRepository.save(pending);
        taskRepository.save(done);

        List<TaskEntity> found = taskRepository.findPendingByUserIdAndDeletedFalse(userId, TaskStatus.PENDING);
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getStatus()).isEqualTo(TaskStatus.PENDING);
    }

    @Test
    void markAsDeleted_shouldHideFromFinds() {
        UUID userId = UUID.randomUUID();
        TaskEntity t = taskRepository.save(TaskEntity.builder().userId(userId).title("T").targetDate(LocalDateTime.now().plusDays(1)).build());

        taskRepository.markAsDeleted(t.getId());

        List<TaskEntity> found = taskRepository.findByUserIdAndDeletedFalse(userId);
        assertThat(found).isEmpty();
    }
}


