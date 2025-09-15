package com.taskmanager.service.impl;

import com.taskmanager.service.TaskService;
import com.taskmanager.service.NotificationService;
import com.taskmanager.storage.TaskStorage;
import com.taskmanager.dto.TaskCreateDto;
import com.taskmanager.model.Task;
import com.taskmanager.model.Notification;
import com.taskmanager.model.NotificationType;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskStorage taskStorage;
    private final NotificationService notificationService;
    
    @Override
    public Task createTask(String userId, TaskCreateDto dto) {
        Task task = Task.create(userId, dto.getTitle(), dto.getDescription(), dto.getTargetDate());
        Task savedTask = taskStorage.save(task);
        
        // Создаем уведомление о новой задаче
        Notification notification = Notification.create(
            userId, 
            "New task created: " + task.getTitle(), 
            NotificationType.TASK_CREATED
        );
        notificationService.createNotification(notification);
        
        return savedTask;
    }
    
    @Override
    public List<Task> getAllTasksByUserId(String userId) {
        return taskStorage.findByUserId(userId);
    }
    
    @Override
    public List<Task> getPendingTasksByUserId(String userId) {
        return taskStorage.findPendingByUserId(userId);
    }
    
    @Override
    public void deleteTask(String taskId) {
        taskStorage.markAsDeleted(taskId);
    }
}
