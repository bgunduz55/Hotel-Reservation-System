package com.hotelreservation.gateway.filter;

import com.hotelreservation.gateway.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * JWT Authentication Filter
 *
 * Global filter for JWT token validation.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;

    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        // Skip authentication for public endpoints
        if (isPublicEndpoint(path)) {
            logger.debug("Skipping JWT authentication for public endpoint: {}", path);
            return chain.filter(exchange);
        }

        String token = extractToken(request);
        
        if (!StringUtils.hasText(token)) {
            logger.warn("No JWT token found in request for path: {}", path);
            return unauthorizedResponse(exchange, "No JWT token provided");
        }

        try {
            // Validate token
            String username = jwtService.extractUsername(token);
            if (!jwtService.validateToken(token, username)) {
                logger.warn("Invalid JWT token for user: {} on path: {}", username, path);
                return unauthorizedResponse(exchange, "Invalid JWT token");
            }

            // Extract roles
            String[] roles = jwtService.extractRoles(token);

            // Add user information to headers
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-User-Name", username)
                    .header("X-User-Roles", String.join(",", roles))
                    .build();

            logger.debug("JWT authentication successful for user: {} with roles: {} on path: {}", 
                    username, String.join(",", roles), path);

            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (Exception e) {
            logger.error("Error during JWT authentication for path: {}. Error: {}", path, e.getMessage(), e);
            return unauthorizedResponse(exchange, "JWT authentication failed");
        }
    }

    /**
     * Extract JWT token from request
     *
     * @param request the server HTTP request
     * @return the JWT token
     */
    private String extractToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(AUTHORIZATION_HEADER);
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        
        return null;
    }

    /**
     * Check if endpoint is public
     *
     * @param path the request path
     * @return true if public, false otherwise
     */
    private boolean isPublicEndpoint(String path) {
        return path.startsWith("/health") ||
               path.startsWith("/actuator") ||
               path.startsWith("/fallback") ||
               path.startsWith("/swagger") ||
               path.startsWith("/v3/api-docs") ||
               path.startsWith("/api/auth") ||
               path.startsWith("/user-service/api/auth") ||
               path.startsWith("/api/public");
    }

    /**
     * Return unauthorized response
     *
     * @param exchange the server web exchange
     * @param message the error message
     * @return the unauthorized response
     */
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
        
        String errorResponse = String.format(
                "{\"timestamp\":\"%s\",\"status\":401,\"error\":\"Unauthorized\",\"message\":\"%s\",\"path\":\"%s\"}",
                java.time.LocalDateTime.now(),
                message,
                exchange.getRequest().getPath().value()
        );
        
        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory().wrap(errorResponse.getBytes()))
        );
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1; // After LoggingFilter
    }
} 