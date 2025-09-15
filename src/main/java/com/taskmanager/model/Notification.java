package com.taskmanager.model;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class Notification {
    private String id;
    private String userId;
    private String message;
    private NotificationType type;
    private boolean read;
    private LocalDateTime createdAt;
}
