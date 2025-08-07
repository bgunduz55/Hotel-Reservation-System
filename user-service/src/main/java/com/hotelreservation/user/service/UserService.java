package com.hotelreservation.user.service;

import com.hotelreservation.user.dto.AuthResponse;
import com.hotelreservation.user.dto.LoginRequest;
import com.hotelreservation.user.dto.RegisterRequest;
import com.hotelreservation.user.entity.User;
import com.hotelreservation.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * User Service
 *
 * Business logic for user management.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@Service
@Transactional
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Register a new user
     *
     * @param registerRequest the registration request
     * @return the auth response
     */
    public AuthResponse register(RegisterRequest registerRequest) {
        logger.info("Registration attempt for user: {}", registerRequest.getUsername());

        try {
            // Check if username already exists
            if (userRepository.existsByUsername(registerRequest.getUsername())) {
                logger.warn("Registration failed: Username {} already exists", registerRequest.getUsername());
                return new AuthResponse("Username already exists");
            }

            // Check if email already exists
            if (userRepository.existsByEmail(registerRequest.getEmail())) {
                logger.warn("Registration failed: Email {} already exists", registerRequest.getEmail());
                return new AuthResponse("Email already exists");
            }

            // Create new user
            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setEmail(registerRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setRoles(List.of("USER"));
            user.setActive(true);

            User savedUser = userRepository.save(user);
            logger.info("User registered successfully: {}", savedUser.getUsername());

            // Generate JWT token
            String token = jwtService.generateToken(savedUser.getUsername(), savedUser.getRoles());
            LocalDateTime issuedAt = jwtService.extractIssuedAt(token);
            LocalDateTime expiresAt = jwtService.extractExpiration(token);

            return new AuthResponse(token, savedUser.getUsername(), savedUser.getRoles(), issuedAt, expiresAt);

        } catch (Exception e) {
            logger.error("Error during registration for user {}: {}", registerRequest.getUsername(), e.getMessage(), e);
            return new AuthResponse("Registration failed: " + e.getMessage());
        }
    }

    /**
     * Login user
     *
     * @param loginRequest the login request
     * @return the auth response
     */
    public AuthResponse login(LoginRequest loginRequest) {
        logger.info("Login attempt for user: {}", loginRequest.getUsername());

        try {
            // Find user by username
            Optional<User> userOpt = userRepository.findByUsernameAndActive(loginRequest.getUsername(), true);
            if (userOpt.isEmpty()) {
                logger.warn("Login failed: User {} not found or inactive", loginRequest.getUsername());
                return new AuthResponse("Invalid username or password");
            }

            User user = userOpt.get();

            // Verify password
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                logger.warn("Login failed: Invalid password for user {}", loginRequest.getUsername());
                return new AuthResponse("Invalid username or password");
            }

            logger.info("User logged in successfully: {}", user.getUsername());

            // Generate JWT token
            String token = jwtService.generateToken(user.getUsername(), user.getRoles());
            LocalDateTime issuedAt = jwtService.extractIssuedAt(token);
            LocalDateTime expiresAt = jwtService.extractExpiration(token);

            return new AuthResponse(token, user.getUsername(), user.getRoles(), issuedAt, expiresAt);

        } catch (Exception e) {
            logger.error("Error during login for user {}: {}", loginRequest.getUsername(), e.getMessage(), e);
            return new AuthResponse("Login failed: " + e.getMessage());
        }
    }

    /**
     * Validate JWT token
     *
     * @param token the JWT token
     * @return the auth response
     */
    public AuthResponse validateToken(String token) {
        try {
            if (!jwtService.validateToken(token)) {
                return new AuthResponse("Invalid token");
            }

            String username = jwtService.extractUsername(token);
            List<String> roles = jwtService.extractRoles(token);

            Optional<User> userOpt = userRepository.findByUsernameAndActive(username, true);
            if (userOpt.isEmpty()) {
                return new AuthResponse("User not found or inactive");
            }

            return new AuthResponse(token, username, roles, 
                    jwtService.extractIssuedAt(token), 
                    jwtService.extractExpiration(token));

        } catch (Exception e) {
            logger.error("Error validating token: {}", e.getMessage(), e);
            return new AuthResponse("Token validation failed: " + e.getMessage());
        }
    }

    /**
     * Get user by username
     *
     * @param username the username
     * @return the user
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsernameAndActive(username, true);
    }

    /**
     * Get all active users
     *
     * @return list of users
     */
    public List<User> getAllActiveUsers() {
        return userRepository.findByActiveTrue();
    }

    /**
     * Update user roles
     *
     * @param username the username
     * @param roles the new roles
     * @return the updated user
     */
    public Optional<User> updateUserRoles(String username, List<String> roles) {
        Optional<User> userOpt = userRepository.findByUsernameAndActive(username, true);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setRoles(roles);
            return Optional.of(userRepository.save(user));
        }
        return Optional.empty();
    }

    /**
     * Deactivate user
     *
     * @param username the username
     * @return true if deactivated, false otherwise
     */
    public boolean deactivateUser(String username) {
        Optional<User> userOpt = userRepository.findByUsernameAndActive(username, true);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setActive(false);
            userRepository.save(user);
            return true;
        }
        return false;
    }
} 