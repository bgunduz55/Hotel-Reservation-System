package com.hotelreservation.hotel.service;

import com.hotelreservation.hotel.dto.HotelDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Hotel Service Interface
 *
 * Business logic layer for Hotel operations.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
public interface HotelService {

    /**
     * Create a new hotel
     *
     * @param hotelDto hotel data
     * @return created hotel DTO
     */
    HotelDto createHotel(HotelDto hotelDto);

    /**
     * Get hotel by ID
     *
     * @param id hotel ID
     * @return Optional of hotel DTO
     */
    Optional<HotelDto> getHotelById(Long id);

    /**
     * Get all active hotels
     *
     * @return List of hotel DTOs
     */
    List<HotelDto> getAllHotels();

    /**
     * Get all active hotels with pagination
     *
     * @param pageable pagination parameters
     * @return Page of hotel DTOs
     */
    Page<HotelDto> getAllHotels(Pageable pageable);

    /**
     * Update hotel
     *
     * @param id hotel ID
     * @param hotelDto hotel data
     * @return updated hotel DTO
     */
    HotelDto updateHotel(Long id, HotelDto hotelDto);

    /**
     * Delete hotel (soft delete)
     *
     * @param id hotel ID
     */
    void deleteHotel(Long id);

    /**
     * Get hotels by city
     *
     * @param city hotel city
     * @return List of hotel DTOs
     */
    List<HotelDto> getHotelsByCity(String city);

    /**
     * Get hotels by city with pagination
     *
     * @param city hotel city
     * @param pageable pagination parameters
     * @return Page of hotel DTOs
     */
    Page<HotelDto> getHotelsByCity(String city, Pageable pageable);

    /**
     * Get hotels by rating range
     *
     * @param minRating minimum rating
     * @param maxRating maximum rating
     * @return List of hotel DTOs
     */
    List<HotelDto> getHotelsByRatingRange(Integer minRating, Integer maxRating);

    /**
     * Get hotels by name (partial match)
     *
     * @param name hotel name
     * @return List of hotel DTOs
     */
    List<HotelDto> getHotelsByName(String name);

    /**
     * Get hotels by name with pagination
     *
     * @param name hotel name
     * @param pageable pagination parameters
     * @return Page of hotel DTOs
     */
    Page<HotelDto> getHotelsByName(String name, Pageable pageable);

    /**
     * Get hotels with minimum rating
     *
     * @param minRating minimum rating
     * @return List of hotel DTOs
     */
    List<HotelDto> getHotelsByMinimumRating(Integer minRating);

    /**
     * Check if hotel exists
     *
     * @param id hotel ID
     * @return true if hotel exists
     */
    boolean hotelExists(Long id);

    /**
     * Check if hotel exists by name and city
     *
     * @param name hotel name
     * @param city hotel city
     * @return true if hotel exists
     */
    boolean hotelExistsByNameAndCity(String name, String city);

    /**
     * Count hotels by city
     *
     * @param city hotel city
     * @return number of hotels in the city
     */
    long countHotelsByCity(String city);

    /**
     * Get hotels with room count
     *
     * @return List of hotel DTOs with room count
     */
    List<Object[]> getHotelsWithRoomCount();

    /**
     * Get hotels by city with average room price
     *
     * @param city hotel city
     * @return List of hotel DTOs with average room price
     */
    List<Object[]> getHotelsByCityWithAveragePrice(String city);
} 