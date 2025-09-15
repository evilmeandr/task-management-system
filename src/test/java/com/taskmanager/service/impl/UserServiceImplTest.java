package com.taskmanager.service.impl;

import com.taskmanager.dto.UserRegistrationDto;
import com.taskmanager.model.User;
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
        testUser = User.create("testuser", "test@example.com");
    }

    @Test
    void register_shouldSaveNewUser() {
        when(userStorage.findByUsername(dto.getUsername())).thenReturn(Optional.empty());
        when(userStorage.save(any(User.class))).thenReturn(testUser);

        User registered = userService.register(dto);

        assertThat(registered).isEqualTo(testUser);
        verify(userStorage).save(any(User.class));
    }

    @Test
    void register_shouldThrowOnDuplicateUsername() {
        when(userStorage.findByUsername(dto.getUsername())).thenReturn(Optional.of(testUser));

        assertThatThrownBy(() -> userService.register(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username already exists");
    }

    @Test
    void login_shouldReturnUser() {
        when(userStorage.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        User loggedIn = userService.login("testuser");

        assertThat(loggedIn).isEqualTo(testUser);
    }

    @Test
    void login_shouldThrowIfNotFound() {
        when(userStorage.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.login("nonexistent"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found");
    }
}
