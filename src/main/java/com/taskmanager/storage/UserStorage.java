package com.taskmanager.storage;

import com.taskmanager.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStorage {
    UserEntity save(UserEntity user);
    Optional<UserEntity> findById(UUID id);
    Optional<UserEntity> findByUsername(String username);
    List<UserEntity> findAll();
}
