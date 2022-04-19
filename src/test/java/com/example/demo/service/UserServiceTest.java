package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllUsers() {
        List<User> list = new ArrayList<User>();
        User user1 = new User("test1@test.com", "TestName1", "TestLastName1");
        User user2 = new User("test2@test.com", "TestName2", "TestLastName2");
        User user3 = new User("test3@test.com", "TestName3", "TestLastName2");

        list.add(user1);
        list.add(user2);
        list.add(user3);

        when(userRepository.findAll()).thenReturn(list);

        List<User> userList = userService.getUsers();
        assertThat(userList.size()).isEqualTo(3);
    }

    @Test
    void getUserById() {
        User user1 = new User("test1@test.com", "TestName1", "TestLastName1");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        User user = userService.getUser(1L);

        assertThat(user).isEqualTo(user1);
    }

    @Test
    void getUserByIdNotExistsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        try {
            userService.getUser(1L);
            fail("Should throw exception.");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("User doesn't exist.");
        } catch (Exception ex) {
            fail("Other exception");
        }
    }

    @Test
    void createUser() {
        User user = new User("test1@test.com", "TestName1", "TestLastName1");
        User userCreated = userService.createUser(user);

        assertThat(userCreated).isEqualTo(user);
    }

    @Test
    void createUserEmailIsTakenException() {
        User user = new User("test1@test.com", "TestName1", "TestLastName1");
        when(userRepository.findByEmail("test1@test.com")).thenReturn(Optional.of(user));
        try {
            userService.createUser(user);
            fail("Should throw exception.");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Email address is already taken.");
        } catch (Exception ex) {
            fail("Other exception");
        }
    }

    @Test
    void createUserMissingDataException() {
        User user = new User();
        user.setEmail("test1@test.com");
        user.setFirstName("TestName1");
        try {
            userService.createUser(user);
            fail("Should throw exception.");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Required data not provided.");
        } catch (Exception ex) {
            fail("Other exception");
        }
    }

    @Test
    void deleteUser() {
        User user = new User("email1@gmail.com", "Adam", "Kowalski");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        String msg = userService.deleteUser(1L);
        assertThat(msg).isEqualTo("User 1 deleted successfully.");
    }

    @Test
    void deleteUserNotExistsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        try {
            userService.deleteUser(1L);
            fail("Should throw exception.");
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo("User doesn't exist.");
        } catch (Exception ex) {
            fail("Other exception");
        }
    }

    @Test
    void updateUser() {
        User user = new User("email1@gmail.com", "Adam", "Kowalski");
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        user.setEmail("email2@gmail.com");
        user.setFirstName("Newfirstname");
        user.setLastName("Newlastname");
        userService.updateUser(id, user);
        assertThat(user.getEmail()).isEqualTo("email2@gmail.com");
        assertThat(user.getFirstName()).isEqualTo("Newfirstname");
        assertThat(user.getLastName()).isEqualTo("Newlastname");
    }

    @Test
    void updateUserNotExistsException() {
        User user = new User("email1@gmail.com", "Adam", "Oldlastname");
        Long id = 1L;
        try {
            userService.updateUser(id, user);
            fail("Should throw exception.");
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo("User doesn't exist.");
        } catch (Exception ex) {
            fail("Other exception");
        }
    }

    @Test
    void updateUserEmailIsTakenException() {
        User user = new User("email1@gmail.com", "Adam", "Oldlastname");
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("email1@gmail.com")).thenReturn(Optional.of(user));
        try {
            userService.updateUser(id, user);
            fail("Should throw exception.");
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo("Email address is already taken.");
        } catch (Exception ex) {
            fail("Other exception");
        }
    }

    @Test
    void updateUserMissingDataException() {
        User user = new User();
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        try {
            userService.updateUser(id, user);
            fail("Should throw exception.");
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo("Required data not provided.");
        } catch (Exception ex) {
            fail("Other exception");
        }
    }

    @Test
    void partialUpdateUser() {
        User user = new User("email1@gmail.com", "Adam", "Oldlastname");
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        user.setLastName("Newlastname");
        userService.partialUpdateUser(id, user);
        assertThat(user.getLastName()).isEqualTo("Newlastname");
    }

    @Test
    void partialUpdateUserNotExistsException() {
        User user = new User("email1@gmail.com", "Adam", "Oldlastname");
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        try {
            user.setLastName("Newlastname");
            userService.partialUpdateUser(id, user);
            fail("Should throw exception.");
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo("User doesn't exist.");
        } catch (Exception ex) {
            fail("Other exception");
        }

    }

    @Test
    void partialUpdateUserEmailIsTaken() {
        User user = new User("email1@gmail.com", "Adam", "Oldlastname");
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("email1@gmail.com")).thenReturn(Optional.of(user));
        try {
            user.setLastName("Newlastname");
            userService.partialUpdateUser(id, user);
            fail("Should throw exception.");
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo("Email address is already taken.");
        } catch (Exception ex) {
            fail("Other exception");
        }
    }
}