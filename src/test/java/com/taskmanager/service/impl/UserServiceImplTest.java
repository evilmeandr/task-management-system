package com.taskmanager.service.impl;

import com.taskmanager.dto.UserRegistrationDto;
import com.taskmanager.model.User;
import com.taskmanager.entity.UserEntity;
import com.taskmanager.storage.UserStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserStorage userStorage;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegistrationDto dto;
    private User testUser;

    @BeforeEach
    void setUp() {
        dto = new UserRegistrationDto();
        dto.setUsername("testuser");
        dto.setEmail("test@example.com");
        testUser = User.builder().username("testuser").email("test@example.com").build();
    }

    @Test
    void register_shouldSaveNewUser() {
        when(userStorage.findByUsername(dto.getUsername())).thenReturn(Optional.empty());
        when(userStorage.save(any(UserEntity.class))).thenAnswer(inv -> (UserEntity) inv.getArgument(0));

        User registered = userService.register(dto);

        assertThat(registered.getUsername()).isEqualTo(testUser.getUsername());
        assertThat(registered.getEmail()).isEqualTo(testUser.getEmail());
        verify(userStorage).save(any(UserEntity.class));
    }

    @Test
    void register_shouldThrowOnDuplicateUsername() {
        when(userStorage.findByUsername(dto.getUsername())).thenReturn(Optional.of(UserEntity.builder().username("testuser").email("test@example.com").build()));

        assertThatThrownBy(() -> userService.register(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username already exists");
    }

    @Test
    void login_shouldReturnUser() {
        when(userStorage.findByUsername("testuser")).thenReturn(Optional.of(UserEntity.builder().username("testuser").email("test@example.com").build()));

        User loggedIn = userService.login("testuser");

        assertThat(loggedIn.getUsername()).isEqualTo(testUser.getUsername());
        assertThat(loggedIn.getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    void login_shouldThrowIfNotFound() {
        when(userStorage.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.login("nonexistent"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found");
    }
}
