package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<User> getUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = userOptional.get();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> createUser(@RequestBody User newUser) {
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            return new ResponseEntity<>("Email address is already taken.", HttpStatus.BAD_REQUEST);
        }
        if (newUser.getEmail() == null || newUser.getFirstName() == null || newUser.getLastName() == null) {
            return new ResponseEntity<>("Data is not complete.", HttpStatus.BAD_REQUEST);
        }
        userRepository.save(newUser);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<String> deleteUser(Long id) {
        boolean userExists = userRepository.existsById(id);
        if (!userExists) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> updateUser(Long id, User userReq) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (userRepository.findByEmail(userReq.getEmail()).isPresent()) {
            return new ResponseEntity<>("Email address is already taken.", HttpStatus.BAD_REQUEST);
        }
        if (userReq.getEmail() == null || userReq.getFirstName() == null || userReq.getLastName() == null) {
            return new ResponseEntity<>("Data is not complete.", HttpStatus.BAD_REQUEST);
        }
        user.setFirstName(userReq.getFirstName());
        user.setLastName(userReq.getLastName());
        user.setEmail(userReq.getEmail());
        userRepository.save(user);
        return new ResponseEntity<>("User updated", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> partialUpdateUser(Long id, User userReq) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        int changes = 0;
        if (userReq.getEmail() != null && !Objects.equals(userReq.getEmail(), user.getEmail())) {
            user.setEmail(userReq.getEmail());
            changes++;
        }
        if (userReq.getFirstName() != null && !Objects.equals(userReq.getFirstName(), user.getFirstName())) {
            user.setFirstName(userReq.getFirstName());
            changes++;
        }
        if (userReq.getLastName() != null && !Objects.equals(userReq.getLastName(), user.getLastName())) {
            user.setLastName(userReq.getLastName());
            changes++;
        }
        if (changes > 0) {
            return new ResponseEntity<>("No changes", HttpStatus.OK);
        }

        if (userRepository.findByEmail(userReq.getEmail()).isPresent()) {
            return new ResponseEntity<>("Email address is already taken.", HttpStatus.BAD_REQUEST);
        }

        userRepository.save(user);
        return new ResponseEntity<>("User updated", HttpStatus.OK);
    }
}
