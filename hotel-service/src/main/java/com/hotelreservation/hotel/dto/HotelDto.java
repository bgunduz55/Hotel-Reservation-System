package com.hotelreservation.hotel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Hotel Data Transfer Object
 *
 * Used for transferring hotel data between layers.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
public class HotelDto {

    private Long id;

    @NotBlank(message = "Hotel name is required")
    @Size(min = 2, max = 100, message = "Hotel name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Hotel address is required")
    @Size(max = 500, message = "Hotel address must not exceed 500 characters")
    private String address;

    @NotBlank(message = "Hotel city is required")
    @Size(min = 2, max = 50, message = "Hotel city must be between 2 and 50 characters")
    private String city;

    @Size(max = 20, message = "Hotel phone must not exceed 20 characters")
    private String phone;

    @Size(max = 100, message = "Hotel email must not exceed 100 characters")
    private String email;

    @Size(max = 1000, message = "Hotel description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Hotel rating is required")
    private Integer rating;

    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;

    // Default constructor
    public HotelDto() {
    }

    // Constructor with required fields
    public HotelDto(String name, String address, String city, Integer rating) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.rating = rating;
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

    @Override
    public String toString() {
        return "HotelDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", rating=" + rating +
                ", active=" + active +
                '}';
    }
} 