package com.hotelreservation.reservation.dto;

import com.hotelreservation.reservation.entity.ReservationStatus;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Reservation DTO
 *
 * Data Transfer Object for Reservation entity.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
public class ReservationDto {

    private Long id;

    @NotNull(message = "Hotel ID is required")
    private Long hotelId;

    @NotNull(message = "Room ID is required")
    private Long roomId;

    @NotBlank(message = "Guest name is required")
    @Size(max = 100, message = "Guest name must be less than 100 characters")
    private String guestName;

    @NotBlank(message = "Guest email is required")
    @Email(message = "Guest email must be valid")
    @Size(max = 100, message = "Guest email must be less than 100 characters")
    private String guestEmail;

    @NotBlank(message = "Guest phone is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Guest phone must be valid")
    @Size(max = 20, message = "Guest phone must be less than 20 characters")
    private String guestPhone;

    @NotNull(message = "Check-in date is required")
    @FutureOrPresent(message = "Check-in date must be in the present or future")
    private LocalDate checkInDate;

    @NotNull(message = "Check-out date is required")
    @Future(message = "Check-out date must be in the future")
    private LocalDate checkOutDate;

    @NotNull(message = "Number of guests is required")
    @Min(value = 1, message = "Number of guests must be at least 1")
    @Max(value = 10, message = "Number of guests must be at most 10")
    private Integer numberOfGuests;

    @NotNull(message = "Total price is required")
    @DecimalMin(value = "0.01", message = "Total price must be greater than 0")
    private BigDecimal totalPrice;

    private ReservationStatus status;

    @Size(max = 500, message = "Special requests must be less than 500 characters")
    private String specialRequests;

    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;

    /**
     * Default constructor
     */
    public ReservationDto() {
    }

    /**
     * Constructor with required fields
     */
    public ReservationDto(Long hotelId, Long roomId, String guestName, String guestEmail, 
                         String guestPhone, LocalDate checkInDate, LocalDate checkOutDate, 
                         Integer numberOfGuests, BigDecimal totalPrice) {
        this.hotelId = hotelId;
        this.roomId = roomId;
        this.guestName = guestName;
        this.guestEmail = guestEmail;
        this.guestPhone = guestPhone;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfGuests = numberOfGuests;
        this.totalPrice = totalPrice;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ReservationDto{" +
                "id=" + id +
                ", hotelId=" + hotelId +
                ", roomId=" + roomId +
                ", guestName='" + guestName + '\'' +
                ", guestEmail='" + guestEmail + '\'' +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", numberOfGuests=" + numberOfGuests +
                ", totalPrice=" + totalPrice +
                ", status=" + status +
                ", active=" + active +
                '}';
    }
} 