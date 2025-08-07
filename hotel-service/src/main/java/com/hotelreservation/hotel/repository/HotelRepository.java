package com.hotelreservation.hotel.repository;

import com.hotelreservation.hotel.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Hotel Repository
 *
 * Data access layer for Hotel entity with custom queries.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    /**
     * Find hotel by ID and active status
     *
     * @param id hotel ID
     * @return Optional of Hotel
     */
    Optional<Hotel> findByIdAndActiveTrue(Long id);

    /**
     * Find all active hotels
     *
     * @return List of active hotels
     */
    List<Hotel> findByActiveTrue();

    /**
     * Find all active hotels with pagination
     *
     * @param pageable pagination parameters
     * @return Page of active hotels
     */
    Page<Hotel> findByActiveTrue(Pageable pageable);

    /**
     * Find hotels by city and active status
     *
     * @param city hotel city
     * @return List of hotels in the specified city
     */
    List<Hotel> findByCityAndActiveTrue(String city);

    /**
     * Find hotels by city with pagination
     *
     * @param city hotel city
     * @param pageable pagination parameters
     * @return Page of hotels in the specified city
     */
    Page<Hotel> findByCityAndActiveTrue(String city, Pageable pageable);

    /**
     * Find hotels by rating range and active status
     *
     * @param minRating minimum rating
     * @param maxRating maximum rating
     * @return List of hotels within the rating range
     */
    List<Hotel> findByRatingBetweenAndActiveTrue(Integer minRating, Integer maxRating);

    /**
     * Find hotels by name containing (case-insensitive) and active status
     *
     * @param name hotel name (partial match)
     * @return List of hotels matching the name
     */
    List<Hotel> findByNameContainingIgnoreCaseAndActiveTrue(String name);

    /**
     * Find hotels by name containing with pagination
     *
     * @param name hotel name (partial match)
     * @param pageable pagination parameters
     * @return Page of hotels matching the name
     */
    Page<Hotel> findByNameContainingIgnoreCaseAndActiveTrue(String name, Pageable pageable);

    /**
     * Custom query to find hotels with rooms count
     *
     * @return List of hotels with room count
     */
    @Query("SELECT h, COUNT(r) as roomCount FROM Hotel h LEFT JOIN h.rooms r " +
           "WHERE h.active = true GROUP BY h")
    List<Object[]> findHotelsWithRoomCount();

    /**
     * Custom query to find hotels by city with average room price
     *
     * @param city hotel city
     * @return List of hotels with average room price
     */
    @Query("SELECT h, AVG(r.pricePerNight) as avgPrice FROM Hotel h LEFT JOIN h.rooms r " +
           "WHERE h.city = :city AND h.active = true GROUP BY h")
    List<Object[]> findHotelsByCityWithAveragePrice(@Param("city") String city);

    /**
     * Check if hotel exists by name and city
     *
     * @param name hotel name
     * @param city hotel city
     * @return true if hotel exists
     */
    boolean existsByNameAndCityAndActiveTrue(String name, String city);

    /**
     * Count hotels by city
     *
     * @param city hotel city
     * @return number of hotels in the city
     */
    long countByCityAndActiveTrue(String city);

    /**
     * Find hotels with minimum rating
     *
     * @param minRating minimum rating
     * @return List of hotels with minimum rating
     */
    List<Hotel> findByRatingGreaterThanEqualAndActiveTrue(Integer minRating);
} 