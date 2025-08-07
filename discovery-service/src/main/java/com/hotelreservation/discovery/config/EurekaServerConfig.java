package com.hotelreservation.discovery.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.eureka.server.EurekaServerConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Eureka Server Configuration
 *
 * Configuration for Eureka Server (Service Discovery).
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@Configuration
public class EurekaServerConfig {

    private static final Logger logger = LoggerFactory.getLogger(EurekaServerConfig.class);

    /**
     * Eureka Server Config Bean
     *
     * @return the eureka server config bean
     */
    @Bean
    public EurekaServerConfigBean eurekaServerConfigBean() {
        logger.info("Configuring Eureka Server");

        EurekaServerConfigBean config = new EurekaServerConfigBean();
        
        // Enable self-preservation mode
        config.setEnableSelfPreservation(true);
        
        // Set renewal threshold
        config.setRenewalThresholdUpdateIntervalMs(60000);
        
        // Set peer node timeout
        config.setPeerNodeConnectTimeoutMs(1000);
        config.setPeerNodeReadTimeoutMs(5000);
        
        // Set registry sync timeout
        config.setRegistrySyncRetries(5);
        config.setRegistrySyncRetryWaitMs(30000);
        
        // Set eviction interval
        config.setEvictionIntervalTimerInMs(60000);
        
        // Set response cache update interval
        config.setResponseCacheUpdateIntervalMs(3000);
        
        // Set peer node total connections
        config.setPeerNodeTotalConnections(1000);
        config.setPeerNodeTotalConnectionsPerHost(500);
        
        // Set peer node connection idle timeout
        config.setPeerNodeConnectionIdleTimeoutSeconds(30);
        
        logger.info("Eureka Server configuration completed");
        
        return config;
    }
} 