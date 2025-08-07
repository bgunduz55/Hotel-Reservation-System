package com.hotelreservation.reservation.repository;

import com.hotelreservation.reservation.entity.Reservation;
import com.hotelreservation.reservation.entity.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Reservation Repository
 *
 * Spring Data JPA repository for Reservation entity.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Basic CRUD operations with soft delete
    Optional<Reservation> findByIdAndActiveTrue(Long id);
    List<Reservation> findByActiveTrue();
    Page<Reservation> findByActiveTrue(Pageable pageable);
    long countByActiveTrue();

    // Find by hotel
    List<Reservation> findByHotelIdAndActiveTrue(Long hotelId);
    Page<Reservation> findByHotelIdAndActiveTrue(Long hotelId, Pageable pageable);
    long countByHotelIdAndActiveTrue(Long hotelId);

    // Find by room
    List<Reservation> findByRoomIdAndActiveTrue(Long roomId);
    Page<Reservation> findByRoomIdAndActiveTrue(Long roomId, Pageable pageable);
    long countByRoomIdAndActiveTrue(Long roomId);

    // Find by guest email
    List<Reservation> findByGuestEmailAndActiveTrue(String guestEmail);
    Page<Reservation> findByGuestEmailAndActiveTrue(String guestEmail, Pageable pageable);
    long countByGuestEmailAndActiveTrue(String guestEmail);

    // Find by guest name (case-insensitive)
    List<Reservation> findByGuestNameContainingIgnoreCaseAndActiveTrue(String guestName);
    Page<Reservation> findByGuestNameContainingIgnoreCaseAndActiveTrue(String guestName, Pageable pageable);

    // Find by status
    List<Reservation> findByStatusAndActiveTrue(ReservationStatus status);
    Page<Reservation> findByStatusAndActiveTrue(ReservationStatus status, Pageable pageable);
    long countByStatusAndActiveTrue(ReservationStatus status);

    // Find by date range
    List<Reservation> findByCheckInDateBetweenAndActiveTrue(LocalDate startDate, LocalDate endDate);
    List<Reservation> findByCheckOutDateBetweenAndActiveTrue(LocalDate startDate, LocalDate endDate);
    List<Reservation> findByCheckInDateGreaterThanEqualAndActiveTrue(LocalDate date);
    List<Reservation> findByCheckOutDateLessThanEqualAndActiveTrue(LocalDate date);

    // Find by hotel and date range
    List<Reservation> findByHotelIdAndCheckInDateBetweenAndActiveTrue(Long hotelId, LocalDate startDate, LocalDate endDate);
    List<Reservation> findByHotelIdAndCheckOutDateBetweenAndActiveTrue(Long hotelId, LocalDate startDate, LocalDate endDate);

    // Find by room and date range
    List<Reservation> findByRoomIdAndCheckInDateBetweenAndActiveTrue(Long roomId, LocalDate startDate, LocalDate endDate);
    List<Reservation> findByRoomIdAndCheckOutDateBetweenAndActiveTrue(Long roomId, LocalDate startDate, LocalDate endDate);

    // Find by hotel and room
    List<Reservation> findByHotelIdAndRoomIdAndActiveTrue(Long hotelId, Long roomId);

    // Find by guest email and date range
    List<Reservation> findByGuestEmailAndCheckInDateBetweenAndActiveTrue(String guestEmail, LocalDate startDate, LocalDate endDate);

    // Find by status and date range
    List<Reservation> findByStatusAndCheckInDateBetweenAndActiveTrue(ReservationStatus status, LocalDate startDate, LocalDate endDate);
    List<Reservation> findByStatusAndCheckOutDateBetweenAndActiveTrue(ReservationStatus status, LocalDate startDate, LocalDate endDate);

    // Conflict detection queries
    @Query("SELECT COUNT(r) > 0 FROM Reservation r WHERE r.roomId = :roomId AND r.active = true " +
           "AND r.status IN ('PENDING', 'CONFIRMED') " +
           "AND ((r.checkInDate <= :checkOutDate AND r.checkOutDate >= :checkInDate))")
    boolean existsConflictingReservation(@Param("roomId") Long roomId, 
                                       @Param("checkInDate") LocalDate checkInDate, 
                                       @Param("checkOutDate") LocalDate checkOutDate);

    @Query("SELECT COUNT(r) > 0 FROM Reservation r WHERE r.roomId = :roomId AND r.id != :reservationId AND r.active = true " +
           "AND r.status IN ('PENDING', 'CONFIRMED') " +
           "AND ((r.checkInDate <= :checkOutDate AND r.checkOutDate >= :checkInDate))")
    boolean existsConflictingReservationExcluding(@Param("roomId") Long roomId, 
                                                @Param("reservationId") Long reservationId,
                                                @Param("checkInDate") LocalDate checkInDate, 
                                                @Param("checkOutDate") LocalDate checkOutDate);

    // Find conflicting reservations
    @Query("SELECT r FROM Reservation r WHERE r.roomId = :roomId AND r.active = true " +
           "AND r.status IN ('PENDING', 'CONFIRMED') " +
           "AND ((r.checkInDate <= :checkOutDate AND r.checkOutDate >= :checkInDate))")
    List<Reservation> findConflictingReservations(@Param("roomId") Long roomId, 
                                                 @Param("checkInDate") LocalDate checkInDate, 
                                                 @Param("checkOutDate") LocalDate checkOutDate);

    // Find reservations for a specific date range
    @Query("SELECT r FROM Reservation r WHERE r.roomId = :roomId AND r.active = true " +
           "AND r.status IN ('PENDING', 'CONFIRMED') " +
           "AND r.checkInDate <= :endDate AND r.checkOutDate >= :startDate")
    List<Reservation> findReservationsInDateRange(@Param("roomId") Long roomId, 
                                                 @Param("startDate") LocalDate startDate, 
                                                 @Param("endDate") LocalDate endDate);

    // Find reservations by hotel and status
    List<Reservation> findByHotelIdAndStatusAndActiveTrue(Long hotelId, ReservationStatus status);
    Page<Reservation> findByHotelIdAndStatusAndActiveTrue(Long hotelId, ReservationStatus status, Pageable pageable);

    // Find reservations by room and status
    List<Reservation> findByRoomIdAndStatusAndActiveTrue(Long roomId, ReservationStatus status);
    Page<Reservation> findByRoomIdAndStatusAndActiveTrue(Long roomId, ReservationStatus status, Pageable pageable);

    // Find reservations by guest email and status
    List<Reservation> findByGuestEmailAndStatusAndActiveTrue(String guestEmail, ReservationStatus status);
    Page<Reservation> findByGuestEmailAndStatusAndActiveTrue(String guestEmail, ReservationStatus status, Pageable pageable);

    // Find reservations by price range
    List<Reservation> findByTotalPriceBetweenAndActiveTrue(java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice);
    Page<Reservation> findByTotalPriceBetweenAndActiveTrue(java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice, Pageable pageable);

    // Find reservations by number of guests
    List<Reservation> findByNumberOfGuestsAndActiveTrue(Integer numberOfGuests);
    List<Reservation> findByNumberOfGuestsBetweenAndActiveTrue(Integer minGuests, Integer maxGuests);

    // Custom queries for statistics
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.hotelId = :hotelId AND r.active = true")
    long countByHotelId(@Param("hotelId") Long hotelId);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.roomId = :roomId AND r.active = true")
    long countByRoomId(@Param("roomId") Long roomId);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.hotelId = :hotelId AND r.status = :status AND r.active = true")
    long countByHotelIdAndStatus(@Param("hotelId") Long hotelId, @Param("status") ReservationStatus status);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.roomId = :roomId AND r.status = :status AND r.active = true")
    long countByRoomIdAndStatus(@Param("roomId") Long roomId, @Param("status") ReservationStatus status);

    // Find reservations with total price statistics
    @Query("SELECT AVG(r.totalPrice) FROM Reservation r WHERE r.hotelId = :hotelId AND r.active = true")
    java.math.BigDecimal getAveragePriceByHotelId(@Param("hotelId") Long hotelId);

    @Query("SELECT SUM(r.totalPrice) FROM Reservation r WHERE r.hotelId = :hotelId AND r.active = true")
    java.math.BigDecimal getTotalRevenueByHotelId(@Param("hotelId") Long hotelId);

    @Query("SELECT SUM(r.totalPrice) FROM Reservation r WHERE r.hotelId = :hotelId AND r.status = :status AND r.active = true")
    java.math.BigDecimal getTotalRevenueByHotelIdAndStatus(@Param("hotelId") Long hotelId, @Param("status") ReservationStatus status);

    // Find reservations by date range with statistics
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.hotelId = :hotelId AND r.active = true " +
           "AND r.checkInDate BETWEEN :startDate AND :endDate")
    long countByHotelIdAndDateRange(@Param("hotelId") Long hotelId, 
                                   @Param("startDate") LocalDate startDate, 
                                   @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(r.totalPrice) FROM Reservation r WHERE r.hotelId = :hotelId AND r.active = true " +
           "AND r.checkInDate BETWEEN :startDate AND :endDate")
    java.math.BigDecimal getTotalRevenueByHotelIdAndDateRange(@Param("hotelId") Long hotelId, 
                                                             @Param("startDate") LocalDate startDate, 
                                                             @Param("endDate") LocalDate endDate);

    // Find upcoming reservations
    @Query("SELECT r FROM Reservation r WHERE r.hotelId = :hotelId AND r.active = true " +
           "AND r.status IN ('PENDING', 'CONFIRMED') AND r.checkInDate >= :today " +
           "ORDER BY r.checkInDate ASC")
    List<Reservation> findUpcomingReservationsByHotelId(@Param("hotelId") Long hotelId, @Param("today") LocalDate today);

    @Query("SELECT r FROM Reservation r WHERE r.roomId = :roomId AND r.active = true " +
           "AND r.status IN ('PENDING', 'CONFIRMED') AND r.checkInDate >= :today " +
           "ORDER BY r.checkInDate ASC")
    List<Reservation> findUpcomingReservationsByRoomId(@Param("roomId") Long roomId, @Param("today") LocalDate today);

    // Find today's check-ins
    @Query("SELECT r FROM Reservation r WHERE r.hotelId = :hotelId AND r.active = true " +
           "AND r.status IN ('PENDING', 'CONFIRMED') AND r.checkInDate = :today")
    List<Reservation> findTodayCheckInsByHotelId(@Param("hotelId") Long hotelId, @Param("today") LocalDate today);

    // Find today's check-outs
    @Query("SELECT r FROM Reservation r WHERE r.hotelId = :hotelId AND r.active = true " +
           "AND r.status IN ('PENDING', 'CONFIRMED') AND r.checkOutDate = :today")
    List<Reservation> findTodayCheckOutsByHotelId(@Param("hotelId") Long hotelId, @Param("today") LocalDate today);

    // Find overdue reservations (past check-out date but not completed)
    @Query("SELECT r FROM Reservation r WHERE r.hotelId = :hotelId AND r.active = true " +
           "AND r.status IN ('PENDING', 'CONFIRMED') AND r.checkOutDate < :today")
    List<Reservation> findOverdueReservationsByHotelId(@Param("hotelId") Long hotelId, @Param("today") LocalDate today);
} 