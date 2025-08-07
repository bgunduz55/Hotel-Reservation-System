package com.hotelreservation.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Auth Response DTO
 *
 * Response model for authentication operations.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
public class AuthResponse {

    @JsonProperty("token")
    private String token;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private Long expiresIn;

    @JsonProperty("username")
    private String username;

    @JsonProperty("roles")
    private List<String> roles;

    @JsonProperty("issued_at")
    private LocalDateTime issuedAt;

    @JsonProperty("expires_at")
    private LocalDateTime expiresAt;

    @JsonProperty("message")
    private String message;

    /**
     * Default constructor
     */
    public AuthResponse() {
        this.tokenType = "Bearer";
    }

    /**
     * Constructor for successful authentication
     *
     * @param token the JWT token
     * @param username the username
     * @param roles the user roles
     * @param issuedAt the token issued time
     * @param expiresAt the token expiration time
     */
    public AuthResponse(String token, String username, List<String> roles, LocalDateTime issuedAt, LocalDateTime expiresAt) {
        this();
        this.token = token;
        this.username = username;
        this.roles = roles;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.expiresIn = 86400L; // 24 hours in seconds
    }

    /**
     * Constructor for error response
     *
     * @param message the error message
     */
    public AuthResponse(String message) {
        this();
        this.message = message;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "token='" + (token != null ? "[PROTECTED]" : null) + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", expiresIn=" + expiresIn +
                ", username='" + username + '\'' +
                ", roles=" + roles +
                ", issuedAt=" + issuedAt +
                ", expiresAt=" + expiresAt +
                ", message='" + message + '\'' +
                '}';
    }
} 