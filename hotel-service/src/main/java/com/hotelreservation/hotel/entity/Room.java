package com.hotelreservation.hotel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Room Entity
 *
 * Represents a room in a hotel with its specifications and pricing.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@Entity
@Table(name = "rooms", indexes = {
    @Index(name = "idx_room_hotel_id", columnList = "hotel_id"),
    @Index(name = "idx_room_number", columnList = "room_number"),
    @Index(name = "idx_room_type", columnList = "room_type"),
    @Index(name = "idx_room_active", columnList = "active")
})
@Where(clause = "active = true")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Hotel is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @NotBlank(message = "Room number is required")
    @Size(min = 1, max = 10, message = "Room number must be between 1 and 10 characters")
    @Column(name = "room_number", nullable = false, length = 10)
    private String roomNumber;

    @NotBlank(message = "Room type is required")
    @Size(min = 2, max = 50, message = "Room type must be between 2 and 50 characters")
    @Column(name = "room_type", nullable = false, length = 50)
    private String roomType;

    @Size(max = 500, message = "Room description must not exceed 500 characters")
    @Column(name = "description", length = 500)
    private String description;

    @NotNull(message = "Room capacity is required")
    @Positive(message = "Room capacity must be positive")
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @NotNull(message = "Room price is required")
    @Positive(message = "Room price must be positive")
    @Column(name = "price_per_night", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    @NotNull(message = "Room availability status is required")
    @Column(name = "available", nullable = false)
    private Boolean available = true;

    @NotNull(message = "Room active status is required")
    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version")
    private Long version;

    // Default constructor
    public Room() {
    }

    // Constructor with required fields
    public Room(String roomNumber, String roomType, Integer capacity, BigDecimal pricePerNight) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.capacity = capacity;
        this.pricePerNight = pricePerNight;
        this.available = true;
        this.active = true;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
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

    // Business methods
    public void softDelete() {
        this.active = false;
    }

    public void markAsUnavailable() {
        this.available = false;
    }

    public void markAsAvailable() {
        this.available = true;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", roomNumber='" + roomNumber + '\'' +
                ", roomType='" + roomType + '\'' +
                ", capacity=" + capacity +
                ", pricePerNight=" + pricePerNight +
                ", available=" + available +
                ", active=" + active +
                '}';
    }
} 