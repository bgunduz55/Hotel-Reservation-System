package com.hotelreservation.user.repository;

import com.hotelreservation.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * User Repository
 *
 * Data access layer for User entity.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by username
     *
     * @param username the username
     * @return Optional<User>
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by email
     *
     * @param email the email
     * @return Optional<User>
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by username or email
     *
     * @param username the username
     * @param email the email
     * @return Optional<User>
     */
    Optional<User> findByUsernameOrEmail(String username, String email);

    /**
     * Check if user exists by username
     *
     * @param username the username
     * @return boolean
     */
    boolean existsByUsername(String username);

    /**
     * Check if user exists by email
     *
     * @param email the email
     * @return boolean
     */
    boolean existsByEmail(String email);

    /**
     * Find all active users
     *
     * @return List<User>
     */
    List<User> findByActiveTrue();

    /**
     * Find users by role
     *
     * @param role the role
     * @return List<User>
     */
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role AND u.active = true")
    List<User> findByRole(@Param("role") String role);

    /**
     * Find user by username and active status
     *
     * @param username the username
     * @param active the active status
     * @return Optional<User>
     */
    Optional<User> findByUsernameAndActive(String username, Boolean active);
} 