package com.hotelreservation.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * JWT Request DTO
 *
 * Request model for JWT authentication.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
public class JwtRequest {

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    /**
     * Default constructor
     */
    public JwtRequest() {
    }

    /**
     * Constructor with username and password
     *
     * @param username the username
     * @param password the password
     */
    public JwtRequest(String username, String password) {
        this.username = username;
        this.password = password;
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
     * Get password
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set password
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "JwtRequest{" +
                "username='" + username + '\'' +
                '}';
    }
} 