package com.Mission.Shakti.controller;


import com.Mission.Shakti.dto.LoginResponse;
import com.Mission.Shakti.dto.UserDto;
import com.Mission.Shakti.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDto request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {

        LoginResponse response = authService.login(
                request.get("username"),
                request.get("password")
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/approve/{userId}")
    public ResponseEntity<?> approveUser(
            @PathVariable Long userId,
            @RequestParam boolean approve) {

        String approverUsername = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        String message = authService.approveUser(userId, approverUsername, approve);

        return ResponseEntity.ok(Map.of("message", message));
    }
}