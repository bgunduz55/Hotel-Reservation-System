package com.hotelreservation.hotel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Hotel Entity
 *
 * Represents a hotel in the system with its basic information and rooms.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@Entity
@Table(name = "hotels", indexes = {
    @Index(name = "idx_hotel_name", columnList = "name"),
    @Index(name = "idx_hotel_city", columnList = "city"),
    @Index(name = "idx_hotel_active", columnList = "active")
})
@Where(clause = "active = true")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Hotel name is required")
    @Size(min = 2, max = 100, message = "Hotel name must be between 2 and 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Hotel address is required")
    @Size(max = 500, message = "Hotel address must not exceed 500 characters")
    @Column(name = "address", nullable = false, length = 500)
    private String address;

    @NotBlank(message = "Hotel city is required")
    @Size(min = 2, max = 50, message = "Hotel city must be between 2 and 50 characters")
    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Size(max = 20, message = "Hotel phone must not exceed 20 characters")
    @Column(name = "phone", length = 20)
    private String phone;

    @Size(max = 100, message = "Hotel email must not exceed 100 characters")
    @Column(name = "email", length = 100)
    private String email;

    @Size(max = 1000, message = "Hotel description must not exceed 1000 characters")
    @Column(name = "description", length = 1000)
    private String description;

    @NotNull(message = "Hotel rating is required")
    @Column(name = "rating", nullable = false)
    private Integer rating;

    @NotNull(message = "Hotel active status is required")
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

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Room> rooms = new ArrayList<>();

    // Default constructor
    public Hotel() {
    }

    // Constructor with required fields
    public Hotel(String name, String address, String city, Integer rating) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.rating = rating;
        this.active = true;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
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

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    // Business methods
    public void addRoom(Room room) {
        rooms.add(room);
        room.setHotel(this);
    }

    public void removeRoom(Room room) {
        rooms.remove(room);
        room.setHotel(null);
    }

    public void softDelete() {
        this.active = false;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", rating=" + rating +
                ", active=" + active +
                '}';
    }
} 