package com.hotelreservation.hotel.service;

import com.hotelreservation.hotel.dto.RoomDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Room Service Interface
 *
 * Business logic layer for Room operations.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
public interface RoomService {

    /**
     * Create a new room
     *
     * @param roomDto room data
     * @return created room DTO
     */
    RoomDto createRoom(RoomDto roomDto);

    /**
     * Get room by ID
     *
     * @param id room ID
     * @return Optional of room DTO
     */
    Optional<RoomDto> getRoomById(Long id);

    /**
     * Get all active rooms
     *
     * @return List of room DTOs
     */
    List<RoomDto> getAllRooms();

    /**
     * Get all active rooms with pagination
     *
     * @param pageable pagination parameters
     * @return Page of room DTOs
     */
    Page<RoomDto> getAllRooms(Pageable pageable);

    /**
     * Update room
     *
     * @param id room ID
     * @param roomDto room data
     * @return updated room DTO
     */
    RoomDto updateRoom(Long id, RoomDto roomDto);

    /**
     * Delete room (soft delete)
     *
     * @param id room ID
     */
    void deleteRoom(Long id);

    /**
     * Get rooms by hotel ID
     *
     * @param hotelId hotel ID
     * @return List of room DTOs
     */
    List<RoomDto> getRoomsByHotelId(Long hotelId);

    /**
     * Get rooms by hotel ID with pagination
     *
     * @param hotelId hotel ID
     * @param pageable pagination parameters
     * @return Page of room DTOs
     */
    Page<RoomDto> getRoomsByHotelId(Long hotelId, Pageable pageable);

    /**
     * Get available rooms by hotel ID
     *
     * @param hotelId hotel ID
     * @return List of available room DTOs
     */
    List<RoomDto> getAvailableRoomsByHotelId(Long hotelId);

    /**
     * Get rooms by room type
     *
     * @param roomType room type
     * @return List of room DTOs
     */
    List<RoomDto> getRoomsByType(String roomType);

    /**
     * Get rooms by room type and hotel ID
     *
     * @param roomType room type
     * @param hotelId hotel ID
     * @return List of room DTOs
     */
    List<RoomDto> getRoomsByTypeAndHotelId(String roomType, Long hotelId);

    /**
     * Get rooms by capacity range
     *
     * @param minCapacity minimum capacity
     * @param maxCapacity maximum capacity
     * @return List of room DTOs
     */
    List<RoomDto> getRoomsByCapacityRange(Integer minCapacity, Integer maxCapacity);

    /**
     * Get rooms by price range
     *
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return List of room DTOs
     */
    List<RoomDto> getRoomsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Get rooms by hotel ID and price range
     *
     * @param hotelId hotel ID
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return List of room DTOs
     */
    List<RoomDto> getRoomsByHotelIdAndPriceRange(Long hotelId, BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Get available rooms by hotel ID and room type
     *
     * @param hotelId hotel ID
     * @param roomType room type
     * @return List of available room DTOs
     */
    List<RoomDto> getAvailableRoomsByHotelIdAndType(Long hotelId, String roomType);

    /**
     * Get room by hotel ID and room number
     *
     * @param hotelId hotel ID
     * @param roomNumber room number
     * @return Optional of room DTO
     */
    Optional<RoomDto> getRoomByHotelIdAndNumber(Long hotelId, String roomNumber);

    /**
     * Mark room as unavailable
     *
     * @param id room ID
     * @return updated room DTO
     */
    RoomDto markRoomAsUnavailable(Long id);

    /**
     * Mark room as available
     *
     * @param id room ID
     * @return updated room DTO
     */
    RoomDto markRoomAsAvailable(Long id);

    /**
     * Check if room exists
     *
     * @param id room ID
     * @return true if room exists
     */
    boolean roomExists(Long id);

    /**
     * Check if room exists by hotel ID and room number
     *
     * @param hotelId hotel ID
     * @param roomNumber room number
     * @return true if room exists
     */
    boolean roomExistsByHotelIdAndNumber(Long hotelId, String roomNumber);

    /**
     * Count rooms by hotel ID
     *
     * @param hotelId hotel ID
     * @return number of rooms for the hotel
     */
    long countRoomsByHotelId(Long hotelId);

    /**
     * Count available rooms by hotel ID
     *
     * @param hotelId hotel ID
     * @return number of available rooms for the hotel
     */
    long countAvailableRoomsByHotelId(Long hotelId);

    /**
     * Get rooms with hotel information
     *
     * @param hotelId hotel ID
     * @return List of rooms with hotel details
     */
    List<Object[]> getRoomsWithHotelInfo(Long hotelId);

    /**
     * Get average room price by hotel ID
     *
     * @param hotelId hotel ID
     * @return average room price for the hotel
     */
    BigDecimal getAverageRoomPriceByHotelId(Long hotelId);

    /**
     * Get rooms by capacity and availability
     *
     * @param capacity room capacity
     * @param available availability status
     * @return List of room DTOs
     */
    List<RoomDto> getRoomsByCapacityAndAvailability(Integer capacity, Boolean available);
} 