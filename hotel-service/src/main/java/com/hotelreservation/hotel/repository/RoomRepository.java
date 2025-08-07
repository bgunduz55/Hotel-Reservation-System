package com.hotelreservation.hotel.repository;

import com.hotelreservation.hotel.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Room Repository
 *
 * Data access layer for Room entity with custom queries.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    /**
     * Find room by ID and active status
     *
     * @param id room ID
     * @return Optional of Room
     */
    Optional<Room> findByIdAndActiveTrue(Long id);

    /**
     * Find all active rooms
     *
     * @return List of active rooms
     */
    List<Room> findByActiveTrue();

    /**
     * Find all active rooms with pagination
     *
     * @param pageable pagination parameters
     * @return Page of active rooms
     */
    Page<Room> findByActiveTrue(Pageable pageable);

    /**
     * Find rooms by hotel ID and active status
     *
     * @param hotelId hotel ID
     * @return List of rooms for the specified hotel
     */
    List<Room> findByHotelIdAndActiveTrue(Long hotelId);

    /**
     * Find rooms by hotel ID with pagination
     *
     * @param hotelId hotel ID
     * @param pageable pagination parameters
     * @return Page of rooms for the specified hotel
     */
    Page<Room> findByHotelIdAndActiveTrue(Long hotelId, Pageable pageable);

    /**
     * Find available rooms by hotel ID
     *
     * @param hotelId hotel ID
     * @return List of available rooms for the specified hotel
     */
    List<Room> findByHotelIdAndAvailableTrueAndActiveTrue(Long hotelId);

    /**
     * Find rooms by room type and active status
     *
     * @param roomType room type
     * @return List of rooms with the specified type
     */
    List<Room> findByRoomTypeAndActiveTrue(String roomType);

    /**
     * Find rooms by room type and hotel ID
     *
     * @param roomType room type
     * @param hotelId hotel ID
     * @return List of rooms with the specified type and hotel
     */
    List<Room> findByRoomTypeAndHotelIdAndActiveTrue(String roomType, Long hotelId);

    /**
     * Find rooms by capacity range and active status
     *
     * @param minCapacity minimum capacity
     * @param maxCapacity maximum capacity
     * @return List of rooms within the capacity range
     */
    List<Room> findByCapacityBetweenAndActiveTrue(Integer minCapacity, Integer maxCapacity);

    /**
     * Find rooms by price range and active status
     *
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return List of rooms within the price range
     */
    List<Room> findByPricePerNightBetweenAndActiveTrue(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Find rooms by hotel ID and price range
     *
     * @param hotelId hotel ID
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return List of rooms within the price range for the specified hotel
     */
    List<Room> findByHotelIdAndPricePerNightBetweenAndActiveTrue(Long hotelId, BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Find available rooms by hotel ID and room type
     *
     * @param hotelId hotel ID
     * @param roomType room type
     * @return List of available rooms with the specified type
     */
    List<Room> findByHotelIdAndRoomTypeAndAvailableTrueAndActiveTrue(Long hotelId, String roomType);

    /**
     * Find room by hotel ID and room number
     *
     * @param hotelId hotel ID
     * @param roomNumber room number
     * @return Optional of Room
     */
    Optional<Room> findByHotelIdAndRoomNumberAndActiveTrue(Long hotelId, String roomNumber);

    /**
     * Check if room exists by hotel ID and room number
     *
     * @param hotelId hotel ID
     * @param roomNumber room number
     * @return true if room exists
     */
    boolean existsByHotelIdAndRoomNumberAndActiveTrue(Long hotelId, String roomNumber);

    /**
     * Count rooms by hotel ID
     *
     * @param hotelId hotel ID
     * @return number of rooms for the hotel
     */
    long countByHotelIdAndActiveTrue(Long hotelId);

    /**
     * Count available rooms by hotel ID
     *
     * @param hotelId hotel ID
     * @return number of available rooms for the hotel
     */
    long countByHotelIdAndAvailableTrueAndActiveTrue(Long hotelId);

    /**
     * Custom query to find rooms with hotel information
     *
     * @param hotelId hotel ID
     * @return List of rooms with hotel details
     */
    @Query("SELECT r, h.name as hotelName FROM Room r JOIN r.hotel h " +
           "WHERE r.hotel.id = :hotelId AND r.active = true")
    List<Object[]> findRoomsWithHotelInfo(@Param("hotelId") Long hotelId);

    /**
     * Custom query to find average room price by hotel
     *
     * @param hotelId hotel ID
     * @return average room price for the hotel
     */
    @Query("SELECT AVG(r.pricePerNight) FROM Room r " +
           "WHERE r.hotel.id = :hotelId AND r.active = true")
    BigDecimal findAveragePriceByHotelId(@Param("hotelId") Long hotelId);

    /**
     * Custom query to find rooms by capacity and availability
     *
     * @param capacity room capacity
     * @param available availability status
     * @return List of rooms matching the criteria
     */
    @Query("SELECT r FROM Room r WHERE r.capacity = :capacity AND r.available = :available AND r.active = true")
    List<Room> findByCapacityAndAvailability(@Param("capacity") Integer capacity, @Param("available") Boolean available);
} 