package com.hotelreservation.reservation.exception;

import java.time.LocalDate;
import java.math.BigDecimal;

/**
 * Invalid Reservation Data Exception
 *
 * Exception thrown when reservation data is invalid.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
public class InvalidReservationDataException extends RuntimeException {

    /**
     * Default constructor
     */
    public InvalidReservationDataException() {
        super("Invalid reservation data");
    }

    /**
     * Constructor with message
     *
     * @param message the error message
     */
    public InvalidReservationDataException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     *
     * @param message the error message
     * @param cause   the cause
     */
    public InvalidReservationDataException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for invalid date range
     *
     * @param checkInDate  the check-in date
     * @param checkOutDate the check-out date
     */
    public InvalidReservationDataException(LocalDate checkInDate, LocalDate checkOutDate) {
        super(String.format("Invalid date range: check-in date %s must be before check-out date %s", 
                checkInDate, checkOutDate));
    }

    /**
     * Constructor for invalid number of guests
     *
     * @param numberOfGuests the number of guests
     */
    public InvalidReservationDataException(Integer numberOfGuests) {
        super(String.format("Invalid number of guests: %d. Must be between 1 and 10", numberOfGuests));
    }

    /**
     * Constructor for invalid price
     *
     * @param price the price
     */
    public InvalidReservationDataException(BigDecimal price) {
        super(String.format("Invalid price: %s. Must be greater than 0", price));
    }

    /**
     * Constructor for invalid guest email
     *
     * @param email the email
     */
    public InvalidReservationDataException(String email, String field) {
        super(String.format("Invalid %s: %s", field, email));
    }

    /**
     * Constructor for missing required field
     *
     * @param field the missing field
     */
    public InvalidReservationDataException(String field, boolean isMissing) {
        super(String.format("Missing required field: %s", field));
    }
} 