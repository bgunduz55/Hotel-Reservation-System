package com.hotelreservation.gateway.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Load Balancer Configuration
 *
 * Configuration for Spring Cloud LoadBalancer.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@Configuration
@LoadBalancerClient(name = "hotel-service", configuration = LoadBalancerConfig.class)
public class LoadBalancerConfig {

    /**
     * Load balanced WebClient bean
     *
     * @return the WebClient
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }
} 