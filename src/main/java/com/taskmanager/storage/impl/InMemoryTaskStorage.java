package com.taskmanager.storage.impl;

import com.taskmanager.entity.TaskEntity;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.storage.TaskStorage;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.UUID;

@Component
@Profile("test")
public class InMemoryTaskStorage implements TaskStorage {
    private final Map<UUID, TaskEntity> tasks = new ConcurrentHashMap<>();
    
    @Override
    public TaskEntity save(TaskEntity task) {
        UUID id = task.getId() != null ? task.getId() : UUID.randomUUID();
        task.setId(id);
        tasks.put(id, task);
        return task;
    }
    
    @Override
    public List<TaskEntity> findByUserId(UUID userId) {
        return tasks.values().stream()
            .filter(task -> task.getUserId().equals(userId) && !task.isDeleted())
            .collect(Collectors.toList());
    }
    
    @Override
    public List<TaskEntity> findPendingByUserId(UUID userId) {
        return tasks.values().stream()
            .filter(task -> task.getUserId().equals(userId) 
                && !task.isDeleted() 
                && task.getStatus() == TaskStatus.PENDING)
            .collect(Collectors.toList());
    }
    
    @Override
    public void markAsDeleted(UUID taskId) {
        TaskEntity task = tasks.get(taskId);
        if (task != null) {
            task.setDeleted(true);
            tasks.put(taskId, task);
        }
    }
}
