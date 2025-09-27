package com.taskmanager.model;

import lombok.Data;
import lombok.Builder;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String userId;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime creationDate;
    private LocalDateTime targetDate;
    private boolean deleted;
}
