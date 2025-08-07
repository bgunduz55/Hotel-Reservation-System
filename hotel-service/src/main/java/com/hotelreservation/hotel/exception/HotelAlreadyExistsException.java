package com.hotelreservation.hotel.exception;

/**
 * Hotel Already Exists Exception
 *
 * Thrown when trying to create a hotel that already exists.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
public class HotelAlreadyExistsException extends RuntimeException {

    /**
     * Constructor with message
     *
     * @param message error message
     */
    public HotelAlreadyExistsException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     *
     * @param message error message
     * @param cause cause of the exception
     */
    public HotelAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with hotel name and city
     *
     * @param name hotel name
     * @param city hotel city
     */
    public HotelAlreadyExistsException(String name, String city) {
        super("Hotel already exists with name: " + name + " and city: " + city);
    }
} 