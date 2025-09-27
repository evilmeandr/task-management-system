package com.taskmanager.service.scheduling.impl;

import com.taskmanager.entity.TaskEntity;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.service.messaging.MessageProducer;
import com.taskmanager.service.messaging.TaskOverdueEvent;
import com.taskmanager.service.scheduling.OverdueTaskChecker;
import com.taskmanager.storage.TaskStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduledOverdueCheckerTest {

    @Mock
    private TaskStorage taskStorage;

    @Mock
    private MessageProducer messageProducer;

    @InjectMocks
    private ScheduledOverdueChecker overdueChecker;

    private TaskEntity overdueTask;
    private TaskEntity pendingTask;
    private TaskEntity completedTask;

    @BeforeEach
    void setUp() {
        UUID userId = UUID.randomUUID();
        UUID taskId1 = UUID.randomUUID();
        UUID taskId2 = UUID.randomUUID();
        UUID taskId3 = UUID.randomUUID();

        overdueTask = TaskEntity.builder()
            .id(taskId1)
            .userId(userId)
            .title("Overdue Task")
            .description("This task is overdue")
            .status(TaskStatus.PENDING)
            .targetDate(LocalDateTime.now().minusDays(1))
            .deleted(false)
            .build();

        pendingTask = TaskEntity.builder()
            .id(taskId2)
            .userId(userId)
            .title("Pending Task")
            .description("This task is still pending")
            .status(TaskStatus.PENDING)
            .targetDate(LocalDateTime.now().plusDays(1))
            .deleted(false)
            .build();

        completedTask = TaskEntity.builder()
            .id(taskId3)
            .userId(userId)
            .title("Completed Task")
            .description("This task is completed")
            .status(TaskStatus.COMPLETED)
            .targetDate(LocalDateTime.now().minusDays(1))
            .deleted(false)
            .build();
    }

    @Test
    void checkOverdueTasks_shouldSendEventForOverdueTasks() {
        when(taskStorage.findAllPendingTasks()).thenReturn(List.of(overdueTask, pendingTask, completedTask));

        overdueChecker.checkOverdueTasks();

        verify(messageProducer, atLeastOnce()).sendTaskOverdueEvent(argThat(event ->
            event.getUserId().equals(overdueTask.getUserId().toString()) &&
            event.getTaskId().equals(overdueTask.getId().toString()) &&
            event.getTaskTitle().equals(overdueTask.getTitle())
        ));
    }

    @Test
    void checkOverdueTasks_shouldNotSendEventForNonOverdueTasks() {
        when(taskStorage.findAllPendingTasks()).thenReturn(List.of(pendingTask));

        overdueChecker.checkOverdueTasks();

        verify(messageProducer, never()).sendTaskOverdueEvent(any(TaskOverdueEvent.class));
    }

    @Test
    void checkOverdueTasks_shouldHandleEmptyTaskList() {
        when(taskStorage.findAllPendingTasks()).thenReturn(List.of());

        overdueChecker.checkOverdueTasks();

        verify(messageProducer, never()).sendTaskOverdueEvent(any(TaskOverdueEvent.class));
    }

    @Test
    void checkOverdueTasks_shouldHandleException() {
        when(taskStorage.findAllPendingTasks()).thenThrow(new RuntimeException("Database error"));

        overdueChecker.checkOverdueTasks();

        verify(messageProducer, never()).sendTaskOverdueEvent(any(TaskOverdueEvent.class));
    }
}
