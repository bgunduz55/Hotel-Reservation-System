package com.hotelreservation.reservation.entity;

/**
 * Reservation Status Enum
 *
 * Enumeration for reservation status values.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
public enum ReservationStatus {
    
    /**
     * Reservation is pending confirmation
     */
    PENDING("Pending"),
    
    /**
     * Reservation is confirmed
     */
    CONFIRMED("Confirmed"),
    
    /**
     * Reservation is cancelled
     */
    CANCELLED("Cancelled"),
    
    /**
     * Reservation is completed
     */
    COMPLETED("Completed");

    private final String displayName;

    ReservationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
} 