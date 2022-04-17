package com.example.demo.repository;

import com.example.demo.model.User;
import org.hibernate.mapping.Collection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void shouldFindUserByEmail() {
        User user = new User("email1@gmail.com", "Adam", "Kowalski");
        userRepository.save(user);
        Optional<User> expected = userRepository.findByEmail("email1@gmail.com");
        assertThat(expected).isPresent();
    }

    @Test
    void shouldNotFindUserByEmail() {
        Optional<User> expected = userRepository.findByEmail("email5@gmail.com");
        // then
        assertThat(expected).isEmpty();
    }
}