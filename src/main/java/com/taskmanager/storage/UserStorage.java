package com.taskmanager.storage;

import com.taskmanager.model.User;
import java.util.Optional;

public interface UserStorage {
    User save(User user);
    Optional<User> findById(String id);
    Optional<User> findByUsername(String username);
}
