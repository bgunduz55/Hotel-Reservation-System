package com.hotelreservation.reservation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Reservation Entity
 *
 * JPA entity for reservation management.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@Entity
@Table(name = "reservations", indexes = {
    @Index(name = "idx_reservation_hotel_id", columnList = "hotel_id"),
    @Index(name = "idx_reservation_room_id", columnList = "room_id"),
    @Index(name = "idx_reservation_guest_email", columnList = "guest_email"),
    @Index(name = "idx_reservation_check_in_date", columnList = "check_in_date"),
    @Index(name = "idx_reservation_check_out_date", columnList = "check_out_date"),
    @Index(name = "idx_reservation_status", columnList = "status"),
    @Index(name = "idx_reservation_active", columnList = "active")
})
@Where(clause = "active = true")
@EntityListeners(AuditingEntityListener.class)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "hotel_id", nullable = false)
    private Long hotelId;

    @NotNull
    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @NotBlank
    @Size(max = 100)
    @Column(name = "guest_name", nullable = false, length = 100)
    private String guestName;

    @NotBlank
    @Email
    @Size(max = 100)
    @Column(name = "guest_email", nullable = false, length = 100)
    private String guestEmail;

    @NotBlank
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Size(max = 20)
    @Column(name = "guest_phone", nullable = false, length = 20)
    private String guestPhone;

    @NotNull
    @FutureOrPresent
    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @NotNull
    @Future
    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;

    @NotNull
    @Min(1)
    @Max(10)
    @Column(name = "number_of_guests", nullable = false)
    private Integer numberOfGuests;

    @NotNull
    @DecimalMin("0.01")
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ReservationStatus status = ReservationStatus.PENDING;

    @Size(max = 500)
    @Column(name = "special_requests", length = 500)
    private String specialRequests;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version")
    private Long version;

    /**
     * Default constructor
     */
    public Reservation() {
    }

    /**
     * Constructor with required fields
     */
    public Reservation(Long hotelId, Long roomId, String guestName, String guestEmail, 
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

    /**
     * Soft delete the reservation
     */
    public void softDelete() {
        this.active = false;
    }

    /**
     * Confirm the reservation
     */
    public void confirm() {
        this.status = ReservationStatus.CONFIRMED;
    }

    /**
     * Cancel the reservation
     */
    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
    }

    /**
     * Complete the reservation
     */
    public void complete() {
        this.status = ReservationStatus.COMPLETED;
    }

    /**
     * Check if reservation is active
     */
    public boolean isActive() {
        return Boolean.TRUE.equals(this.active);
    }

    /**
     * Check if reservation is confirmed
     */
    public boolean isConfirmed() {
        return ReservationStatus.CONFIRMED.equals(this.status);
    }

    /**
     * Check if reservation is cancelled
     */
    public boolean isCancelled() {
        return ReservationStatus.CANCELLED.equals(this.status);
    }

    /**
     * Check if reservation is completed
     */
    public boolean isCompleted() {
        return ReservationStatus.COMPLETED.equals(this.status);
    }

    /**
     * Check if reservation is pending
     */
    public boolean isPending() {
        return ReservationStatus.PENDING.equals(this.status);
    }

    @Override
    public String toString() {
        return "Reservation{" +
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