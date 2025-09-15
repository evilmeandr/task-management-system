package com.taskmanager.storage.impl;

import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class InMemoryTaskStorageTest {

    private InMemoryTaskStorage storage;
    private Task testTask;
    private Task completedTask;
    private String userId = "user123";

    @BeforeEach
    void setUp() {
        storage = new InMemoryTaskStorage();
        testTask = Task.create(userId, "Pending Task", "Desc", LocalDateTime.now().plusDays(1));
        completedTask = Task.create(userId, "Completed Task", "Desc", LocalDateTime.now());
        completedTask.setStatus(TaskStatus.COMPLETED);
    }

    @Test
    void save_shouldStoreAndReturnTask() {
        Task saved = storage.save(testTask);

        assertThat(saved).isEqualTo(testTask);
    }

    @Test
    void findByUserId_shouldReturnAllNonDeletedTasks() {
        storage.save(testTask);
        storage.save(completedTask);

        List<Task> found = storage.findByUserId(userId);

        assertThat(found).hasSize(2).contains(testTask, completedTask);
    }

    @Test
    void findPendingByUserId_shouldReturnOnlyPendingNonDeleted() {
        storage.save(testTask);
        storage.save(completedTask);

        List<Task> found = storage.findPendingByUserId(userId);

        assertThat(found).hasSize(1).contains(testTask);
    }

    @Test
    void markAsDeleted_shouldHideFromFinds() {
        storage.save(testTask);
        storage.markAsDeleted(testTask.getId());

        List<Task> found = storage.findByUserId(userId);

        assertThat(found).isEmpty();
    }
}
