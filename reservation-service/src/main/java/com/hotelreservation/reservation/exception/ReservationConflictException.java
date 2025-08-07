package com.hotelreservation.reservation.exception;

import java.time.LocalDate;

/**
 * Reservation Conflict Exception
 *
 * Exception thrown when there is a conflict with an existing reservation.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
public class ReservationConflictException extends RuntimeException {

    /**
     * Default constructor
     */
    public ReservationConflictException() {
        super("Reservation conflict detected");
    }

    /**
     * Constructor with message
     *
     * @param message the error message
     */
    public ReservationConflictException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     *
     * @param message the error message
     * @param cause   the cause
     */
    public ReservationConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with conflict details
     *
     * @param roomId       the room ID
     * @param checkInDate  the check-in date
     * @param checkOutDate the check-out date
     */
    public ReservationConflictException(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        super(String.format("Reservation conflict detected for room %d between %s and %s", 
                roomId, checkInDate, checkOutDate));
    }

    /**
     * Constructor with conflict details and additional message
     *
     * @param roomId       the room ID
     * @param checkInDate  the check-in date
     * @param checkOutDate the check-out date
     * @param message      additional message
     */
    public ReservationConflictException(Long roomId, LocalDate checkInDate, LocalDate checkOutDate, String message) {
        super(String.format("Reservation conflict detected for room %d between %s and %s. %s", 
                roomId, checkInDate, checkOutDate, message));
    }

    /**
     * Constructor with hotel and room details
     *
     * @param hotelId      the hotel ID
     * @param roomId       the room ID
     * @param checkInDate  the check-in date
     * @param checkOutDate the check-out date
     */
    public ReservationConflictException(Long hotelId, Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        super(String.format("Reservation conflict detected for hotel %d, room %d between %s and %s", 
                hotelId, roomId, checkInDate, checkOutDate));
    }
} 