package com.taskmanager.service.impl;

import com.taskmanager.config.EntityMapper;
import com.taskmanager.entity.NotificationEntity;
import com.taskmanager.entity.TaskEntity;
import com.taskmanager.model.Task;
import com.taskmanager.model.Notification;
import com.taskmanager.model.NotificationType;
import com.taskmanager.service.TaskService;
import com.taskmanager.service.NotificationService;
import com.taskmanager.storage.TaskStorage;
import com.taskmanager.dto.TaskCreateDto;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskStorage taskStorage;
    private final NotificationService notificationService;
    
    @Override
    public Task createTask(String userId, TaskCreateDto dto) {
        TaskEntity entity = TaskEntity.builder()
            .userId(UUID.fromString(userId))
            .title(dto.getTitle())
            .description(dto.getDescription())
            .targetDate(dto.getTargetDate())
            .build();
        TaskEntity savedTask = taskStorage.save(entity);
        
        // Создаем уведомление о новой задаче
        Notification notification = Notification.builder()
            .userId(userId)
            .message("New task created: " + entity.getTitle())
            .type(NotificationType.TASK_CREATED)
            .build();
        notificationService.createNotification(notification);
        
        return EntityMapper.toModel(savedTask);
    }
    
    @Override
    public List<Task> getAllTasksByUserId(String userId) {
        return taskStorage.findByUserId(UUID.fromString(userId)).stream()
            .map(EntityMapper::toModel)
            .toList();
    }
    
    @Override
    public List<Task> getPendingTasksByUserId(String userId) {
        return taskStorage.findPendingByUserId(UUID.fromString(userId)).stream()
            .map(EntityMapper::toModel)
            .toList();
    }
    
    @Override
    public void deleteTask(String taskId) {
        taskStorage.markAsDeleted(UUID.fromString(taskId));
    }
}
