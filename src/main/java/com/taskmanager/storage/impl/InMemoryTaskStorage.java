package com.taskmanager.storage.impl;

import com.taskmanager.storage.TaskStorage;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class InMemoryTaskStorage implements TaskStorage {
    private final Map<String, Task> tasks = new ConcurrentHashMap<>();
    
    @Override
    public Task save(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }
    
    @Override
    public List<Task> findByUserId(String userId) {
        return tasks.values().stream()
            .filter(task -> task.getUserId().equals(userId) && !task.isDeleted())
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Task> findPendingByUserId(String userId) {
        return tasks.values().stream()
            .filter(task -> task.getUserId().equals(userId) 
                && !task.isDeleted() 
                && task.getStatus() == TaskStatus.PENDING)
            .collect(Collectors.toList());
    }
    
    @Override
    public void markAsDeleted(String taskId) {
        Task task = tasks.get(taskId);
        if (task != null) {
            task.setDeleted(true);
            tasks.put(taskId, task);
        }
    }
}
