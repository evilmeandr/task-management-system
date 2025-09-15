package com.taskmanager.storage.impl;

import com.taskmanager.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class InMemoryUserStorageTest {

    private InMemoryUserStorage storage;
    private User testUser;

    @BeforeEach
    void setUp() {
        storage = new InMemoryUserStorage();
        testUser = User.create("testuser", "test@example.com");
    }

    @Test
    void save_shouldStoreAndReturnUser() {
        User saved = storage.save(testUser);

        assertThat(saved).isEqualTo(testUser);
        assertThat(storage.findById(testUser.getId())).contains(testUser);
    }

    @Test
    void findById_shouldReturnUserIfExists() {
        storage.save(testUser);

        Optional<User> found = storage.findById(testUser.getId());

        assertThat(found).contains(testUser);
    }

    @Test
    void findById_shouldReturnEmptyIfNotExists() {
        Optional<User> found = storage.findById("nonexistent");

        assertThat(found).isEmpty();
    }

    @Test
    void findByUsername_shouldReturnUserIfExists() {
        storage.save(testUser);

        Optional<User> found = storage.findByUsername("testuser");

        assertThat(found).contains(testUser);
    }

    @Test
    void findByUsername_shouldReturnEmptyIfNotExists() {
        Optional<User> found = storage.findByUsername("nonexistent");

        assertThat(found).isEmpty();
    }
}
