package com.hotelreservation.reservation.controller;

import com.hotelreservation.reservation.dto.ReservationDto;
import com.hotelreservation.reservation.entity.ReservationStatus;
import com.hotelreservation.reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Reservation Controller
 *
 * REST API controller for reservation management.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Reservation", description = "Reservation management API")
public class ReservationController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * Create a new reservation
     */
    @PostMapping
    @Operation(summary = "Create a new reservation", description = "Creates a new reservation with the provided data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reservation created successfully",
                    content = @Content(schema = @Schema(implementation = ReservationDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid reservation data"),
            @ApiResponse(responseCode = "409", description = "Reservation conflict detected")
    })
    public ResponseEntity<ReservationDto> createReservation(
            @Parameter(description = "Reservation data", required = true)
            @Valid @RequestBody ReservationDto reservationDto) {
        logger.info("Creating new reservation for guest: {}", reservationDto.getGuestEmail());
        ReservationDto createdReservation = reservationService.createReservation(reservationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }

    /**
     * Get reservation by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get reservation by ID", description = "Retrieves a reservation by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation found",
                    content = @Content(schema = @Schema(implementation = ReservationDto.class))),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    public ResponseEntity<ReservationDto> getReservationById(
            @Parameter(description = "Reservation ID", required = true)
            @PathVariable Long id) {
        logger.debug("Getting reservation by ID: {}", id);
        Optional<ReservationDto> reservation = reservationService.getReservationById(id);
        return reservation.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all reservations with pagination
     */
    @GetMapping
    @Operation(summary = "Get all reservations", description = "Retrieves all reservations with pagination support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservations retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<ReservationDto>> getAllReservations(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Sort direction")
            @RequestParam(defaultValue = "DESC") String direction) {
        logger.debug("Getting all reservations with pagination: page={}, size={}, sort={}, direction={}", 
                page, size, sort, direction);
        
        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        Page<ReservationDto> reservations = reservationService.getAllReservations(pageable);
        return ResponseEntity.ok(reservations);
    }

    /**
     * Update reservation
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update reservation", description = "Updates an existing reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation updated successfully",
                    content = @Content(schema = @Schema(implementation = ReservationDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid reservation data"),
            @ApiResponse(responseCode = "404", description = "Reservation not found"),
            @ApiResponse(responseCode = "409", description = "Reservation conflict detected")
    })
    public ResponseEntity<ReservationDto> updateReservation(
            @Parameter(description = "Reservation ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated reservation data", required = true)
            @Valid @RequestBody ReservationDto reservationDto) {
        logger.info("Updating reservation with ID: {}", id);
        ReservationDto updatedReservation = reservationService.updateReservation(id, reservationDto);
        return ResponseEntity.ok(updatedReservation);
    }

    /**
     * Delete reservation
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete reservation", description = "Soft deletes a reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reservation deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    public ResponseEntity<Void> deleteReservation(
            @Parameter(description = "Reservation ID", required = true)
            @PathVariable Long id) {
        logger.info("Deleting reservation with ID: {}", id);
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get reservations by hotel ID
     */
    @GetMapping("/hotel/{hotelId}")
    @Operation(summary = "Get reservations by hotel ID", description = "Retrieves all reservations for a specific hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservations retrieved successfully",
                    content = @Content(schema = @Schema(implementation = List.class)))
    })
    public ResponseEntity<List<ReservationDto>> getReservationsByHotelId(
            @Parameter(description = "Hotel ID", required = true)
            @PathVariable Long hotelId,
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size) {
        logger.debug("Getting reservations by hotel ID: {}", hotelId);
        Pageable pageable = PageRequest.of(page, size);
        Page<ReservationDto> reservations = reservationService.getReservationsByHotelId(hotelId, pageable);
        return ResponseEntity.ok(reservations.getContent());
    }

    /**
     * Get reservations by room ID
     */
    @GetMapping("/room/{roomId}")
    @Operation(summary = "Get reservations by room ID", description = "Retrieves all reservations for a specific room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservations retrieved successfully",
                    content = @Content(schema = @Schema(implementation = List.class)))
    })
    public ResponseEntity<List<ReservationDto>> getReservationsByRoomId(
            @Parameter(description = "Room ID", required = true)
            @PathVariable Long roomId,
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size) {
        logger.debug("Getting reservations by room ID: {}", roomId);
        Pageable pageable = PageRequest.of(page, size);
        Page<ReservationDto> reservations = reservationService.getReservationsByRoomId(roomId, pageable);
        return ResponseEntity.ok(reservations.getContent());
    }

    /**
     * Get reservations by guest email
     */
    @GetMapping("/guest/email")
    @Operation(summary = "Get reservations by guest email", description = "Retrieves all reservations for a specific guest email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservations retrieved successfully",
                    content = @Content(schema = @Schema(implementation = List.class)))
    })
    public ResponseEntity<List<ReservationDto>> getReservationsByGuestEmail(
            @Parameter(description = "Guest email", required = true)
            @RequestParam String email,
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size) {
        logger.debug("Getting reservations by guest email: {}", email);
        Pageable pageable = PageRequest.of(page, size);
        Page<ReservationDto> reservations = reservationService.getReservationsByGuestEmail(email, pageable);
        return ResponseEntity.ok(reservations.getContent());
    }

    /**
     * Get reservations by guest name
     */
    @GetMapping("/guest/name")
    @Operation(summary = "Get reservations by guest name", description = "Retrieves all reservations for a specific guest name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservations retrieved successfully",
                    content = @Content(schema = @Schema(implementation = List.class)))
    })
    public ResponseEntity<List<ReservationDto>> getReservationsByGuestName(
            @Parameter(description = "Guest name", required = true)
            @RequestParam String name,
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size) {
        logger.debug("Getting reservations by guest name: {}", name);
        Pageable pageable = PageRequest.of(page, size);
        Page<ReservationDto> reservations = reservationService.getReservationsByGuestName(name, pageable);
        return ResponseEntity.ok(reservations.getContent());
    }

    /**
     * Get reservations by status
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Get reservations by status", description = "Retrieves all reservations with a specific status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservations retrieved successfully",
                    content = @Content(schema = @Schema(implementation = List.class)))
    })
    public ResponseEntity<List<ReservationDto>> getReservationsByStatus(
            @Parameter(description = "Reservation status", required = true)
            @PathVariable ReservationStatus status,
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size) {
        logger.debug("Getting reservations by status: {}", status);
        Pageable pageable = PageRequest.of(page, size);
        Page<ReservationDto> reservations = reservationService.getReservationsByStatus(status, pageable);
        return ResponseEntity.ok(reservations.getContent());
    }

    /**
     * Get reservations by date range
     */
    @GetMapping("/date-range")
    @Operation(summary = "Get reservations by date range", description = "Retrieves reservations within a specific date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservations retrieved successfully",
                    content = @Content(schema = @Schema(implementation = List.class)))
    })
    public ResponseEntity<List<ReservationDto>> getReservationsByDateRange(
            @Parameter(description = "Start date (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "Date type (check-in or check-out)")
            @RequestParam(defaultValue = "check-in") String dateType) {
        logger.debug("Getting reservations by date range: {} to {}, type: {}", startDate, endDate, dateType);
        
        List<ReservationDto> reservations;
        if ("check-out".equalsIgnoreCase(dateType)) {
            reservations = reservationService.getReservationsByCheckOutDateRange(startDate, endDate);
        } else {
            reservations = reservationService.getReservationsByCheckInDateRange(startDate, endDate);
        }
        return ResponseEntity.ok(reservations);
    }

    /**
     * Get reservations by price range
     */
    @GetMapping("/price-range")
    @Operation(summary = "Get reservations by price range", description = "Retrieves reservations within a specific price range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservations retrieved successfully",
                    content = @Content(schema = @Schema(implementation = List.class)))
    })
    public ResponseEntity<List<ReservationDto>> getReservationsByPriceRange(
            @Parameter(description = "Minimum price", required = true)
            @RequestParam BigDecimal minPrice,
            @Parameter(description = "Maximum price", required = true)
            @RequestParam BigDecimal maxPrice,
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size) {
        logger.debug("Getting reservations by price range: {} to {}", minPrice, maxPrice);
        Pageable pageable = PageRequest.of(page, size);
        Page<ReservationDto> reservations = reservationService.getReservationsByPriceRange(minPrice, maxPrice, pageable);
        return ResponseEntity.ok(reservations.getContent());
    }

    /**
     * Check for conflicting reservations
     */
    @GetMapping("/conflicts")
    @Operation(summary = "Check for conflicting reservations", description = "Checks if there are conflicting reservations for a room and date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conflict check completed",
                    content = @Content(schema = @Schema(implementation = Boolean.class)))
    })
    public ResponseEntity<Boolean> checkForConflicts(
            @Parameter(description = "Room ID", required = true)
            @RequestParam Long roomId,
            @Parameter(description = "Check-in date (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @Parameter(description = "Check-out date (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate) {
        logger.debug("Checking for conflicts for room ID: {} between {} and {}", roomId, checkInDate, checkOutDate);
        boolean hasConflicts = reservationService.hasConflictingReservation(roomId, checkInDate, checkOutDate);
        return ResponseEntity.ok(hasConflicts);
    }

    /**
     * Get conflicting reservations
     */
    @GetMapping("/conflicts/details")
    @Operation(summary = "Get conflicting reservations", description = "Retrieves detailed information about conflicting reservations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conflicting reservations retrieved successfully",
                    content = @Content(schema = @Schema(implementation = List.class)))
    })
    public ResponseEntity<List<ReservationDto>> getConflictingReservations(
            @Parameter(description = "Room ID", required = true)
            @RequestParam Long roomId,
            @Parameter(description = "Check-in date (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @Parameter(description = "Check-out date (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate) {
        logger.debug("Getting conflicting reservations for room ID: {} between {} and {}", roomId, checkInDate, checkOutDate);
        List<ReservationDto> conflicts = reservationService.getConflictingReservations(roomId, checkInDate, checkOutDate);
        return ResponseEntity.ok(conflicts);
    }

    /**
     * Confirm reservation
     */
    @PutMapping("/{id}/confirm")
    @Operation(summary = "Confirm reservation", description = "Confirms a pending reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation confirmed successfully",
                    content = @Content(schema = @Schema(implementation = ReservationDto.class))),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    public ResponseEntity<ReservationDto> confirmReservation(
            @Parameter(description = "Reservation ID", required = true)
            @PathVariable Long id) {
        logger.info("Confirming reservation with ID: {}", id);
        ReservationDto confirmedReservation = reservationService.confirmReservation(id);
        return ResponseEntity.ok(confirmedReservation);
    }

    /**
     * Cancel reservation
     */
    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel reservation", description = "Cancels a reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation cancelled successfully",
                    content = @Content(schema = @Schema(implementation = ReservationDto.class))),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    public ResponseEntity<ReservationDto> cancelReservation(
            @Parameter(description = "Reservation ID", required = true)
            @PathVariable Long id) {
        logger.info("Cancelling reservation with ID: {}", id);
        ReservationDto cancelledReservation = reservationService.cancelReservation(id);
        return ResponseEntity.ok(cancelledReservation);
    }

    /**
     * Complete reservation
     */
    @PutMapping("/{id}/complete")
    @Operation(summary = "Complete reservation", description = "Marks a reservation as completed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation completed successfully",
                    content = @Content(schema = @Schema(implementation = ReservationDto.class))),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    public ResponseEntity<ReservationDto> completeReservation(
            @Parameter(description = "Reservation ID", required = true)
            @PathVariable Long id) {
        logger.info("Completing reservation with ID: {}", id);
        ReservationDto completedReservation = reservationService.completeReservation(id);
        return ResponseEntity.ok(completedReservation);
    }

    /**
     * Get upcoming reservations by hotel ID
     */
    @GetMapping("/hotel/{hotelId}/upcoming")
    @Operation(summary = "Get upcoming reservations by hotel ID", description = "Retrieves upcoming reservations for a specific hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Upcoming reservations retrieved successfully",
                    content = @Content(schema = @Schema(implementation = List.class)))
    })
    public ResponseEntity<List<ReservationDto>> getUpcomingReservationsByHotelId(
            @Parameter(description = "Hotel ID", required = true)
            @PathVariable Long hotelId) {
        logger.debug("Getting upcoming reservations for hotel ID: {}", hotelId);
        List<ReservationDto> reservations = reservationService.getUpcomingReservationsByHotelId(hotelId);
        return ResponseEntity.ok(reservations);
    }

    /**
     * Get today's check-ins by hotel ID
     */
    @GetMapping("/hotel/{hotelId}/check-ins/today")
    @Operation(summary = "Get today's check-ins by hotel ID", description = "Retrieves today's check-ins for a specific hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Today's check-ins retrieved successfully",
                    content = @Content(schema = @Schema(implementation = List.class)))
    })
    public ResponseEntity<List<ReservationDto>> getTodayCheckInsByHotelId(
            @Parameter(description = "Hotel ID", required = true)
            @PathVariable Long hotelId) {
        logger.debug("Getting today's check-ins for hotel ID: {}", hotelId);
        List<ReservationDto> checkIns = reservationService.getTodayCheckInsByHotelId(hotelId);
        return ResponseEntity.ok(checkIns);
    }

    /**
     * Get today's check-outs by hotel ID
     */
    @GetMapping("/hotel/{hotelId}/check-outs/today")
    @Operation(summary = "Get today's check-outs by hotel ID", description = "Retrieves today's check-outs for a specific hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Today's check-outs retrieved successfully",
                    content = @Content(schema = @Schema(implementation = List.class)))
    })
    public ResponseEntity<List<ReservationDto>> getTodayCheckOutsByHotelId(
            @Parameter(description = "Hotel ID", required = true)
            @PathVariable Long hotelId) {
        logger.debug("Getting today's check-outs for hotel ID: {}", hotelId);
        List<ReservationDto> checkOuts = reservationService.getTodayCheckOutsByHotelId(hotelId);
        return ResponseEntity.ok(checkOuts);
    }

    /**
     * Get overdue reservations by hotel ID
     */
    @GetMapping("/hotel/{hotelId}/overdue")
    @Operation(summary = "Get overdue reservations by hotel ID", description = "Retrieves overdue reservations for a specific hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Overdue reservations retrieved successfully",
                    content = @Content(schema = @Schema(implementation = List.class)))
    })
    public ResponseEntity<List<ReservationDto>> getOverdueReservationsByHotelId(
            @Parameter(description = "Hotel ID", required = true)
            @PathVariable Long hotelId) {
        logger.debug("Getting overdue reservations for hotel ID: {}", hotelId);
        List<ReservationDto> overdue = reservationService.getOverdueReservationsByHotelId(hotelId);
        return ResponseEntity.ok(overdue);
    }

    /**
     * Get reservation statistics by hotel ID
     */
    @GetMapping("/hotel/{hotelId}/statistics")
    @Operation(summary = "Get reservation statistics by hotel ID", description = "Retrieves reservation statistics for a specific hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    })
    public ResponseEntity<Object> getReservationStatisticsByHotelId(
            @Parameter(description = "Hotel ID", required = true)
            @PathVariable Long hotelId) {
        logger.debug("Getting reservation statistics for hotel ID: {}", hotelId);
        
        long totalReservations = reservationService.getReservationCountByHotelId(hotelId);
        long pendingReservations = reservationService.getReservationCountByHotelIdAndStatus(hotelId, ReservationStatus.PENDING);
        long confirmedReservations = reservationService.getReservationCountByHotelIdAndStatus(hotelId, ReservationStatus.CONFIRMED);
        long cancelledReservations = reservationService.getReservationCountByHotelIdAndStatus(hotelId, ReservationStatus.CANCELLED);
        long completedReservations = reservationService.getReservationCountByHotelIdAndStatus(hotelId, ReservationStatus.COMPLETED);
        
        BigDecimal averagePrice = reservationService.getAveragePriceByHotelId(hotelId);
        BigDecimal totalRevenue = reservationService.getTotalRevenueByHotelId(hotelId);
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalReservations", totalReservations);
        statistics.put("pendingReservations", pendingReservations);
        statistics.put("confirmedReservations", confirmedReservations);
        statistics.put("cancelledReservations", cancelledReservations);
        statistics.put("completedReservations", completedReservations);
        statistics.put("averagePrice", averagePrice);
        statistics.put("totalRevenue", totalRevenue);
        
        return ResponseEntity.ok(statistics);
    }
} 