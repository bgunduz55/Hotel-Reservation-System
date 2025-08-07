package com.hotelreservation.discovery.service;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service Registry Service
 *
 * Service for managing service registry operations.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@Service
public class ServiceRegistryService {

    private static final Logger logger = LoggerFactory.getLogger(ServiceRegistryService.class);

    private final EurekaClient eurekaClient;

    @Autowired
    public ServiceRegistryService(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
    }

    /**
     * Get all registered services
     *
     * @return list of registered services
     */
    public List<Map<String, Object>> getAllServices() {
        logger.info("Getting all registered services");

        List<Map<String, Object>> services = new ArrayList<>();
        
        try {
            List<Application> applications = eurekaClient.getApplications().getRegisteredApplications();
            
            for (Application application : applications) {
                Map<String, Object> serviceInfo = new HashMap<>();
                serviceInfo.put("name", application.getName());
                serviceInfo.put("instanceCount", application.getInstances().size());
                serviceInfo.put("status", getApplicationStatus(application));
                
                List<Map<String, Object>> instances = new ArrayList<>();
                for (InstanceInfo instance : application.getInstances()) {
                    Map<String, Object> instanceInfo = new HashMap<>();
                    instanceInfo.put("instanceId", instance.getInstanceId());
                    instanceInfo.put("hostName", instance.getHostName());
                    instanceInfo.put("ipAddress", instance.getIPAddr());
                    instanceInfo.put("port", instance.getPort());
                    instanceInfo.put("status", instance.getStatus().name());
                    instanceInfo.put("healthCheckUrl", instance.getHealthCheckUrl());
                    instanceInfo.put("homePageUrl", instance.getHomePageUrl());
                    instanceInfo.put("lastUpdatedTimestamp", instance.getLastUpdatedTimestamp());
                    
                    instances.add(instanceInfo);
                }
                serviceInfo.put("instances", instances);
                
                services.add(serviceInfo);
            }
            
            logger.info("Found {} registered services", services.size());
            
        } catch (Exception e) {
            logger.error("Error getting registered services. Error: {}", e.getMessage(), e);
        }
        
        return services;
    }

    /**
     * Get service by name
     *
     * @param serviceName the service name
     * @return the service information
     */
    public Map<String, Object> getServiceByName(String serviceName) {
        logger.info("Getting service information for: {}", serviceName);

        Map<String, Object> serviceInfo = new HashMap<>();
        
        try {
            Application application = eurekaClient.getApplication(serviceName);
            
            if (application != null) {
                serviceInfo.put("name", application.getName());
                serviceInfo.put("instanceCount", application.getInstances().size());
                serviceInfo.put("status", getApplicationStatus(application));
                
                List<Map<String, Object>> instances = new ArrayList<>();
                for (InstanceInfo instance : application.getInstances()) {
                    Map<String, Object> instanceInfo = new HashMap<>();
                    instanceInfo.put("instanceId", instance.getInstanceId());
                    instanceInfo.put("hostName", instance.getHostName());
                    instanceInfo.put("ipAddress", instance.getIPAddr());
                    instanceInfo.put("port", instance.getPort());
                    instanceInfo.put("status", instance.getStatus().name());
                    instanceInfo.put("healthCheckUrl", instance.getHealthCheckUrl());
                    instanceInfo.put("homePageUrl", instance.getHomePageUrl());
                    instanceInfo.put("lastUpdatedTimestamp", instance.getLastUpdatedTimestamp());
                    
                    instances.add(instanceInfo);
                }
                serviceInfo.put("instances", instances);
                
                logger.info("Found service: {} with {} instances", serviceName, instances.size());
            } else {
                logger.warn("Service not found: {}", serviceName);
                serviceInfo.put("error", "Service not found");
            }
            
        } catch (Exception e) {
            logger.error("Error getting service information for: {}. Error: {}", serviceName, e.getMessage(), e);
            serviceInfo.put("error", e.getMessage());
        }
        
        return serviceInfo;
    }

    /**
     * Get service statistics
     *
     * @return the service statistics
     */
    public Map<String, Object> getServiceStatistics() {
        logger.info("Getting service statistics");

        Map<String, Object> statistics = new HashMap<>();
        
        try {
            List<Application> applications = eurekaClient.getApplications().getRegisteredApplications();
            
            int totalServices = applications.size();
            int totalInstances = 0;
            int upInstances = 0;
            int downInstances = 0;
            
            for (Application application : applications) {
                for (InstanceInfo instance : application.getInstances()) {
                    totalInstances++;
                    if (instance.getStatus() == InstanceInfo.InstanceStatus.UP) {
                        upInstances++;
                    } else {
                        downInstances++;
                    }
                }
            }
            
            statistics.put("totalServices", totalServices);
            statistics.put("totalInstances", totalInstances);
            statistics.put("upInstances", upInstances);
            statistics.put("downInstances", downInstances);
            statistics.put("availabilityPercentage", totalInstances > 0 ? (upInstances * 100.0 / totalInstances) : 0.0);
            
            logger.info("Service statistics - Total: {}, Instances: {}, Up: {}, Down: {}", 
                    totalServices, totalInstances, upInstances, downInstances);
            
        } catch (Exception e) {
            logger.error("Error getting service statistics. Error: {}", e.getMessage(), e);
            statistics.put("error", e.getMessage());
        }
        
        return statistics;
    }

    /**
     * Get application status
     *
     * @param application the application
     * @return the application status
     */
    private String getApplicationStatus(Application application) {
        if (application.getInstances().isEmpty()) {
            return "NO_INSTANCES";
        }
        
        boolean hasUpInstances = application.getInstances().stream()
                .anyMatch(instance -> instance.getStatus() == InstanceInfo.InstanceStatus.UP);
        
        return hasUpInstances ? "UP" : "DOWN";
    }
} 