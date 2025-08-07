package com.hotelreservation.hotel.exception;

/**
 * Hotel Not Found Exception
 *
 * Thrown when a hotel is not found in the system.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
public class HotelNotFoundException extends RuntimeException {

    /**
     * Constructor with message
     *
     * @param message error message
     */
    public HotelNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     *
     * @param message error message
     * @param cause cause of the exception
     */
    public HotelNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with hotel ID
     *
     * @param hotelId hotel ID
     */
    public HotelNotFoundException(Long hotelId) {
        super("Hotel not found with ID: " + hotelId);
    }
} 