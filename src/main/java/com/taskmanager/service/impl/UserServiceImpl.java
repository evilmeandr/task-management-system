package com.taskmanager.service.impl;

import com.taskmanager.config.EntityMapper;
import com.taskmanager.entity.UserEntity;
import com.taskmanager.service.UserService;
import com.taskmanager.storage.UserStorage;
import com.taskmanager.dto.UserRegistrationDto;
import com.taskmanager.model.User;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    
    @Override
    public List<User> getAllUsers() {
        List<UserEntity> entities = userStorage.findAll();
        return entities.stream()
            .map(EntityMapper::toModel)
            .collect(Collectors.toList());
    }
    
    @Override
    public User register(UserRegistrationDto dto) {
        if (userStorage.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        UserEntity entity = UserEntity.builder()
            .username(dto.getUsername())
            .email(dto.getEmail())
            .build();
        UserEntity saved = userStorage.save(entity);
        return EntityMapper.toModel(saved);
    }
    
    @Override
    public User login(String username) {
        UserEntity found = userStorage.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return EntityMapper.toModel(found);
    }
}
