package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

//@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    private AutoCloseable autoCloseable;
    private UserService underTest;


    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new UserService(userRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getAllUsers() {
        underTest.getUsers();
        verify(userRepository).findAll();
    }

    @Test
    void getUserById() {
        underTest.getUser(1L);
        verify(userRepository).findById(1L);
    }

    @Test
    void createUser() {
        User user = new User("email1@gmail.com", "Adam", "Kowalski");

        underTest.createUser(user);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(user);
    }

    @Test
    @Disabled
    void deleteUser() {
    }

    @Test
    @Disabled
    void updateUser() {
    }

    @Test
    @Disabled
    void partialUpdateUser() {
    }
}