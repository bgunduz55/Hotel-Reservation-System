package com.hotelreservation.discovery.controller;

import com.hotelreservation.discovery.service.ServiceRegistryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service Registry Controller
 *
 * Controller for service registry operations.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/discovery")
public class ServiceRegistryController {

    private static final Logger logger = LoggerFactory.getLogger(ServiceRegistryController.class);

    private final ServiceRegistryService serviceRegistryService;

    @Autowired
    public ServiceRegistryController(ServiceRegistryService serviceRegistryService) {
        this.serviceRegistryService = serviceRegistryService;
    }

    /**
     * Get all registered services
     *
     * @return list of registered services
     */
    @GetMapping("/services")
    public ResponseEntity<Map<String, Object>> getAllServices() {
        logger.info("Getting all registered services");

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "discovery-service");
        
        try {
            List<Map<String, Object>> services = serviceRegistryService.getAllServices();
            response.put("services", services);
            response.put("count", services.size());
            response.put("status", "success");
            
            logger.info("Successfully retrieved {} registered services", services.size());
            
        } catch (Exception e) {
            logger.error("Error getting all services. Error: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Get service by name
     *
     * @param serviceName the service name
     * @return the service information
     */
    @GetMapping("/services/{serviceName}")
    public ResponseEntity<Map<String, Object>> getServiceByName(@PathVariable String serviceName) {
        logger.info("Getting service information for: {}", serviceName);

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "discovery-service");
        response.put("requestedService", serviceName);
        
        try {
            Map<String, Object> serviceInfo = serviceRegistryService.getServiceByName(serviceName);
            
            if (serviceInfo.containsKey("error")) {
                response.put("status", "error");
                response.put("message", serviceInfo.get("error"));
                return ResponseEntity.notFound().build();
            } else {
                response.put("serviceInfo", serviceInfo);
                response.put("status", "success");
                
                logger.info("Successfully retrieved service information for: {}", serviceName);
            }
            
        } catch (Exception e) {
            logger.error("Error getting service information for: {}. Error: {}", serviceName, e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Get service statistics
     *
     * @return the service statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getServiceStatistics() {
        logger.info("Getting service statistics");

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "discovery-service");
        
        try {
            Map<String, Object> statistics = serviceRegistryService.getServiceStatistics();
            response.put("statistics", statistics);
            response.put("status", "success");
            
            logger.info("Successfully retrieved service statistics");
            
        } catch (Exception e) {
            logger.error("Error getting service statistics. Error: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Get service health status
     *
     * @param serviceName the service name
     * @return the service health status
     */
    @GetMapping("/services/{serviceName}/health")
    public ResponseEntity<Map<String, Object>> getServiceHealth(@PathVariable String serviceName) {
        logger.info("Getting health status for service: {}", serviceName);

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "discovery-service");
        response.put("requestedService", serviceName);
        
        try {
            Map<String, Object> serviceInfo = serviceRegistryService.getServiceByName(serviceName);
            
            if (serviceInfo.containsKey("error")) {
                response.put("status", "error");
                response.put("message", serviceInfo.get("error"));
                return ResponseEntity.notFound().build();
            } else {
                String serviceStatus = (String) serviceInfo.get("status");
                List<Map<String, Object>> instances = (List<Map<String, Object>>) serviceInfo.get("instances");
                
                Map<String, Object> healthInfo = new HashMap<>();
                healthInfo.put("serviceStatus", serviceStatus);
                healthInfo.put("instanceCount", instances.size());
                healthInfo.put("upInstances", instances.stream()
                        .filter(instance -> "UP".equals(instance.get("status")))
                        .count());
                healthInfo.put("downInstances", instances.stream()
                        .filter(instance -> "DOWN".equals(instance.get("status")))
                        .count());
                
                response.put("healthInfo", healthInfo);
                response.put("status", "success");
                
                logger.info("Successfully retrieved health status for service: {}", serviceName);
            }
            
        } catch (Exception e) {
            logger.error("Error getting health status for service: {}. Error: {}", serviceName, e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Get discovery service info
     *
     * @return the discovery service information
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getDiscoveryInfo() {
        logger.info("Getting discovery service information");

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "discovery-service");
        response.put("type", "Eureka Server");
        response.put("version", "1.0.0");
        response.put("description", "Service Discovery Server for Hotel Reservation System");
        response.put("port", 8761);
        response.put("status", "UP");
        
        // Add discovery capabilities
        Map<String, Object> capabilities = new HashMap<>();
        capabilities.put("service_registration", true);
        capabilities.put("service_discovery", true);
        capabilities.put("health_monitoring", true);
        capabilities.put("load_balancing", true);
        capabilities.put("self_preservation", true);
        
        response.put("capabilities", capabilities);

        return ResponseEntity.ok(response);
    }
} 