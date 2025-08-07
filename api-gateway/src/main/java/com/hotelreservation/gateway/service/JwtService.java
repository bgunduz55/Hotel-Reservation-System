package com.hotelreservation.gateway.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT Service
 *
 * Service for JWT token generation and validation.
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

    /**
     * Generate JWT token
     *
     * @param username the username
     * @param roles the user roles
     * @return the JWT token
     */
    public String generateToken(String username, String[] roles) {
        logger.info("Generating JWT token for user: {}", username);

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        claims.put("username", username);

        return createToken(claims, username);
    }

    /**
     * Create JWT token
     *
     * @param claims the claims
     * @param subject the subject
     * @return the JWT token
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 1000);

        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
            
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(subject)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            logger.error("Error creating JWT token for user: {}. Error: {}", subject, e.getMessage(), e);
            throw new RuntimeException("Failed to create JWT token", e);
        }
    }

    /**
     * Validate JWT token
     *
     * @param token the JWT token
     * @param username the username
     * @return true if valid, false otherwise
     */
    public Boolean validateToken(String token, String username) {
        try {
            final String tokenUsername = extractUsername(token);
            return (username.equals(tokenUsername) && !isTokenExpired(token));
        } catch (Exception e) {
            logger.error("Error validating JWT token for user: {}. Error: {}", username, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Extract username from token
     *
     * @param token the JWT token
     * @return the username
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract expiration date from token
     *
     * @param token the JWT token
     * @return the expiration date
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extract roles from token
     *
     * @param token the JWT token
     * @return the roles
     */
    public String[] extractRoles(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Object rolesObj = claims.get("roles");
            
            if (rolesObj instanceof java.util.List) {
                @SuppressWarnings("unchecked")
                java.util.List<String> rolesList = (java.util.List<String>) rolesObj;
                return rolesList.toArray(new String[0]);
            } else if (rolesObj instanceof String[]) {
                return (String[]) rolesObj;
            } else {
                logger.warn("Unexpected roles type: {}", rolesObj != null ? rolesObj.getClass() : "null");
                return new String[0];
            }
        } catch (Exception e) {
            logger.error("Error extracting roles from JWT token. Error: {}", e.getMessage(), e);
            return new String[0];
        }
    }

    /**
     * Extract claim from token
     *
     * @param token the JWT token
     * @param claimsResolver the claims resolver
     * @param <T> the type
     * @return the claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract all claims from token
     *
     * @param token the JWT token
     * @return the claims
     */
    private Claims extractAllClaims(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            logger.warn("JWT token expired: {}", e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token: {}", e.getMessage(), e);
            throw e;
        } catch (MalformedJwtException e) {
            logger.error("Malformed JWT token: {}", e.getMessage(), e);
            throw e;
        } catch (SecurityException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage(), e);
            throw e;
        } catch (IllegalArgumentException e) {
            logger.error("JWT token is empty: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Check if token is expired
     *
     * @param token the JWT token
     * @return true if expired, false otherwise
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Get token expiration time in seconds
     *
     * @return the expiration time
     */
    public Long getExpiration() {
        return expiration;
    }
} 