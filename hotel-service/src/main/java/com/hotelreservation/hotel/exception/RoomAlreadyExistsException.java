package com.hotelreservation.hotel.exception;

/**
 * Room Already Exists Exception
 *
 * Thrown when trying to create a room that already exists.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
public class RoomAlreadyExistsException extends RuntimeException {

    /**
     * Constructor with message
     *
     * @param message error message
     */
    public RoomAlreadyExistsException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     *
     * @param message error message
     * @param cause cause of the exception
     */
    public RoomAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with hotel ID and room number
     *
     * @param hotelId hotel ID
     * @param roomNumber room number
     */
    public RoomAlreadyExistsException(Long hotelId, String roomNumber) {
        super("Room already exists with hotel ID: " + hotelId + " and room number: " + roomNumber);
    }
} 