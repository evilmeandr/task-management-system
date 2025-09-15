package com.taskmanager.storage;

import com.taskmanager.model.Task;
import java.util.List;

public interface TaskStorage {
    Task save(Task task);
    List<Task> findByUserId(String userId);
    List<Task> findPendingByUserId(String userId);
    void markAsDeleted(String taskId);
}
