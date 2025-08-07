package com.hotelreservation.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logging Filter
 *
 * Global filter for logging all requests and responses.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        String method = request.getMethod().name();
        String remoteAddress = request.getRemoteAddress() != null ? 
                request.getRemoteAddress().getAddress().getHostAddress() : "unknown";
        String userAgent = request.getHeaders().getFirst("User-Agent");
        String correlationId = request.getHeaders().getFirst("X-Correlation-ID");

        logger.info("Incoming request - Method: {}, Path: {}, Remote Address: {}, User-Agent: {}, Correlation-ID: {}, Timestamp: {}", 
                method, path, remoteAddress, userAgent, correlationId, LocalDateTime.now().format(formatter));

        long startTime = System.currentTimeMillis();

        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;
                    int statusCode = exchange.getResponse().getStatusCode() != null ? 
                            exchange.getResponse().getStatusCode().value() : 0;

                    logger.info("Request completed - Method: {}, Path: {}, Status: {}, Duration: {}ms, Correlation-ID: {}, Timestamp: {}", 
                            method, path, statusCode, duration, correlationId, LocalDateTime.now().format(formatter));
                }));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
} 