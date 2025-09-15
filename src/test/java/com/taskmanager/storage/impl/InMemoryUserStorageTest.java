package com.taskmanager.storage.impl;

import com.taskmanager.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class InMemoryUserStorageTest {

    private InMemoryUserStorage storage;
    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        storage = new InMemoryUserStorage();
        testUser = UserEntity.builder().username("testuser").email("test@example.com").build();
    }

    @Test
    void save_shouldStoreAndReturnUser() {
        UserEntity saved = storage.save(testUser);

        assertThat(saved).isEqualTo(testUser);
        assertThat(storage.findById(testUser.getId())).contains(testUser);
    }

    @Test
    void findById_shouldReturnUserIfExists() {
        storage.save(testUser);

        Optional<UserEntity> found = storage.findById(testUser.getId());

        assertThat(found).contains(testUser);
    }

    @Test
    void findById_shouldReturnEmptyIfNotExists() {
        Optional<UserEntity> found = storage.findById(UUID.randomUUID());

        assertThat(found).isEmpty();
    }

    @Test
    void findByUsername_shouldReturnUserIfExists() {
        storage.save(testUser);

        Optional<UserEntity> found = storage.findByUsername("testuser");

        assertThat(found).contains(testUser);
    }

    @Test
    void findByUsername_shouldReturnEmptyIfNotExists() {
        Optional<UserEntity> found = storage.findByUsername("nonexistent");

        assertThat(found).isEmpty();
    }
}
