package com.taskmanager.service;

import com.taskmanager.dto.TaskCreateDto;
import com.taskmanager.model.Task;
import java.util.List;

public interface TaskService {
    Task createTask(String userId, TaskCreateDto dto);
    List<Task> getAllTasksByUserId(String userId);
    List<Task> getPendingTasksByUserId(String userId);
    void deleteTask(String taskId);
}
