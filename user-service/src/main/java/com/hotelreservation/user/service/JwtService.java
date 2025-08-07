package com.hotelreservation.user.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * JWT Service
 *
 * Service for JWT token operations.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secret:defaultSecretKeyForDevelopmentOnly}")
    private String secret;

    @Value("${jwt.expiration:86400}")
    private Long expiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Generate JWT token
     *
     * @param username the username
     * @param roles the user roles
     * @return the JWT token
     */
    public String generateToken(String username, List<String> roles) {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiry = now.plusSeconds(expiration);

            return Jwts.builder()
                    .setSubject(username)
                    .claim("username", username)
                    .claim("roles", roles)
                    .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                    .setExpiration(Date.from(expiry.atZone(ZoneId.systemDefault()).toInstant()))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            logger.error("Error generating JWT token for user {}: {}", username, e.getMessage(), e);
            throw new RuntimeException("Error generating JWT token", e);
        }
    }

    /**
     * Validate JWT token
     *
     * @param token the JWT token
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Extract username from JWT token
     *
     * @param token the JWT token
     * @return the username
     */
    public String extractUsername(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.get("username", String.class);
        } catch (Exception e) {
            logger.error("Error extracting username from JWT token: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Extract roles from JWT token
     *
     * @param token the JWT token
     * @return the roles
     */
    public List<String> extractRoles(String token) {
        try {
            Claims claims = extractAllClaims(token);
            @SuppressWarnings("unchecked")
            List<String> roles = claims.get("roles", List.class);
            return roles;
        } catch (Exception e) {
            logger.error("Error extracting roles from JWT token: {}", e.getMessage(), e);
            return List.of("USER");
        }
    }

    /**
     * Extract issued at time from JWT token
     *
     * @param token the JWT token
     * @return the issued at time
     */
    public LocalDateTime extractIssuedAt(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Date issuedAt = claims.getIssuedAt();
            return issuedAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        } catch (Exception e) {
            logger.error("Error extracting issued at time from JWT token: {}", e.getMessage(), e);
            return LocalDateTime.now();
        }
    }

    /**
     * Extract expiration time from JWT token
     *
     * @param token the JWT token
     * @return the expiration time
     */
    public LocalDateTime extractExpiration(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Date expiration = claims.getExpiration();
            return expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        } catch (Exception e) {
            logger.error("Error extracting expiration time from JWT token: {}", e.getMessage(), e);
            return LocalDateTime.now().plusSeconds(expiration);
        }
    }

    /**
     * Extract all claims from JWT token
     *
     * @param token the JWT token
     * @return the claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
} 