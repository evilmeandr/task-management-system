package com.taskmanager.service;

import com.taskmanager.dto.UserRegistrationDto;
import com.taskmanager.model.User;
import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User register(UserRegistrationDto dto);
    User login(String username);
}
