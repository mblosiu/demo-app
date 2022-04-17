package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody User newUser) {
        return userService.createUser(newUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User userReq) {
        return userService.updateUser(id, userReq);
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<String> partialUpdateUser(@PathVariable Long id, @RequestBody User userReq) {
        return userService.partialUpdateUser(id, userReq);
    }

}
