package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User doesn't exist.");
        }
        return userOptional.get();
    }

    @Transactional
    public User createUser(@RequestBody User newUser) {
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email address is already taken.");
        }
        if (newUser.getEmail() == null || newUser.getFirstName() == null || newUser.getLastName() == null) {
            throw new IllegalArgumentException("Required data not provided.");
        }
        userRepository.save(newUser);
        return newUser;
    }

    @Transactional
    public String deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User doesn't exist.");
        }
        userRepository.delete(user.get());
        return "User " + id + " deleted successfully.";
    }

    @Transactional
    public User updateUser(Long id, User userReq) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User doesn't exist."));
        if (userRepository.findByEmail(userReq.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email address is already taken.");
        }
        if (userReq.getEmail() == null || userReq.getFirstName() == null || userReq.getLastName() == null) {
            throw new IllegalArgumentException("Required data not provided.");
        }
        user.setFirstName(userReq.getFirstName());
        user.setLastName(userReq.getLastName());
        user.setEmail(userReq.getEmail());
        userRepository.save(user);
        return user;
    }

    @Transactional
    public User partialUpdateUser(Long id, User userReq) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User doesn't exist."));

        if (userReq.getEmail() != null && !Objects.equals(userReq.getEmail(), user.getEmail())) {
            user.setEmail(userReq.getEmail());
        }
        if (userReq.getFirstName() != null && !Objects.equals(userReq.getFirstName(), user.getFirstName())) {
            user.setFirstName(userReq.getFirstName());
        }
        if (userReq.getLastName() != null && !Objects.equals(userReq.getLastName(), user.getLastName())) {
            user.setLastName(userReq.getLastName());
        }

        if (userRepository.findByEmail(userReq.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email address is already taken.");
        }

        userRepository.save(user);
        return user;
    }
}
