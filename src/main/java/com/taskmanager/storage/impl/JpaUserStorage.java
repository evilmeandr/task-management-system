package com.taskmanager.storage.impl;

import com.taskmanager.entity.UserEntity;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class JpaUserStorage implements UserStorage {
    private final UserRepository userRepository;

    @Override
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}


