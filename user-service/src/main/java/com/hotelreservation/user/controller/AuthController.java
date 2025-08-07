package com.hotelreservation.user.controller;

import com.hotelreservation.user.dto.AuthResponse;
import com.hotelreservation.user.dto.LoginRequest;
import com.hotelreservation.user.dto.RegisterRequest;
import com.hotelreservation.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Auth Controller
 *
 * REST controller for authentication operations.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Register endpoint
     *
     * @param registerRequest the registration request
     * @return the JWT response
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        logger.info("Registration request received for user: {}", registerRequest.getUsername());

        AuthResponse response = userService.register(registerRequest);

        if (response.getToken() != null) {
            logger.info("Registration successful for user: {}", registerRequest.getUsername());
            return ResponseEntity.ok(response);
        } else {
            logger.warn("Registration failed for user {}: {}", registerRequest.getUsername(), response.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Login endpoint
     *
     * @param loginRequest the login request
     * @return the JWT response
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("Login request received for user: {}", loginRequest.getUsername());

        AuthResponse response = userService.login(loginRequest);

        if (response.getToken() != null) {
            logger.info("Login successful for user: {}", loginRequest.getUsername());
            return ResponseEntity.ok(response);
        } else {
            logger.warn("Login failed for user {}: {}", loginRequest.getUsername(), response.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    /**
     * Validate token endpoint
     *
     * @param token the JWT token
     * @return the validation response
     */
    @PostMapping("/validate")
    public ResponseEntity<AuthResponse> validateToken(@RequestParam String token) {
        logger.info("Token validation request received");

        AuthResponse response = userService.validateToken(token);

        if (response.getToken() != null) {
            logger.info("Token validation successful");
            return ResponseEntity.ok(response);
        } else {
            logger.warn("Token validation failed: {}", response.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    /**
     * Health check endpoint
     *
     * @return health status
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        logger.info("Health check request received");
        return ResponseEntity.ok("Auth service is running");
    }

    /**
     * Root endpoint for testing
     *
     * @return simple response
     */
    @GetMapping("/")
    public ResponseEntity<String> root() {
        logger.info("Root endpoint accessed");
        return ResponseEntity.ok("User Service is running");
    }
} 