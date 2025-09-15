package com.taskmanager.service;

import com.taskmanager.dto.UserRegistrationDto;
import com.taskmanager.model.User;

public interface UserService {
    User register(UserRegistrationDto dto);
    User login(String username);
}
