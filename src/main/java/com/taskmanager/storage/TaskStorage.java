package com.taskmanager.storage;

import com.taskmanager.entity.TaskEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskStorage {
    TaskEntity save(TaskEntity task);
    Optional<TaskEntity> findById(UUID taskId);
    List<TaskEntity> findByUserId(UUID userId);
    List<TaskEntity> findPendingByUserId(UUID userId);
    void markAsDeleted(UUID taskId);
}
