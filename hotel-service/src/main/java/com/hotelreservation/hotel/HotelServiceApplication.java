package com.hotelreservation.hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Hotel Service Application
 *
 * This service manages hotel and room information in the microservices architecture.
 * It provides CRUD operations for hotels and rooms.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class HotelServiceApplication {

    /**
     * Main method to start the Hotel Service
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(HotelServiceApplication.class, args);
    }
} 