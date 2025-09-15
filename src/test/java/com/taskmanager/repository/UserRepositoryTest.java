package com.taskmanager.repository;

import com.taskmanager.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void save_and_findById() {
        UserEntity user = UserEntity.builder().username("testuser").email("test@example.com").build();
        UserEntity saved = userRepository.save(user);

        Optional<UserEntity> found = userRepository.findById(saved.getId());
        assertThat(found).contains(saved);
    }

    @Test
    void findByUsername_shouldReturnUser() {
        userRepository.save(UserEntity.builder().username("testuser").email("test@example.com").build());

        Optional<UserEntity> found = userRepository.findByUsername("testuser");
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("testuser");
    }
}


