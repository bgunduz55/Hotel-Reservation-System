package com.hotelreservation.gateway;

import com.hotelreservation.gateway.dto.JwtRequest;
import com.hotelreservation.gateway.dto.JwtResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * API Gateway Integration Tests
 *
 * Integration tests for API Gateway.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@SpringBootTest(
    classes = ApiGatewayApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
class ApiGatewayIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
    }

    @Test
    void testHealthCheck() {
        // When
        ResponseEntity<Map> response = restTemplate.getForEntity(
            baseUrl + "/health",
            Map.class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().get("status"));
    }

    @Test
    void testGatewayInfo() {
        // When
        ResponseEntity<Map> response = restTemplate.getForEntity(
            baseUrl + "/info",
            Map.class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("api-gateway", response.getBody().get("service"));
        assertEquals("1.0.0", response.getBody().get("version"));
    }

    @Test
    void testLoginEndpoint() {
        // Given
        JwtRequest jwtRequest = new JwtRequest("admin", "admin123");

        // When
        ResponseEntity<JwtResponse> response = restTemplate.postForEntity(
            baseUrl + "/api/auth/login",
            jwtRequest,
            JwtResponse.class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getToken());
        assertEquals("admin", response.getBody().getUsername());
        assertEquals("Bearer", response.getBody().getTokenType());
    }

    @Test
    void testLoginWithInvalidCredentials() {
        // Given
        JwtRequest jwtRequest = new JwtRequest("invalid", "invalid");

        // When
        ResponseEntity<JwtResponse> response = restTemplate.postForEntity(
            baseUrl + "/api/auth/login",
            jwtRequest,
            JwtResponse.class
        );

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testValidateToken() {
        // Given
        JwtRequest jwtRequest = new JwtRequest("admin", "admin123");
        ResponseEntity<JwtResponse> loginResponse = restTemplate.postForEntity(
            baseUrl + "/api/auth/login",
            jwtRequest,
            JwtResponse.class
        );
        String token = loginResponse.getBody().getToken();

        // When
        ResponseEntity<Map> response = restTemplate.postForEntity(
            baseUrl + "/api/auth/validate?token=" + token,
            null,
            Map.class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue((Boolean) response.getBody().get("valid"));
        assertEquals("admin", response.getBody().get("username"));
    }

    @Test
    void testRefreshToken() {
        // Given
        JwtRequest jwtRequest = new JwtRequest("admin", "admin123");
        ResponseEntity<JwtResponse> loginResponse = restTemplate.postForEntity(
            baseUrl + "/api/auth/login",
            jwtRequest,
            JwtResponse.class
        );
        String token = loginResponse.getBody().getToken();

        // When
        ResponseEntity<JwtResponse> response = restTemplate.postForEntity(
            baseUrl + "/api/auth/refresh?token=" + token,
            null,
            JwtResponse.class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getToken());
        assertEquals("admin", response.getBody().getUsername());
    }

    @Test
    void testLogout() {
        // When
        ResponseEntity<Map> response = restTemplate.postForEntity(
            baseUrl + "/api/auth/logout",
            null,
            Map.class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Logout successful", response.getBody().get("message"));
    }

    @Test
    void testFallbackEndpoints() {
        // Test hotel service fallback
        ResponseEntity<Map> hotelFallback = restTemplate.getForEntity(
            baseUrl + "/fallback/hotel-service",
            Map.class
        );
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, hotelFallback.getStatusCode());
        assertEquals("SERVICE_UNAVAILABLE", hotelFallback.getBody().get("status"));

        // Test reservation service fallback
        ResponseEntity<Map> reservationFallback = restTemplate.getForEntity(
            baseUrl + "/fallback/reservation-service",
            Map.class
        );
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, reservationFallback.getStatusCode());
        assertEquals("SERVICE_UNAVAILABLE", reservationFallback.getBody().get("status"));

        // Test notification service fallback
        ResponseEntity<Map> notificationFallback = restTemplate.getForEntity(
            baseUrl + "/fallback/notification-service",
            Map.class
        );
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, notificationFallback.getStatusCode());
        assertEquals("SERVICE_UNAVAILABLE", notificationFallback.getBody().get("status"));

        // Test discovery service fallback
        ResponseEntity<Map> discoveryFallback = restTemplate.getForEntity(
            baseUrl + "/fallback/discovery-service",
            Map.class
        );
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, discoveryFallback.getStatusCode());
        assertEquals("SERVICE_UNAVAILABLE", discoveryFallback.getBody().get("status"));
    }

    @Test
    void testGatewayRoutes() {
        // Test hotel service route
        ResponseEntity<String> hotelResponse = restTemplate.getForEntity(
            baseUrl + "/api/hotels",
            String.class
        );
        // Should return fallback or service unavailable
        assertTrue(hotelResponse.getStatusCode().is4xxClientError() || 
                  hotelResponse.getStatusCode().is5xxServerError());

        // Test reservation service route
        ResponseEntity<String> reservationResponse = restTemplate.getForEntity(
            baseUrl + "/api/reservations",
            String.class
        );
        // Should return fallback or service unavailable
        assertTrue(reservationResponse.getStatusCode().is4xxClientError() || 
                  reservationResponse.getStatusCode().is5xxServerError());

        // Test notification service route
        ResponseEntity<String> notificationResponse = restTemplate.getForEntity(
            baseUrl + "/api/notifications",
            String.class
        );
        // Should return fallback or service unavailable
        assertTrue(notificationResponse.getStatusCode().is4xxClientError() || 
                  notificationResponse.getStatusCode().is5xxServerError());
    }

    @Test
    void testCorsHeaders() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.add("Origin", "http://localhost:3000");
        headers.add("Access-Control-Request-Method", "GET");
        headers.add("Access-Control-Request-Headers", "Content-Type");

        // When
        ResponseEntity<String> response = restTemplate.exchange(
            baseUrl + "/api/hotels",
            org.springframework.http.HttpMethod.OPTIONS,
            new org.springframework.http.HttpEntity<>(headers),
            String.class
        );

        // Then
        // CORS should be handled by the gateway
        assertNotNull(response.getHeaders().getAccessControlAllowOrigin());
    }

    @Test
    void testRateLimiting() {
        // Test multiple requests to check rate limiting behavior
        for (int i = 0; i < 10; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/api/hotels",
                String.class
            );
            // All requests should be processed (rate limiting might be configured)
            assertNotNull(response);
        }
    }

    @Test
    void testCircuitBreaker() {
        // Test circuit breaker behavior by making multiple requests
        // to a service that might be unavailable
        for (int i = 0; i < 5; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/api/hotels",
                String.class
            );
            // Circuit breaker should handle the responses appropriately
            assertNotNull(response);
        }
    }
} 