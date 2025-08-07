package com.hotelreservation.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Register Request DTO
 *
 * Request model for user registration.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
public class RegisterRequest {

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    /**
     * Default constructor
     */
    public RegisterRequest() {
    }

    /**
     * Constructor with username, email and password
     *
     * @param username the username
     * @param email the email
     * @param password the password
     */
    public RegisterRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
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
     * Get email
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set email
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
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
        return "RegisterRequest{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
} 