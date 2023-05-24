package com.example.anonymousboard.auth.controller;

import com.example.anonymousboard.auth.dto.LoginRequest;
import com.example.anonymousboard.auth.dto.TokenResponse;
import com.example.anonymousboard.auth.service.AuthService;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        TokenResponse tokenResponse = authService.createToken(loginRequest);
        return ResponseEntity.ok(tokenResponse);
    }
}
