package com.hotelreservation.reservation.service;

import com.hotelreservation.reservation.dto.ReservationDto;
import com.hotelreservation.reservation.entity.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Reservation Service Interface
 *
 * Business logic interface for reservation management.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
public interface ReservationService {

    // Basic CRUD operations
    ReservationDto createReservation(ReservationDto reservationDto);
    Optional<ReservationDto> getReservationById(Long id);
    List<ReservationDto> getAllReservations();
    Page<ReservationDto> getAllReservations(Pageable pageable);
    ReservationDto updateReservation(Long id, ReservationDto reservationDto);
    void deleteReservation(Long id);

    // Find by hotel
    List<ReservationDto> getReservationsByHotelId(Long hotelId);
    Page<ReservationDto> getReservationsByHotelId(Long hotelId, Pageable pageable);
    long countReservationsByHotelId(Long hotelId);

    // Find by room
    List<ReservationDto> getReservationsByRoomId(Long roomId);
    Page<ReservationDto> getReservationsByRoomId(Long roomId, Pageable pageable);
    long countReservationsByRoomId(Long roomId);

    // Find by guest
    List<ReservationDto> getReservationsByGuestEmail(String guestEmail);
    Page<ReservationDto> getReservationsByGuestEmail(String guestEmail, Pageable pageable);
    List<ReservationDto> getReservationsByGuestName(String guestName);
    Page<ReservationDto> getReservationsByGuestName(String guestName, Pageable pageable);
    long countReservationsByGuestEmail(String guestEmail);

    // Find by status
    List<ReservationDto> getReservationsByStatus(ReservationStatus status);
    Page<ReservationDto> getReservationsByStatus(ReservationStatus status, Pageable pageable);
    long countReservationsByStatus(ReservationStatus status);

    // Find by date range
    List<ReservationDto> getReservationsByCheckInDateRange(LocalDate startDate, LocalDate endDate);
    List<ReservationDto> getReservationsByCheckOutDateRange(LocalDate startDate, LocalDate endDate);
    List<ReservationDto> getReservationsByCheckInDateFrom(LocalDate date);
    List<ReservationDto> getReservationsByCheckOutDateTo(LocalDate date);

    // Find by hotel and date range
    List<ReservationDto> getReservationsByHotelIdAndCheckInDateRange(Long hotelId, LocalDate startDate, LocalDate endDate);
    List<ReservationDto> getReservationsByHotelIdAndCheckOutDateRange(Long hotelId, LocalDate startDate, LocalDate endDate);

    // Find by room and date range
    List<ReservationDto> getReservationsByRoomIdAndCheckInDateRange(Long roomId, LocalDate startDate, LocalDate endDate);
    List<ReservationDto> getReservationsByRoomIdAndCheckOutDateRange(Long roomId, LocalDate startDate, LocalDate endDate);

    // Find by status and date range
    List<ReservationDto> getReservationsByStatusAndCheckInDateRange(ReservationStatus status, LocalDate startDate, LocalDate endDate);
    List<ReservationDto> getReservationsByStatusAndCheckOutDateRange(ReservationStatus status, LocalDate startDate, LocalDate endDate);

    // Find by hotel and status
    List<ReservationDto> getReservationsByHotelIdAndStatus(Long hotelId, ReservationStatus status);
    Page<ReservationDto> getReservationsByHotelIdAndStatus(Long hotelId, ReservationStatus status, Pageable pageable);

    // Find by room and status
    List<ReservationDto> getReservationsByRoomIdAndStatus(Long roomId, ReservationStatus status);
    Page<ReservationDto> getReservationsByRoomIdAndStatus(Long roomId, ReservationStatus status, Pageable pageable);

    // Find by guest email and status
    List<ReservationDto> getReservationsByGuestEmailAndStatus(String guestEmail, ReservationStatus status);
    Page<ReservationDto> getReservationsByGuestEmailAndStatus(String guestEmail, ReservationStatus status, Pageable pageable);

    // Find by price range
    List<ReservationDto> getReservationsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    Page<ReservationDto> getReservationsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    // Find by number of guests
    List<ReservationDto> getReservationsByNumberOfGuests(Integer numberOfGuests);
    List<ReservationDto> getReservationsByNumberOfGuestsRange(Integer minGuests, Integer maxGuests);

    // Conflict detection
    boolean hasConflictingReservation(Long roomId, LocalDate checkInDate, LocalDate checkOutDate);
    boolean hasConflictingReservationExcluding(Long roomId, Long reservationId, LocalDate checkInDate, LocalDate checkOutDate);
    List<ReservationDto> getConflictingReservations(Long roomId, LocalDate checkInDate, LocalDate checkOutDate);
    List<ReservationDto> getReservationsInDateRange(Long roomId, LocalDate startDate, LocalDate endDate);

    // Status management
    ReservationDto confirmReservation(Long id);
    ReservationDto cancelReservation(Long id);
    ReservationDto completeReservation(Long id);

    // Operational queries
    List<ReservationDto> getUpcomingReservationsByHotelId(Long hotelId);
    List<ReservationDto> getUpcomingReservationsByRoomId(Long roomId);
    List<ReservationDto> getTodayCheckInsByHotelId(Long hotelId);
    List<ReservationDto> getTodayCheckOutsByHotelId(Long hotelId);
    List<ReservationDto> getOverdueReservationsByHotelId(Long hotelId);

    // Statistics
    long getReservationCountByHotelId(Long hotelId);
    long getReservationCountByRoomId(Long roomId);
    long getReservationCountByHotelIdAndStatus(Long hotelId, ReservationStatus status);
    long getReservationCountByRoomIdAndStatus(Long roomId, ReservationStatus status);
    BigDecimal getAveragePriceByHotelId(Long hotelId);
    BigDecimal getTotalRevenueByHotelId(Long hotelId);
    BigDecimal getTotalRevenueByHotelIdAndStatus(Long hotelId, ReservationStatus status);
    long getReservationCountByHotelIdAndDateRange(Long hotelId, LocalDate startDate, LocalDate endDate);
    BigDecimal getTotalRevenueByHotelIdAndDateRange(Long hotelId, LocalDate startDate, LocalDate endDate);

    // Existence checks
    boolean reservationExists(Long id);
    boolean reservationExistsByHotelIdAndRoomId(Long hotelId, Long roomId);
    boolean reservationExistsByGuestEmailAndDateRange(String guestEmail, LocalDate checkInDate, LocalDate checkOutDate);

    // Validation
    boolean isValidDateRange(LocalDate checkInDate, LocalDate checkOutDate);
    boolean isValidNumberOfGuests(Integer numberOfGuests);
    boolean isValidPrice(BigDecimal price);
} 