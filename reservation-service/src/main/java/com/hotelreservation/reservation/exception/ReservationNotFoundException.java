package com.hotelreservation.reservation.exception;

/**
 * Reservation Not Found Exception
 *
 * Exception thrown when a requested reservation is not found.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
public class ReservationNotFoundException extends RuntimeException {

    /**
     * Default constructor
     */
    public ReservationNotFoundException() {
        super("Reservation not found");
    }

    /**
     * Constructor with message
     *
     * @param message the error message
     */
    public ReservationNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     *
     * @param message the error message
     * @param cause   the cause
     */
    public ReservationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with reservation ID
     *
     * @param reservationId the reservation ID
     */
    public ReservationNotFoundException(Long reservationId) {
        super("Reservation not found with ID: " + reservationId);
    }

    /**
     * Constructor with reservation ID and additional details
     *
     * @param reservationId the reservation ID
     * @param details      additional details
     */
    public ReservationNotFoundException(Long reservationId, String details) {
        super("Reservation not found with ID: " + reservationId + ". " + details);
    }
} 