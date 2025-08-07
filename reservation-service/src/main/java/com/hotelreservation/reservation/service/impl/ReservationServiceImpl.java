package com.hotelreservation.reservation.service.impl;

import com.hotelreservation.reservation.dto.ReservationDto;
import com.hotelreservation.reservation.entity.Reservation;
import com.hotelreservation.reservation.entity.ReservationStatus;
import com.hotelreservation.reservation.exception.InvalidReservationDataException;
import com.hotelreservation.reservation.exception.ReservationConflictException;
import com.hotelreservation.reservation.exception.ReservationNotFoundException;
import com.hotelreservation.reservation.repository.ReservationRepository;
import com.hotelreservation.reservation.service.EventPublishingService;
import com.hotelreservation.reservation.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Reservation Service Implementation
 *
 * Business logic implementation for reservation management.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationServiceImpl.class);

    private final ReservationRepository reservationRepository;
    private final EventPublishingService eventPublishingService;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository, EventPublishingService eventPublishingService) {
        this.reservationRepository = reservationRepository;
        this.eventPublishingService = eventPublishingService;
    }

    @Override
    @Transactional
    public ReservationDto createReservation(ReservationDto reservationDto) {
        logger.info("Creating reservation for guest: {}", reservationDto.getGuestEmail());

        // Validate reservation data
        validateReservationData(reservationDto);

        // Check for conflicts
        if (hasConflictingReservation(reservationDto.getRoomId(), reservationDto.getCheckInDate(), reservationDto.getCheckOutDate())) {
            throw new ReservationConflictException(reservationDto.getRoomId(), reservationDto.getCheckInDate(), reservationDto.getCheckOutDate());
        }

        // Convert DTO to entity
        Reservation reservation = convertToEntity(reservationDto);
        reservation.setStatus(ReservationStatus.PENDING);

        // Save reservation
        Reservation savedReservation = reservationRepository.save(reservation);
        logger.info("Reservation created successfully with ID: {}", savedReservation.getId());

        // Convert to DTO
        ReservationDto savedReservationDto = convertToDto(savedReservation);

        // Publish event
        try {
            eventPublishingService.publishReservationCreatedEvent(savedReservationDto);
            logger.info("Reservation created event published successfully for reservation ID: {}", savedReservation.getId());
        } catch (Exception e) {
            logger.error("Failed to publish reservation created event for reservation ID: {}. Error: {}", 
                    savedReservation.getId(), e.getMessage(), e);
            // Don't throw exception to avoid rolling back the transaction
            // The reservation is still created successfully
        }

        return savedReservationDto;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReservationDto> getReservationById(Long id) {
        logger.debug("Finding reservation by ID: {}", id);
        return reservationRepository.findByIdAndActiveTrue(id)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getAllReservations() {
        logger.debug("Finding all active reservations");
        return reservationRepository.findByActiveTrue()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReservationDto> getAllReservations(Pageable pageable) {
        logger.debug("Finding all active reservations with pagination");
        return reservationRepository.findByActiveTrue(pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional
    public ReservationDto updateReservation(Long id, ReservationDto reservationDto) {
        logger.info("Updating reservation with ID: {}", id);

        // Validate reservation data
        validateReservationData(reservationDto);

        // Find existing reservation
        Reservation existingReservation = reservationRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));

        // Check for conflicts (excluding current reservation)
        if (hasConflictingReservationExcluding(reservationDto.getRoomId(), id, reservationDto.getCheckInDate(), reservationDto.getCheckOutDate())) {
            throw new ReservationConflictException(reservationDto.getRoomId(), reservationDto.getCheckInDate(), reservationDto.getCheckOutDate());
        }

        // Update reservation fields
        updateReservationFields(existingReservation, reservationDto);

        // Save updated reservation
        Reservation updatedReservation = reservationRepository.save(existingReservation);
        logger.info("Reservation updated successfully with ID: {}", updatedReservation.getId());

        return convertToDto(updatedReservation);
    }

    @Override
    @Transactional
    public void deleteReservation(Long id) {
        logger.info("Deleting reservation with ID: {}", id);

        Reservation reservation = reservationRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));

        reservation.softDelete();
        reservationRepository.save(reservation);
        logger.info("Reservation deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getReservationsByHotelId(Long hotelId) {
        logger.debug("Finding reservations by hotel ID: {}", hotelId);
        return reservationRepository.findByHotelIdAndActiveTrue(hotelId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReservationDto> getReservationsByHotelId(Long hotelId, Pageable pageable) {
        logger.debug("Finding reservations by hotel ID: {} with pagination", hotelId);
        return reservationRepository.findByHotelIdAndActiveTrue(hotelId, pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public long countReservationsByHotelId(Long hotelId) {
        logger.debug("Counting reservations by hotel ID: {}", hotelId);
        return reservationRepository.countByHotelIdAndActiveTrue(hotelId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getReservationsByRoomId(Long roomId) {
        logger.debug("Finding reservations by room ID: {}", roomId);
        return reservationRepository.findByRoomIdAndActiveTrue(roomId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReservationDto> getReservationsByRoomId(Long roomId, Pageable pageable) {
        logger.debug("Finding reservations by room ID: {} with pagination", roomId);
        return reservationRepository.findByRoomIdAndActiveTrue(roomId, pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public long countReservationsByRoomId(Long roomId) {
        logger.debug("Counting reservations by room ID: {}", roomId);
        return reservationRepository.countByRoomIdAndActiveTrue(roomId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getReservationsByGuestEmail(String guestEmail) {
        logger.debug("Finding reservations by guest email: {}", guestEmail);
        return reservationRepository.findByGuestEmailAndActiveTrue(guestEmail)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReservationDto> getReservationsByGuestEmail(String guestEmail, Pageable pageable) {
        logger.debug("Finding reservations by guest email: {} with pagination", guestEmail);
        return reservationRepository.findByGuestEmailAndActiveTrue(guestEmail, pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getReservationsByGuestName(String guestName) {
        logger.debug("Finding reservations by guest name: {}", guestName);
        return reservationRepository.findByGuestNameContainingIgnoreCaseAndActiveTrue(guestName)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReservationDto> getReservationsByGuestName(String guestName, Pageable pageable) {
        logger.debug("Finding reservations by guest name: {} with pagination", guestName);
        return reservationRepository.findByGuestNameContainingIgnoreCaseAndActiveTrue(guestName, pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public long countReservationsByGuestEmail(String guestEmail) {
        logger.debug("Counting reservations by guest email: {}", guestEmail);
        return reservationRepository.countByGuestEmailAndActiveTrue(guestEmail);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getReservationsByStatus(ReservationStatus status) {
        logger.debug("Finding reservations by status: {}", status);
        return reservationRepository.findByStatusAndActiveTrue(status)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReservationDto> getReservationsByStatus(ReservationStatus status, Pageable pageable) {
        logger.debug("Finding reservations by status: {} with pagination", status);
        return reservationRepository.findByStatusAndActiveTrue(status, pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public long countReservationsByStatus(ReservationStatus status) {
        logger.debug("Counting reservations by status: {}", status);
        return reservationRepository.countByStatusAndActiveTrue(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getReservationsByCheckInDateRange(LocalDate startDate, LocalDate endDate) {
        logger.debug("Finding reservations by check-in date range: {} to {}", startDate, endDate);
        return reservationRepository.findByCheckInDateBetweenAndActiveTrue(startDate, endDate)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getReservationsByCheckOutDateRange(LocalDate startDate, LocalDate endDate) {
        logger.debug("Finding reservations by check-out date range: {} to {}", startDate, endDate);
        return reservationRepository.findByCheckOutDateBetweenAndActiveTrue(startDate, endDate)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getReservationsByCheckInDateFrom(LocalDate date) {
        logger.debug("Finding reservations by check-in date from: {}", date);
        return reservationRepository.findByCheckInDateGreaterThanEqualAndActiveTrue(date)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getReservationsByCheckOutDateTo(LocalDate date) {
        logger.debug("Finding reservations by check-out date to: {}", date);
        return reservationRepository.findByCheckOutDateLessThanEqualAndActiveTrue(date)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getReservationsByHotelIdAndCheckInDateRange(Long hotelId, LocalDate startDate, LocalDate endDate) {
        logger.debug("Finding reservations by hotel ID: {} and check-in date range: {} to {}", hotelId, startDate, endDate);
        return reservationRepository.findByHotelIdAndCheckInDateBetweenAndActiveTrue(hotelId, startDate, endDate)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getReservationsByHotelIdAndCheckOutDateRange(Long hotelId, LocalDate startDate, LocalDate endDate) {
        logger.debug("Finding reservations by hotel ID: {} and check-out date range: {} to {}", hotelId, startDate, endDate);
        return reservationRepository.findByHotelIdAndCheckOutDateBetweenAndActiveTrue(hotelId, startDate, endDate)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getReservationsByRoomIdAndCheckInDateRange(Long roomId, LocalDate startDate, LocalDate endDate) {
        logger.debug("Finding reservations by room ID: {} and check-in date range: {} to {}", roomId, startDate, endDate);
        return reservationRepository.findByRoomIdAndCheckInDateBetweenAndActiveTrue(roomId, startDate, endDate)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getReservationsByRoomIdAndCheckOutDateRange(Long roomId, LocalDate startDate, LocalDate endDate) {
        logger.debug("Finding reservations by room ID: {} and check-out date range: {} to {}", roomId, startDate, endDate);
        return reservationRepository.findByRoomIdAndCheckOutDateBetweenAndActiveTrue(roomId, startDate, endDate)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getReservationsByStatusAndCheckInDateRange(ReservationStatus status, LocalDate startDate, LocalDate endDate) {
        logger.debug("Finding reservations by status: {} and check-in date range: {} to {}", status, startDate, endDate);
        return reservationRepository.findByStatusAndCheckInDateBetweenAndActiveTrue(status, startDate, endDate)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getReservationsByStatusAndCheckOutDateRange(ReservationStatus status, LocalDate startDate, LocalDate endDate) {
        logger.debug("Finding reservations by status: {} and check-out date range: {} to {}", status, startDate, endDate);
        return reservationRepository.findByStatusAndCheckOutDateBetweenAndActiveTrue(status, startDate, endDate)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getReservationsByHotelIdAndStatus(Long hotelId, ReservationStatus status) {
        logger.debug("Finding reservations by hotel ID: {} and status: {}", hotelId, status);
        return reservationRepository.findByHotelIdAndStatusAndActiveTrue(hotelId, status)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReservationDto> getReservationsByHotelIdAndStatus(Long hotelId, ReservationStatus status, Pageable pageable) {
        logger.debug("Finding reservations by hotel ID: {} and status: {} with pagination", hotelId, status);
        return reservationRepository.findByHotelIdAndStatusAndActiveTrue(hotelId, status, pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getReservationsByRoomIdAndStatus(Long roomId, ReservationStatus status) {
        logger.debug("Finding reservations by room ID: {} and status: {}", roomId, status);
        return reservationRepository.findByRoomIdAndStatusAndActiveTrue(roomId, status)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReservationDto> getReservationsByRoomIdAndStatus(Long roomId, ReservationStatus status, Pageable pageable) {
        logger.debug("Finding reservations by room ID: {} and status: {} with pagination", roomId, status);
        return reservationRepository.findByRoomIdAndStatusAndActiveTrue(roomId, status, pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getReservationsByGuestEmailAndStatus(String guestEmail, ReservationStatus status) {
        logger.debug("Finding reservations by guest email: {} and status: {}", guestEmail, status);
        return reservationRepository.findByGuestEmailAndStatusAndActiveTrue(guestEmail, status)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReservationDto> getReservationsByGuestEmailAndStatus(String guestEmail, ReservationStatus status, Pageable pageable) {
        logger.debug("Finding reservations by guest email: {} and status: {} with pagination", guestEmail, status);
        return reservationRepository.findByGuestEmailAndStatusAndActiveTrue(guestEmail, status, pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getReservationsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        logger.debug("Finding reservations by price range: {} to {}", minPrice, maxPrice);
        return reservationRepository.findByTotalPriceBetweenAndActiveTrue(minPrice, maxPrice)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReservationDto> getReservationsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        logger.debug("Finding reservations by price range: {} to {} with pagination", minPrice, maxPrice);
        return reservationRepository.findByTotalPriceBetweenAndActiveTrue(minPrice, maxPrice, pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getReservationsByNumberOfGuests(Integer numberOfGuests) {
        logger.debug("Finding reservations by number of guests: {}", numberOfGuests);
        return reservationRepository.findByNumberOfGuestsAndActiveTrue(numberOfGuests)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getReservationsByNumberOfGuestsRange(Integer minGuests, Integer maxGuests) {
        logger.debug("Finding reservations by number of guests range: {} to {}", minGuests, maxGuests);
        return reservationRepository.findByNumberOfGuestsBetweenAndActiveTrue(minGuests, maxGuests)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasConflictingReservation(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        logger.debug("Checking for conflicting reservations for room ID: {} between {} and {}", roomId, checkInDate, checkOutDate);
        return reservationRepository.existsConflictingReservation(roomId, checkInDate, checkOutDate);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasConflictingReservationExcluding(Long roomId, Long reservationId, LocalDate checkInDate, LocalDate checkOutDate) {
        logger.debug("Checking for conflicting reservations for room ID: {} between {} and {} (excluding reservation ID: {})", 
                roomId, checkInDate, checkOutDate, reservationId);
        return reservationRepository.existsConflictingReservationExcluding(roomId, reservationId, checkInDate, checkOutDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getConflictingReservations(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        logger.debug("Finding conflicting reservations for room ID: {} between {} and {}", roomId, checkInDate, checkOutDate);
        return reservationRepository.findConflictingReservations(roomId, checkInDate, checkOutDate)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getReservationsInDateRange(Long roomId, LocalDate startDate, LocalDate endDate) {
        logger.debug("Finding reservations in date range for room ID: {} between {} and {}", roomId, startDate, endDate);
        return reservationRepository.findReservationsInDateRange(roomId, startDate, endDate)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReservationDto confirmReservation(Long id) {
        logger.info("Confirming reservation with ID: {}", id);
        Reservation reservation = reservationRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));

        reservation.confirm();
        Reservation confirmedReservation = reservationRepository.save(reservation);
        logger.info("Reservation confirmed successfully with ID: {}", id);

        return convertToDto(confirmedReservation);
    }

    @Override
    @Transactional
    public ReservationDto cancelReservation(Long id) {
        logger.info("Cancelling reservation with ID: {}", id);
        Reservation reservation = reservationRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));

        reservation.cancel();
        Reservation cancelledReservation = reservationRepository.save(reservation);
        logger.info("Reservation cancelled successfully with ID: {}", id);

        return convertToDto(cancelledReservation);
    }

    @Override
    @Transactional
    public ReservationDto completeReservation(Long id) {
        logger.info("Completing reservation with ID: {}", id);
        Reservation reservation = reservationRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));

        reservation.complete();
        Reservation completedReservation = reservationRepository.save(reservation);
        logger.info("Reservation completed successfully with ID: {}", id);

        return convertToDto(completedReservation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getUpcomingReservationsByHotelId(Long hotelId) {
        logger.debug("Finding upcoming reservations for hotel ID: {}", hotelId);
        return reservationRepository.findUpcomingReservationsByHotelId(hotelId, LocalDate.now())
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getUpcomingReservationsByRoomId(Long roomId) {
        logger.debug("Finding upcoming reservations for room ID: {}", roomId);
        return reservationRepository.findUpcomingReservationsByRoomId(roomId, LocalDate.now())
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getTodayCheckInsByHotelId(Long hotelId) {
        logger.debug("Finding today's check-ins for hotel ID: {}", hotelId);
        return reservationRepository.findTodayCheckInsByHotelId(hotelId, LocalDate.now())
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getTodayCheckOutsByHotelId(Long hotelId) {
        logger.debug("Finding today's check-outs for hotel ID: {}", hotelId);
        return reservationRepository.findTodayCheckOutsByHotelId(hotelId, LocalDate.now())
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getOverdueReservationsByHotelId(Long hotelId) {
        logger.debug("Finding overdue reservations for hotel ID: {}", hotelId);
        return reservationRepository.findOverdueReservationsByHotelId(hotelId, LocalDate.now())
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long getReservationCountByHotelId(Long hotelId) {
        logger.debug("Getting reservation count for hotel ID: {}", hotelId);
        return reservationRepository.countByHotelId(hotelId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getReservationCountByRoomId(Long roomId) {
        logger.debug("Getting reservation count for room ID: {}", roomId);
        return reservationRepository.countByRoomId(roomId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getReservationCountByHotelIdAndStatus(Long hotelId, ReservationStatus status) {
        logger.debug("Getting reservation count for hotel ID: {} and status: {}", hotelId, status);
        return reservationRepository.countByHotelIdAndStatus(hotelId, status);
    }

    @Override
    @Transactional(readOnly = true)
    public long getReservationCountByRoomIdAndStatus(Long roomId, ReservationStatus status) {
        logger.debug("Getting reservation count for room ID: {} and status: {}", roomId, status);
        return reservationRepository.countByRoomIdAndStatus(roomId, status);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getAveragePriceByHotelId(Long hotelId) {
        logger.debug("Getting average price for hotel ID: {}", hotelId);
        return reservationRepository.getAveragePriceByHotelId(hotelId);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalRevenueByHotelId(Long hotelId) {
        logger.debug("Getting total revenue for hotel ID: {}", hotelId);
        return reservationRepository.getTotalRevenueByHotelId(hotelId);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalRevenueByHotelIdAndStatus(Long hotelId, ReservationStatus status) {
        logger.debug("Getting total revenue for hotel ID: {} and status: {}", hotelId, status);
        return reservationRepository.getTotalRevenueByHotelIdAndStatus(hotelId, status);
    }

    @Override
    @Transactional(readOnly = true)
    public long getReservationCountByHotelIdAndDateRange(Long hotelId, LocalDate startDate, LocalDate endDate) {
        logger.debug("Getting reservation count for hotel ID: {} and date range: {} to {}", hotelId, startDate, endDate);
        return reservationRepository.countByHotelIdAndDateRange(hotelId, startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalRevenueByHotelIdAndDateRange(Long hotelId, LocalDate startDate, LocalDate endDate) {
        logger.debug("Getting total revenue for hotel ID: {} and date range: {} to {}", hotelId, startDate, endDate);
        return reservationRepository.getTotalRevenueByHotelIdAndDateRange(hotelId, startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean reservationExists(Long id) {
        logger.debug("Checking if reservation exists with ID: {}", id);
        return reservationRepository.findByIdAndActiveTrue(id).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean reservationExistsByHotelIdAndRoomId(Long hotelId, Long roomId) {
        logger.debug("Checking if reservation exists for hotel ID: {} and room ID: {}", hotelId, roomId);
        return !reservationRepository.findByHotelIdAndRoomIdAndActiveTrue(hotelId, roomId).isEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean reservationExistsByGuestEmailAndDateRange(String guestEmail, LocalDate checkInDate, LocalDate checkOutDate) {
        logger.debug("Checking if reservation exists for guest email: {} and date range: {} to {}", guestEmail, checkInDate, checkOutDate);
        return !reservationRepository.findByGuestEmailAndCheckInDateBetweenAndActiveTrue(guestEmail, checkInDate, checkOutDate).isEmpty();
    }

    @Override
    public boolean isValidDateRange(LocalDate checkInDate, LocalDate checkOutDate) {
        return checkInDate != null && checkOutDate != null && checkInDate.isBefore(checkOutDate);
    }

    @Override
    public boolean isValidNumberOfGuests(Integer numberOfGuests) {
        return numberOfGuests != null && numberOfGuests >= 1 && numberOfGuests <= 10;
    }

    @Override
    public boolean isValidPrice(BigDecimal price) {
        return price != null && price.compareTo(BigDecimal.ZERO) > 0;
    }

    // Private helper methods

    private void validateReservationData(ReservationDto reservationDto) {
        if (!isValidDateRange(reservationDto.getCheckInDate(), reservationDto.getCheckOutDate())) {
            throw new InvalidReservationDataException(reservationDto.getCheckInDate(), reservationDto.getCheckOutDate());
        }

        if (!isValidNumberOfGuests(reservationDto.getNumberOfGuests())) {
            throw new InvalidReservationDataException(reservationDto.getNumberOfGuests());
        }

        if (!isValidPrice(reservationDto.getTotalPrice())) {
            throw new InvalidReservationDataException(reservationDto.getTotalPrice());
        }
    }

    private void updateReservationFields(Reservation reservation, ReservationDto reservationDto) {
        reservation.setHotelId(reservationDto.getHotelId());
        reservation.setRoomId(reservationDto.getRoomId());
        reservation.setGuestName(reservationDto.getGuestName());
        reservation.setGuestEmail(reservationDto.getGuestEmail());
        reservation.setGuestPhone(reservationDto.getGuestPhone());
        reservation.setCheckInDate(reservationDto.getCheckInDate());
        reservation.setCheckOutDate(reservationDto.getCheckOutDate());
        reservation.setNumberOfGuests(reservationDto.getNumberOfGuests());
        reservation.setTotalPrice(reservationDto.getTotalPrice());
        reservation.setSpecialRequests(reservationDto.getSpecialRequests());
    }

    private Reservation convertToEntity(ReservationDto dto) {
        Reservation entity = new Reservation();
        entity.setId(dto.getId());
        entity.setHotelId(dto.getHotelId());
        entity.setRoomId(dto.getRoomId());
        entity.setGuestName(dto.getGuestName());
        entity.setGuestEmail(dto.getGuestEmail());
        entity.setGuestPhone(dto.getGuestPhone());
        entity.setCheckInDate(dto.getCheckInDate());
        entity.setCheckOutDate(dto.getCheckOutDate());
        entity.setNumberOfGuests(dto.getNumberOfGuests());
        entity.setTotalPrice(dto.getTotalPrice());
        entity.setStatus(dto.getStatus());
        entity.setSpecialRequests(dto.getSpecialRequests());
        entity.setActive(dto.getActive());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        entity.setVersion(dto.getVersion());
        return entity;
    }

    private ReservationDto convertToDto(Reservation entity) {
        ReservationDto dto = new ReservationDto();
        dto.setId(entity.getId());
        dto.setHotelId(entity.getHotelId());
        dto.setRoomId(entity.getRoomId());
        dto.setGuestName(entity.getGuestName());
        dto.setGuestEmail(entity.getGuestEmail());
        dto.setGuestPhone(entity.getGuestPhone());
        dto.setCheckInDate(entity.getCheckInDate());
        dto.setCheckOutDate(entity.getCheckOutDate());
        dto.setNumberOfGuests(entity.getNumberOfGuests());
        dto.setTotalPrice(entity.getTotalPrice());
        dto.setStatus(entity.getStatus());
        dto.setSpecialRequests(entity.getSpecialRequests());
        dto.setActive(entity.getActive());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setVersion(entity.getVersion());
        return dto;
    }
} 