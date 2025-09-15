package com.taskmanager.service.impl;

import com.taskmanager.dto.TaskCreateDto;
import com.taskmanager.model.Notification;
import com.taskmanager.model.Task;
import com.taskmanager.service.NotificationService;
import com.taskmanager.storage.TaskStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskStorage taskStorage;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private TaskServiceImpl taskService;

    private TaskCreateDto dto;
    private Task testTask;
    private String userId = "user123";

    @BeforeEach
    void setUp() {
        dto = new TaskCreateDto();
        dto.setTitle("Test Task");
        dto.setDescription("Desc");
        dto.setTargetDate(LocalDateTime.now().plusDays(1));
        testTask = Task.create(userId, "Test Task", "Desc", dto.getTargetDate());
    }

    @Test
    void createTask_shouldSaveTaskAndCreateNotification() {
        when(taskStorage.save(any(Task.class))).thenReturn(testTask);
        Notification mockNotification = Notification.builder().id("test").build();
        when(notificationService.createNotification(any(Notification.class))).thenReturn(mockNotification);

        Task created = taskService.createTask(userId, dto);

        assertThat(created).isEqualTo(testTask);
        verify(taskStorage).save(any(Task.class));
        verify(notificationService).createNotification(any(Notification.class));
    }

    @Test
    void getAllTasksByUserId_shouldReturnFromStorage() {
        List<Task> expected = List.of(testTask);
        when(taskStorage.findByUserId(userId)).thenReturn(expected);

        List<Task> result = taskService.getAllTasksByUserId(userId);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void getPendingTasksByUserId_shouldReturnFromStorage() {
        List<Task> expected = List.of(testTask);
        when(taskStorage.findPendingByUserId(userId)).thenReturn(expected);

        List<Task> result = taskService.getPendingTasksByUserId(userId);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void deleteTask_shouldMarkAsDeleted() {
        String taskId = "task123";

        taskService.deleteTask(taskId);

        verify(taskStorage).markAsDeleted(taskId);
    }
}
