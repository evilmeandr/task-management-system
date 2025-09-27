package com.taskmanager.storage.impl;

import com.taskmanager.entity.TaskEntity;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.storage.TaskStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Profile({"dev","postgres","redis"})
@RequiredArgsConstructor
public class JpaTaskStorage implements TaskStorage {
    private final TaskRepository taskRepository;

    @Override
    public TaskEntity save(TaskEntity task) {
        return taskRepository.save(task);
    }

    @Override
    public Optional<TaskEntity> findById(UUID taskId) {
        return taskRepository.findById(taskId);
    }

    @Override
    public List<TaskEntity> findByUserId(UUID userId) {
        return taskRepository.findByUserIdAndDeletedFalse(userId);
    }

    @Override
    public List<TaskEntity> findPendingByUserId(UUID userId) {
        return taskRepository.findPendingByUserIdAndDeletedFalse(userId, TaskStatus.PENDING);
    }

    @Override
    @Transactional
    public void markAsDeleted(UUID taskId) {
        taskRepository.markAsDeleted(taskId);
    }

    @Override
    public List<TaskEntity> findAllPendingTasks() {
        return taskRepository.findByStatusAndDeletedFalse(TaskStatus.PENDING);
    }
}


