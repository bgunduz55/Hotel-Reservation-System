package com.hotelreservation.hotel.exception;

/**
 * Room Not Found Exception
 *
 * Thrown when a room is not found in the system.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
public class RoomNotFoundException extends RuntimeException {

    /**
     * Constructor with message
     *
     * @param message error message
     */
    public RoomNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     *
     * @param message error message
     * @param cause cause of the exception
     */
    public RoomNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with room ID
     *
     * @param roomId room ID
     */
    public RoomNotFoundException(Long roomId) {
        super("Room not found with ID: " + roomId);
    }

    /**
     * Constructor with hotel ID and room number
     *
     * @param hotelId hotel ID
     * @param roomNumber room number
     */
    public RoomNotFoundException(Long hotelId, String roomNumber) {
        super("Room not found with hotel ID: " + hotelId + " and room number: " + roomNumber);
    }
} 