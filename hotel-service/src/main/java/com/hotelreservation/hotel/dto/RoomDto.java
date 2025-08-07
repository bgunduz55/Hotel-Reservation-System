package com.hotelreservation.hotel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Room Data Transfer Object
 *
 * Used for transferring room data between layers.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
public class RoomDto {

    private Long id;

    @NotNull(message = "Hotel ID is required")
    private Long hotelId;

    @NotBlank(message = "Room number is required")
    @Size(min = 1, max = 10, message = "Room number must be between 1 and 10 characters")
    private String roomNumber;

    @NotBlank(message = "Room type is required")
    @Size(min = 2, max = 50, message = "Room type must be between 2 and 50 characters")
    private String roomType;

    @Size(max = 500, message = "Room description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Room capacity is required")
    @Positive(message = "Room capacity must be positive")
    private Integer capacity;

    @NotNull(message = "Room price is required")
    @Positive(message = "Room price must be positive")
    private BigDecimal pricePerNight;

    private Boolean available;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;

    // Default constructor
    public RoomDto() {
    }

    // Constructor with required fields
    public RoomDto(Long hotelId, String roomNumber, String roomType, Integer capacity, BigDecimal pricePerNight) {
        this.hotelId = hotelId;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.capacity = capacity;
        this.pricePerNight = pricePerNight;
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

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
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
        return "RoomDto{" +
                "id=" + id +
                ", hotelId=" + hotelId +
                ", roomNumber='" + roomNumber + '\'' +
                ", roomType='" + roomType + '\'' +
                ", capacity=" + capacity +
                ", pricePerNight=" + pricePerNight +
                ", available=" + available +
                ", active=" + active +
                '}';
    }
} 