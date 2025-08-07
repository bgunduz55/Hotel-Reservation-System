package com.hotelreservation.reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Reservation Service Application
 *
 * Main application class for Reservation Service.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ReservationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservationServiceApplication.class, args);
    }
} 