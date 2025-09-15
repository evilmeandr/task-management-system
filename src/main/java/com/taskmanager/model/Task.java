package com.taskmanager.model;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class Task {
    private String id;
    private String userId;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime creationDate;
    private LocalDateTime targetDate;
    private boolean deleted;
    
    public static Task create(String userId, String title, String description, LocalDateTime targetDate) {
        return Task.builder()
            .id(UUID.randomUUID().toString())
            .userId(userId)
            .title(title)
            .description(description)
            .status(TaskStatus.PENDING)
            .creationDate(LocalDateTime.now())
            .targetDate(targetDate)
            .deleted(false)
            .build();
    }
}
