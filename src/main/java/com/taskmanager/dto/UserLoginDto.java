package com.taskmanager.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class UserLoginDto {
    @NotBlank(message = "Username is required")
    private String username;
}
