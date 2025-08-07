package com.hotelreservation.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * JWT Response DTO
 *
 * Response model for JWT authentication.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
public class JwtResponse {

    @JsonProperty("token")
    private String token;

    @JsonProperty("token_type")
    private String tokenType = "Bearer";

    @JsonProperty("expires_in")
    private Long expiresIn;

    @JsonProperty("username")
    private String username;

    @JsonProperty("roles")
    private String[] roles;

    @JsonProperty("issued_at")
    private LocalDateTime issuedAt;

    @JsonProperty("expires_at")
    private LocalDateTime expiresAt;

    /**
     * Default constructor
     */
    public JwtResponse() {
    }

    /**
     * Constructor with token and username
     *
     * @param token the JWT token
     * @param username the username
     * @param roles the user roles
     * @param expiresIn the token expiration time in seconds
     */
    public JwtResponse(String token, String username, String[] roles, Long expiresIn) {
        this.token = token;
        this.username = username;
        this.roles = roles;
        this.expiresIn = expiresIn;
        this.issuedAt = LocalDateTime.now();
        this.expiresAt = this.issuedAt.plusSeconds(expiresIn);
    }

    /**
     * Get token
     *
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Set token
     *
     * @param token the token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Get token type
     *
     * @return the token type
     */
    public String getTokenType() {
        return tokenType;
    }

    /**
     * Set token type
     *
     * @param tokenType the token type
     */
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    /**
     * Get expires in
     *
     * @return the expires in
     */
    public Long getExpiresIn() {
        return expiresIn;
    }

    /**
     * Set expires in
     *
     * @param expiresIn the expires in
     */
    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    /**
     * Get username
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set username
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get roles
     *
     * @return the roles
     */
    public String[] getRoles() {
        return roles;
    }

    /**
     * Set roles
     *
     * @param roles the roles
     */
    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    /**
     * Get issued at
     *
     * @return the issued at
     */
    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    /**
     * Set issued at
     *
     * @param issuedAt the issued at
     */
    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    /**
     * Get expires at
     *
     * @return the expires at
     */
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    /**
     * Set expires at
     *
     * @param expiresAt the expires at
     */
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override
    public String toString() {
        return "JwtResponse{" +
                "token='" + (token != null ? token.substring(0, Math.min(token.length(), 20)) + "..." : null) + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", expiresIn=" + expiresIn +
                ", username='" + username + '\'' +
                ", issuedAt=" + issuedAt +
                ", expiresAt=" + expiresAt +
                '}';
    }
} 