package com.taskmanager.controller;

import com.taskmanager.service.UserService;
import com.taskmanager.dto.UserRegistrationDto;
import com.taskmanager.model.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody UserRegistrationDto dto) {
        User user = userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    
    @GetMapping("/login")
    public ResponseEntity<User> login(@RequestParam String username) {
        User user = userService.login(username);
        return ResponseEntity.ok(user);
    }
}
