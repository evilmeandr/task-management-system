package com.taskmanager.service.impl;

import com.taskmanager.dto.TaskCreateDto;
import com.taskmanager.model.Notification;
import com.taskmanager.entity.TaskEntity;
import com.taskmanager.model.Task;
import com.taskmanager.service.NotificationService;
import com.taskmanager.service.cache.CacheService;
import com.taskmanager.service.cache.CacheKeyGenerator;
import com.taskmanager.service.messaging.MessageProducer;
import com.taskmanager.service.messaging.TaskCreatedEvent;
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

    @Mock
    private CacheService cacheService;

    @Mock
    private CacheKeyGenerator cacheKeyGenerator;

    @Mock
    private MessageProducer messageProducer;

    @InjectMocks
    private TaskServiceImpl taskService;

    private TaskCreateDto dto;
    private Task testTask;
    private String userId = "00000000-0000-0000-0000-000000000000";

    @BeforeEach
    void setUp() {
        dto = new TaskCreateDto();
        dto.setTitle("Test Task");
        dto.setDescription("Desc");
        dto.setTargetDate(LocalDateTime.now().plusDays(1));
        testTask = Task.builder().userId(userId).title("Test Task").description("Desc").targetDate(dto.getTargetDate()).build();
    }

    @Test
    void createTask_shouldSaveTaskAndSendEvent() {
        when(taskStorage.save(any(TaskEntity.class))).thenAnswer(inv -> (TaskEntity) inv.getArgument(0));
        when(cacheKeyGenerator.generateTasksByUserKey(userId)).thenReturn("tasks:user:" + userId);
        when(cacheKeyGenerator.generatePendingTasksByUserKey(userId)).thenReturn("pending:tasks:user:" + userId);

        Task created = taskService.createTask(userId, dto);

        assertThat(created.getTitle()).isEqualTo("Test Task");
        assertThat(created.getUserId()).isEqualTo(userId);
        verify(taskStorage).save(any(TaskEntity.class));
        verify(messageProducer).sendTaskCreatedEvent(any(TaskCreatedEvent.class));
        verify(cacheService).evict("tasks:user:" + userId);
        verify(cacheService).evict("pending:tasks:user:" + userId);
        verify(notificationService, never()).createNotification(any());
    }

    @Test
    void getAllTasksByUserId_shouldReturnFromStorage() {
        List<TaskEntity> expected = List.of(TaskEntity.builder().userId(java.util.UUID.fromString("00000000-0000-0000-0000-000000000000")).title("X").build());
        when(taskStorage.findByUserId(java.util.UUID.fromString(userId))).thenReturn(expected);

        List<Task> result = taskService.getAllTasksByUserId(userId);

        assertThat(result).hasSize(1);
    }

    @Test
    void getPendingTasksByUserId_shouldReturnFromStorage() {
        List<TaskEntity> expected = List.of(TaskEntity.builder().userId(java.util.UUID.fromString("00000000-0000-0000-0000-000000000000")).title("Pending").build());
        when(taskStorage.findPendingByUserId(java.util.UUID.fromString(userId))).thenReturn(expected);

        List<Task> result = taskService.getPendingTasksByUserId(userId);

        assertThat(result).hasSize(1);
    }

    @Test
    void deleteTask_shouldMarkAsDeleted() {
        String taskId = "00000000-0000-0000-0000-000000000000";
        TaskEntity taskEntity = TaskEntity.builder()
            .id(java.util.UUID.fromString(taskId))
            .userId(java.util.UUID.fromString(userId))
            .title("Test Task")
            .build();
        
        when(taskStorage.findById(java.util.UUID.fromString(taskId))).thenReturn(java.util.Optional.of(taskEntity));
        when(cacheKeyGenerator.generateTasksByUserKey(userId)).thenReturn("tasks:user:" + userId);
        when(cacheKeyGenerator.generatePendingTasksByUserKey(userId)).thenReturn("pending:tasks:user:" + userId);

        taskService.deleteTask(taskId);

        verify(taskStorage).findById(java.util.UUID.fromString(taskId));
        verify(taskStorage).markAsDeleted(any(java.util.UUID.class));
        verify(cacheService).evict("tasks:user:" + userId);
        verify(cacheService).evict("pending:tasks:user:" + userId);
    }
}
