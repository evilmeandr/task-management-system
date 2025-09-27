package com.taskmanager.service.impl;

import com.taskmanager.config.EntityMapper;
import com.taskmanager.entity.TaskEntity;
import com.taskmanager.model.Task;
import com.taskmanager.model.Notification;
import com.taskmanager.model.NotificationType;
import com.taskmanager.service.TaskService;
import com.taskmanager.service.NotificationService;
import com.taskmanager.service.cache.CacheService;
import com.taskmanager.service.cache.CacheKeyGenerator;
import com.taskmanager.storage.TaskStorage;
import com.taskmanager.dto.TaskCreateDto;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskStorage taskStorage;
    private final NotificationService notificationService;
    private final CacheService cacheService;
    private final CacheKeyGenerator cacheKeyGenerator;
    
    @Override
    @CachePut(value = "tasks", key = "#result.id")
    public Task createTask(String userId, TaskCreateDto dto) {
        log.debug("Creating new task for user: {}", userId);
        
        TaskEntity entity = TaskEntity.builder()
            .userId(UUID.fromString(userId))
            .title(dto.getTitle())
            .description(dto.getDescription())
            .targetDate(dto.getTargetDate())
            .build();
        TaskEntity savedTask = taskStorage.save(entity);
    
        String tasksByUserKey = cacheKeyGenerator.generateTasksByUserKey(userId);
        String pendingTasksKey = cacheKeyGenerator.generatePendingTasksByUserKey(userId);
        cacheService.evict(tasksByUserKey);
        cacheService.evict(pendingTasksKey);
        
        Notification notification = Notification.builder()
            .userId(userId)
            .message("New task created: " + entity.getTitle())
            .type(NotificationType.TASK_CREATED)
            .build();
        notificationService.createNotification(notification);
        
        Task result = EntityMapper.toModel(savedTask);
        log.debug("Task created successfully with ID: {}", result.getId());
        return result;
    }
    
    @Override
    @Cacheable(value = "tasks", key = "'tasks:user:' + #userId")
    public List<Task> getAllTasksByUserId(String userId) {
        log.debug("Getting all tasks for user: {} from database", userId);
        
        List<Task> tasks = taskStorage.findByUserId(UUID.fromString(userId)).stream()
            .map(EntityMapper::toModel)
            .toList();
        
        log.debug("Found {} tasks for user: {}", tasks.size(), userId);
        return tasks;
    }
    
    @Override
    @Cacheable(value = "tasks", key = "'pending:tasks:user:' + #userId")
    public List<Task> getPendingTasksByUserId(String userId) {
        log.debug("Getting pending tasks for user: {} from database", userId);
        
        List<Task> tasks = taskStorage.findPendingByUserId(UUID.fromString(userId)).stream()
            .map(EntityMapper::toModel)
            .toList();
        
        log.debug("Found {} pending tasks for user: {}", tasks.size(), userId);
        return tasks;
    }
    
    @Override
    @CacheEvict(value = "tasks", key = "#taskId")
    public void deleteTask(String taskId) {
        log.debug("Deleting task: {}", taskId);
        
        TaskEntity taskEntity = taskStorage.findById(UUID.fromString(taskId))
            .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));
        
        String userId = taskEntity.getUserId().toString();
        
        taskStorage.markAsDeleted(UUID.fromString(taskId));
        
        String tasksByUserKey = cacheKeyGenerator.generateTasksByUserKey(userId);
        String pendingTasksKey = cacheKeyGenerator.generatePendingTasksByUserKey(userId);
        cacheService.evict(tasksByUserKey);
        cacheService.evict(pendingTasksKey);
        
        log.debug("Task {} deleted successfully", taskId);
    }
}
