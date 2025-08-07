package com.hotelreservation.discovery.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Health Controller
 *
 * Controller for health check endpoints.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/health")
public class HealthController {

    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

    /**
     * Health check endpoint
     *
     * @return the health status
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> healthCheck() {
        logger.info("Health check request received");

        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "discovery-service");
        response.put("timestamp", LocalDateTime.now());
        response.put("version", "1.0.0");
        response.put("description", "Eureka Server is running");

        return ResponseEntity.ok(response);
    }

    /**
     * Detailed health check endpoint
     *
     * @return the detailed health status
     */
    @GetMapping("/detailed")
    public ResponseEntity<Map<String, Object>> detailedHealthCheck() {
        logger.info("Detailed health check request received");

        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "discovery-service");
        response.put("timestamp", LocalDateTime.now());
        response.put("version", "1.0.0");
        response.put("description", "Eureka Server is running");
        
        // Add additional health information
        Map<String, Object> details = new HashMap<>();
        details.put("eureka_server", "UP");
        details.put("service_registry", "UP");
        details.put("peer_communication", "UP");
        details.put("self_preservation", "ENABLED");
        
        response.put("details", details);

        return ResponseEntity.ok(response);
    }

    /**
     * Service info endpoint
     *
     * @return the service information
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> serviceInfo() {
        logger.info("Service info request received");

        Map<String, Object> response = new HashMap<>();
        response.put("service", "discovery-service");
        response.put("type", "Eureka Server");
        response.put("version", "1.0.0");
        response.put("description", "Service Discovery Server for Hotel Reservation System");
        response.put("port", 8761);
        response.put("timestamp", LocalDateTime.now());
        
        // Add service capabilities
        Map<String, Object> capabilities = new HashMap<>();
        capabilities.put("service_registration", true);
        capabilities.put("service_discovery", true);
        capabilities.put("health_monitoring", true);
        capabilities.put("load_balancing", true);
        
        response.put("capabilities", capabilities);

        return ResponseEntity.ok(response);
    }
} 