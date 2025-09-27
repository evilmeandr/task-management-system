package com.taskmanager.model;

import lombok.Data;
import lombok.Builder;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
}
