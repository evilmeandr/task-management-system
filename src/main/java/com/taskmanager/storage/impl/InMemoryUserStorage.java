package com.taskmanager.storage.impl;

import com.taskmanager.entity.UserEntity;
import com.taskmanager.storage.UserStorage;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

@Component
@Profile("test")
public class InMemoryUserStorage implements UserStorage {
    private final Map<UUID, UserEntity> users = new ConcurrentHashMap<>();
    private final Map<String, UserEntity> usersByUsername = new ConcurrentHashMap<>();
    
    @Override
    public UserEntity save(UserEntity user) {
        UUID id = user.getId() != null ? user.getId() : UUID.randomUUID();
        user.setId(id);
        users.put(id, user);
        usersByUsername.put(user.getUsername(), user);
        return user;
    }
    
    @Override
    public Optional<UserEntity> findById(UUID id) {
        return Optional.ofNullable(users.get(id));
    }
    
    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return Optional.ofNullable(usersByUsername.get(username));
    }
}
