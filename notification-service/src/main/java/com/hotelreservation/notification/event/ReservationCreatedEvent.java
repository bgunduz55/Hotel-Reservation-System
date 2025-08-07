package com.hotelreservation.notification.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Reservation Created Event
 *
 * Event received from reservation service when a new reservation is created.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
public class ReservationCreatedEvent {

    @JsonProperty("reservation_id")
    private Long reservationId;

    @JsonProperty("hotel_id")
    private Long hotelId;

    @JsonProperty("room_id")
    private Long roomId;

    @JsonProperty("guest_name")
    private String guestName;

    @JsonProperty("guest_email")
    private String guestEmail;

    @JsonProperty("guest_phone")
    private String guestPhone;

    @JsonProperty("check_in_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkInDate;

    @JsonProperty("check_out_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOutDate;

    @JsonProperty("number_of_guests")
    private Integer numberOfGuests;

    @JsonProperty("total_price")
    private BigDecimal totalPrice;

    @JsonProperty("status")
    private String status;

    @JsonProperty("special_requests")
    private String specialRequests;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonProperty("event_type")
    private String eventType;

    @JsonProperty("event_timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime eventTimestamp;

    /**
     * Default constructor
     */
    public ReservationCreatedEvent() {
    }

    /**
     * Constructor with all fields
     */
    public ReservationCreatedEvent(Long reservationId, Long hotelId, Long roomId, String guestName,
                                  String guestEmail, String guestPhone, LocalDate checkInDate,
                                  LocalDate checkOutDate, Integer numberOfGuests, BigDecimal totalPrice,
                                  String status, String specialRequests, LocalDateTime createdAt,
                                  String eventType, LocalDateTime eventTimestamp) {
        this.reservationId = reservationId;
        this.hotelId = hotelId;
        this.roomId = roomId;
        this.guestName = guestName;
        this.guestEmail = guestEmail;
        this.guestPhone = guestPhone;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfGuests = numberOfGuests;
        this.totalPrice = totalPrice;
        this.status = status;
        this.specialRequests = specialRequests;
        this.createdAt = createdAt;
        this.eventType = eventType;
        this.eventTimestamp = eventTimestamp;
    }

    // Getters and Setters
    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }

    public String getGuestPhone() {
        return guestPhone;
    }

    public void setGuestPhone(String guestPhone) {
        this.guestPhone = guestPhone;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(LocalDateTime eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    @Override
    public String toString() {
        return "ReservationCreatedEvent{" +
                "reservationId=" + reservationId +
                ", hotelId=" + hotelId +
                ", roomId=" + roomId +
                ", guestName='" + guestName + '\'' +
                ", guestEmail='" + guestEmail + '\'' +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", numberOfGuests=" + numberOfGuests +
                ", totalPrice=" + totalPrice +
                ", status='" + status + '\'' +
                ", eventType='" + eventType + '\'' +
                ", eventTimestamp=" + eventTimestamp +
                '}';
    }
} 