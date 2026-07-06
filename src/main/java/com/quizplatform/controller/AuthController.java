package com.quizplatform.controller;

import com.quizplatform.dto.LoginRequest;
import com.quizplatform.dto.LoginResponse;
import com.quizplatform.dto.RegisterRequest;
import com.quizplatform.dto.UserDTO;
import com.quizplatform.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    @org.springframework.beans.factory.annotation.Autowired
    public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    private AuthService authService;
    private AuthenticationManager authenticationManager;

    /**
     * POST /api/auth/register
     * Register a new user account.
     */
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterRequest request) {
        UserDTO user = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    /**
     * POST /api/auth/login
     * Authenticate and receive a JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request, authenticationManager);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/auth/me
     * Validate token and return current user info.
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me() {
        var user = authService.getCurrentUser();
        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail(),
                "role", user.getRole().name()
        ));
    }
}
