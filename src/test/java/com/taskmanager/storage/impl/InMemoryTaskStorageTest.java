package com.taskmanager.storage.impl;

import com.taskmanager.entity.TaskEntity;
import com.taskmanager.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class InMemoryTaskStorageTest {

    private InMemoryTaskStorage storage;
    private TaskEntity testTask;
    private TaskEntity completedTask;
    private UUID userId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        storage = new InMemoryTaskStorage();
        testTask = TaskEntity.builder().userId(userId).title("Pending Task").description("Desc").targetDate(LocalDateTime.now().plusDays(1)).status(TaskStatus.PENDING).build();
        completedTask = TaskEntity.builder().userId(userId).title("Completed Task").description("Desc").targetDate(LocalDateTime.now()).status(TaskStatus.COMPLETED).build();
    }

    @Test
    void save_shouldStoreAndReturnTask() {
        TaskEntity saved = storage.save(testTask);

        assertThat(saved).isEqualTo(testTask);
    }

    @Test
    void findByUserId_shouldReturnAllNonDeletedTasks() {
        storage.save(testTask);
        storage.save(completedTask);

        List<TaskEntity> found = storage.findByUserId(userId);

        assertThat(found).hasSize(2).contains(testTask, completedTask);
    }

    @Test
    void findPendingByUserId_shouldReturnOnlyPendingNonDeleted() {
        storage.save(testTask);
        storage.save(completedTask);

        List<TaskEntity> found = storage.findPendingByUserId(userId);

        assertThat(found).hasSize(1).contains(testTask);
    }

    @Test
    void markAsDeleted_shouldHideFromFinds() {
        storage.save(testTask);
        storage.markAsDeleted(testTask.getId());

        List<TaskEntity> found = storage.findByUserId(userId);

        assertThat(found).isEmpty();
    }
}
