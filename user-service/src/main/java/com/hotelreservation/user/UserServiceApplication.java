package com.hotelreservation.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * User Service Application
 *
 * Main application class for user service.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
} 