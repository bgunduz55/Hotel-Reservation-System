package com.hotelreservation.gateway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Fallback Controller
 *
 * Handles fallback responses when services are unavailable.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    private static final Logger logger = LoggerFactory.getLogger(FallbackController.class);

    /**
     * Hotel Service Fallback
     *
     * @return fallback response for hotel service
     */
    @GetMapping("/hotel-service")
    public ResponseEntity<Map<String, Object>> hotelServiceFallback() {
        logger.warn("Hotel service is unavailable, returning fallback response");

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", "SERVICE_UNAVAILABLE");
        response.put("message", "Hotel service is currently unavailable. Please try again later.");
        response.put("service", "hotel-service");
        response.put("fallback", true);

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    /**
     * Reservation Service Fallback
     *
     * @return fallback response for reservation service
     */
    @GetMapping("/reservation-service")
    public ResponseEntity<Map<String, Object>> reservationServiceFallback() {
        logger.warn("Reservation service is unavailable, returning fallback response");

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", "SERVICE_UNAVAILABLE");
        response.put("message", "Reservation service is currently unavailable. Please try again later.");
        response.put("service", "reservation-service");
        response.put("fallback", true);

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    /**
     * Notification Service Fallback
     *
     * @return fallback response for notification service
     */
    @GetMapping("/notification-service")
    public ResponseEntity<Map<String, Object>> notificationServiceFallback() {
        logger.warn("Notification service is unavailable, returning fallback response");

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", "SERVICE_UNAVAILABLE");
        response.put("message", "Notification service is currently unavailable. Please try again later.");
        response.put("service", "notification-service");
        response.put("fallback", true);

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    /**
     * Discovery Service Fallback
     *
     * @return fallback response for discovery service
     */
    @GetMapping("/discovery-service")
    public ResponseEntity<Map<String, Object>> discoveryServiceFallback() {
        logger.warn("Discovery service is unavailable, returning fallback response");

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", "SERVICE_UNAVAILABLE");
        response.put("message", "Discovery service is currently unavailable. Please try again later.");
        response.put("service", "discovery-service");
        response.put("fallback", true);

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    /**
     * Generic Service Fallback
     *
     * @return fallback response for any service
     */
    @GetMapping("/generic")
    public ResponseEntity<Map<String, Object>> genericFallback() {
        logger.warn("A service is unavailable, returning generic fallback response");

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", "SERVICE_UNAVAILABLE");
        response.put("message", "Service is currently unavailable. Please try again later.");
        response.put("fallback", true);

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
} 