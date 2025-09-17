package com.taskmanager.integration;

import com.taskmanager.BaseIntegrationTest;
import com.taskmanager.entity.UserEntity;
import com.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFindUserByUsername() {
        UserEntity user = UserEntity.builder()
                .username("int_user")
                .email("int_user@example.com")
                .build();

        UserEntity saved = userRepository.save(user);

        Optional<UserEntity> found = userRepository.findByUsername("int_user");

        assertThat(saved.getId()).isNotNull();
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("int_user");
    }
}


