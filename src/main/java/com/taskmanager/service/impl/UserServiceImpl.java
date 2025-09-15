package com.taskmanager.service.impl;

import com.taskmanager.service.UserService;
import com.taskmanager.storage.UserStorage;
import com.taskmanager.dto.UserRegistrationDto;
import com.taskmanager.model.User;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    
    @Override
    public User register(UserRegistrationDto dto) {
        // Проверяем, существует ли пользователь
        if (userStorage.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        User user = User.create(dto.getUsername(), dto.getEmail());
        return userStorage.save(user);
    }
    
    @Override
    public User login(String username) {
        return userStorage.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
