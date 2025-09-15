package com.taskmanager.model;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class User {
    private String id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
    
    public static User create(String username, String email) {
        return User.builder()
            .id(UUID.randomUUID().toString())
            .username(username)
            .email(email)
            .createdAt(LocalDateTime.now())
            .build();
    }
}
