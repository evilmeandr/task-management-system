package com.taskmanager.config;

import com.taskmanager.entity.NotificationEntity;
import com.taskmanager.entity.TaskEntity;
import com.taskmanager.entity.UserEntity;
import com.taskmanager.model.Notification;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.model.User;
import java.util.UUID;

public final class EntityMapper {
    private EntityMapper() {}

    public static User toModel(UserEntity entity) {
        if (entity == null) return null;
        return User.builder()
            .id(entity.getId() != null ? entity.getId().toString() : null)
            .username(entity.getUsername())
            .email(entity.getEmail())
            .createdAt(entity.getCreatedAt())
            .build();
    }

    public static UserEntity toEntity(User model) {
        if (model == null) return null;
        return UserEntity.builder()
            .id(model.getId() != null ? UUID.fromString(model.getId()) : null)
            .username(model.getUsername())
            .email(model.getEmail())
            .createdAt(model.getCreatedAt())
            .build();
    }

    public static Task toModel(TaskEntity entity) {
        if (entity == null) return null;
        return Task.builder()
            .id(entity.getId() != null ? entity.getId().toString() : null)
            .userId(entity.getUserId() != null ? entity.getUserId().toString() : null)
            .title(entity.getTitle())
            .description(entity.getDescription())
            .status(entity.getStatus())
            .creationDate(entity.getCreationDate())
            .targetDate(entity.getTargetDate())
            .deleted(entity.isDeleted())
            .build();
    }

    public static TaskEntity toEntity(Task model) {
        if (model == null) return null;
        return TaskEntity.builder()
            .id(model.getId() != null ? UUID.fromString(model.getId()) : null)
            .userId(model.getUserId() != null ? UUID.fromString(model.getUserId()) : null)
            .title(model.getTitle())
            .description(model.getDescription())
            .status(model.getStatus() != null ? model.getStatus() : TaskStatus.PENDING)
            .creationDate(model.getCreationDate())
            .targetDate(model.getTargetDate())
            .deleted(model.isDeleted())
            .build();
    }

    public static Notification toModel(NotificationEntity entity) {
        if (entity == null) return null;
        return Notification.builder()
            .id(entity.getId() != null ? entity.getId().toString() : null)
            .userId(entity.getUserId() != null ? entity.getUserId().toString() : null)
            .message(entity.getMessage())
            .type(entity.getType())
            .read(entity.isRead())
            .createdAt(entity.getCreatedAt())
            .build();
    }

    public static NotificationEntity toEntity(Notification model) {
        if (model == null) return null;
        return NotificationEntity.builder()
            .id(model.getId() != null ? UUID.fromString(model.getId()) : null)
            .userId(model.getUserId() != null ? UUID.fromString(model.getUserId()) : null)
            .message(model.getMessage())
            .type(model.getType())
            .read(model.isRead())
            .createdAt(model.getCreatedAt() != null ? model.getCreatedAt() : java.time.LocalDateTime.now())
            .build();
    }
}


