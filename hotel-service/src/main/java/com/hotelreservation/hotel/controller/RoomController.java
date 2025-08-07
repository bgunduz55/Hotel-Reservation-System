package com.hotelreservation.hotel.controller;

import com.hotelreservation.hotel.dto.RoomDto;
import com.hotelreservation.hotel.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Room Controller
 *
 * REST API endpoints for Room operations.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/rooms")
@Tag(name = "Room", description = "Room management API")
public class RoomController {

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Create a new room
     */
    @PostMapping
    @Operation(summary = "Create a new room", description = "Creates a new room with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Room created successfully",
            content = @Content(schema = @Schema(implementation = RoomDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Hotel not found"),
        @ApiResponse(responseCode = "409", description = "Room already exists")
    })
    public ResponseEntity<RoomDto> createRoom(@Valid @RequestBody RoomDto roomDto) {
        logger.info("Creating room: {} for hotel ID: {}", roomDto.getRoomNumber(), roomDto.getHotelId());
        RoomDto createdRoom = roomService.createRoom(roomDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
    }

    /**
     * Get room by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get room by ID", description = "Retrieves a room by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Room found",
            content = @Content(schema = @Schema(implementation = RoomDto.class))),
        @ApiResponse(responseCode = "404", description = "Room not found")
    })
    public ResponseEntity<RoomDto> getRoomById(@Parameter(description = "Room ID") @PathVariable Long id) {
        logger.debug("Getting room by ID: {}", id);
        Optional<RoomDto> room = roomService.getRoomById(id);
        return room.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all rooms with pagination
     */
    @GetMapping
    @Operation(summary = "Get all rooms", description = "Retrieves all rooms with pagination support")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rooms retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<RoomDto>> getAllRooms(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "ASC") String sortDir) {
        
        logger.debug("Getting all rooms with pagination: page={}, size={}, sortBy={}, sortDir={}", 
                page, size, sortBy, sortDir);
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<RoomDto> rooms = roomService.getAllRooms(pageable);
        
        return ResponseEntity.ok(rooms);
    }

    /**
     * Update room
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update room", description = "Updates an existing room")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Room updated successfully",
            content = @Content(schema = @Schema(implementation = RoomDto.class))),
        @ApiResponse(responseCode = "404", description = "Room not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<RoomDto> updateRoom(
            @Parameter(description = "Room ID") @PathVariable Long id,
            @Valid @RequestBody RoomDto roomDto) {
        
        logger.info("Updating room with ID: {}", id);
        RoomDto updatedRoom = roomService.updateRoom(id, roomDto);
        return ResponseEntity.ok(updatedRoom);
    }

    /**
     * Delete room
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete room", description = "Soft deletes a room")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Room deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Room not found")
    })
    public ResponseEntity<Void> deleteRoom(@Parameter(description = "Room ID") @PathVariable Long id) {
        logger.info("Deleting room with ID: {}", id);
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get rooms by hotel ID
     */
    @GetMapping("/hotel/{hotelId}")
    @Operation(summary = "Get rooms by hotel ID", description = "Retrieves all rooms for a specific hotel")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rooms retrieved successfully",
            content = @Content(schema = @Schema(implementation = RoomDto.class)))
    })
    public ResponseEntity<List<RoomDto>> getRoomsByHotelId(
            @Parameter(description = "Hotel ID") @PathVariable Long hotelId) {
        
        logger.debug("Getting rooms by hotel ID: {}", hotelId);
        List<RoomDto> rooms = roomService.getRoomsByHotelId(hotelId);
        return ResponseEntity.ok(rooms);
    }

    /**
     * Get rooms by hotel ID with pagination
     */
    @GetMapping("/hotel/{hotelId}/page")
    @Operation(summary = "Get rooms by hotel ID with pagination", description = "Retrieves rooms for a hotel with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rooms retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<RoomDto>> getRoomsByHotelIdWithPagination(
            @Parameter(description = "Hotel ID") @PathVariable Long hotelId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        logger.debug("Getting rooms by hotel ID with pagination: hotelId={}, page={}, size={}", hotelId, page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<RoomDto> rooms = roomService.getRoomsByHotelId(hotelId, pageable);
        return ResponseEntity.ok(rooms);
    }

    /**
     * Get available rooms by hotel ID
     */
    @GetMapping("/hotel/{hotelId}/available")
    @Operation(summary = "Get available rooms by hotel ID", description = "Retrieves all available rooms for a hotel")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Available rooms retrieved successfully",
            content = @Content(schema = @Schema(implementation = RoomDto.class)))
    })
    public ResponseEntity<List<RoomDto>> getAvailableRoomsByHotelId(
            @Parameter(description = "Hotel ID") @PathVariable Long hotelId) {
        
        logger.debug("Getting available rooms by hotel ID: {}", hotelId);
        List<RoomDto> rooms = roomService.getAvailableRoomsByHotelId(hotelId);
        return ResponseEntity.ok(rooms);
    }

    /**
     * Get rooms by type
     */
    @GetMapping("/type/{roomType}")
    @Operation(summary = "Get rooms by type", description = "Retrieves all rooms of a specific type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rooms retrieved successfully",
            content = @Content(schema = @Schema(implementation = RoomDto.class)))
    })
    public ResponseEntity<List<RoomDto>> getRoomsByType(
            @Parameter(description = "Room type") @PathVariable String roomType) {
        
        logger.debug("Getting rooms by type: {}", roomType);
        List<RoomDto> rooms = roomService.getRoomsByType(roomType);
        return ResponseEntity.ok(rooms);
    }

    /**
     * Get rooms by type and hotel ID
     */
    @GetMapping("/hotel/{hotelId}/type/{roomType}")
    @Operation(summary = "Get rooms by type and hotel ID", description = "Retrieves rooms of a specific type for a hotel")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rooms retrieved successfully",
            content = @Content(schema = @Schema(implementation = RoomDto.class)))
    })
    public ResponseEntity<List<RoomDto>> getRoomsByTypeAndHotelId(
            @Parameter(description = "Hotel ID") @PathVariable Long hotelId,
            @Parameter(description = "Room type") @PathVariable String roomType) {
        
        logger.debug("Getting rooms by type and hotel ID: type={}, hotelId={}", roomType, hotelId);
        List<RoomDto> rooms = roomService.getRoomsByTypeAndHotelId(roomType, hotelId);
        return ResponseEntity.ok(rooms);
    }

    /**
     * Get rooms by capacity range
     */
    @GetMapping("/capacity")
    @Operation(summary = "Get rooms by capacity range", description = "Retrieves rooms within a capacity range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rooms retrieved successfully",
            content = @Content(schema = @Schema(implementation = RoomDto.class)))
    })
    public ResponseEntity<List<RoomDto>> getRoomsByCapacityRange(
            @Parameter(description = "Minimum capacity") @RequestParam Integer minCapacity,
            @Parameter(description = "Maximum capacity") @RequestParam Integer maxCapacity) {
        
        logger.debug("Getting rooms by capacity range: {} - {}", minCapacity, maxCapacity);
        List<RoomDto> rooms = roomService.getRoomsByCapacityRange(minCapacity, maxCapacity);
        return ResponseEntity.ok(rooms);
    }

    /**
     * Get rooms by price range
     */
    @GetMapping("/price")
    @Operation(summary = "Get rooms by price range", description = "Retrieves rooms within a price range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rooms retrieved successfully",
            content = @Content(schema = @Schema(implementation = RoomDto.class)))
    })
    public ResponseEntity<List<RoomDto>> getRoomsByPriceRange(
            @Parameter(description = "Minimum price") @RequestParam BigDecimal minPrice,
            @Parameter(description = "Maximum price") @RequestParam BigDecimal maxPrice) {
        
        logger.debug("Getting rooms by price range: {} - {}", minPrice, maxPrice);
        List<RoomDto> rooms = roomService.getRoomsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(rooms);
    }

    /**
     * Get rooms by hotel ID and price range
     */
    @GetMapping("/hotel/{hotelId}/price")
    @Operation(summary = "Get rooms by hotel ID and price range", description = "Retrieves rooms for a hotel within a price range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rooms retrieved successfully",
            content = @Content(schema = @Schema(implementation = RoomDto.class)))
    })
    public ResponseEntity<List<RoomDto>> getRoomsByHotelIdAndPriceRange(
            @Parameter(description = "Hotel ID") @PathVariable Long hotelId,
            @Parameter(description = "Minimum price") @RequestParam BigDecimal minPrice,
            @Parameter(description = "Maximum price") @RequestParam BigDecimal maxPrice) {
        
        logger.debug("Getting rooms by hotel ID and price range: hotelId={}, minPrice={}, maxPrice={}", 
                hotelId, minPrice, maxPrice);
        List<RoomDto> rooms = roomService.getRoomsByHotelIdAndPriceRange(hotelId, minPrice, maxPrice);
        return ResponseEntity.ok(rooms);
    }

    /**
     * Get available rooms by hotel ID and type
     */
    @GetMapping("/hotel/{hotelId}/type/{roomType}/available")
    @Operation(summary = "Get available rooms by hotel ID and type", description = "Retrieves available rooms of a specific type for a hotel")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Available rooms retrieved successfully",
            content = @Content(schema = @Schema(implementation = RoomDto.class)))
    })
    public ResponseEntity<List<RoomDto>> getAvailableRoomsByHotelIdAndType(
            @Parameter(description = "Hotel ID") @PathVariable Long hotelId,
            @Parameter(description = "Room type") @PathVariable String roomType) {
        
        logger.debug("Getting available rooms by hotel ID and type: hotelId={}, type={}", hotelId, roomType);
        List<RoomDto> rooms = roomService.getAvailableRoomsByHotelIdAndType(hotelId, roomType);
        return ResponseEntity.ok(rooms);
    }

    /**
     * Get room by hotel ID and room number
     */
    @GetMapping("/hotel/{hotelId}/number/{roomNumber}")
    @Operation(summary = "Get room by hotel ID and room number", description = "Retrieves a specific room by hotel ID and room number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Room found",
            content = @Content(schema = @Schema(implementation = RoomDto.class))),
        @ApiResponse(responseCode = "404", description = "Room not found")
    })
    public ResponseEntity<RoomDto> getRoomByHotelIdAndNumber(
            @Parameter(description = "Hotel ID") @PathVariable Long hotelId,
            @Parameter(description = "Room number") @PathVariable String roomNumber) {
        
        logger.debug("Getting room by hotel ID and room number: hotelId={}, roomNumber={}", hotelId, roomNumber);
        Optional<RoomDto> room = roomService.getRoomByHotelIdAndNumber(hotelId, roomNumber);
        return room.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Mark room as unavailable
     */
    @PutMapping("/{id}/unavailable")
    @Operation(summary = "Mark room as unavailable", description = "Marks a room as unavailable")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Room marked as unavailable",
            content = @Content(schema = @Schema(implementation = RoomDto.class))),
        @ApiResponse(responseCode = "404", description = "Room not found")
    })
    public ResponseEntity<RoomDto> markRoomAsUnavailable(@Parameter(description = "Room ID") @PathVariable Long id) {
        logger.info("Marking room as unavailable with ID: {}", id);
        RoomDto updatedRoom = roomService.markRoomAsUnavailable(id);
        return ResponseEntity.ok(updatedRoom);
    }

    /**
     * Mark room as available
     */
    @PutMapping("/{id}/available")
    @Operation(summary = "Mark room as available", description = "Marks a room as available")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Room marked as available",
            content = @Content(schema = @Schema(implementation = RoomDto.class))),
        @ApiResponse(responseCode = "404", description = "Room not found")
    })
    public ResponseEntity<RoomDto> markRoomAsAvailable(@Parameter(description = "Room ID") @PathVariable Long id) {
        logger.info("Marking room as available with ID: {}", id);
        RoomDto updatedRoom = roomService.markRoomAsAvailable(id);
        return ResponseEntity.ok(updatedRoom);
    }

    /**
     * Check if room exists
     */
    @GetMapping("/{id}/exists")
    @Operation(summary = "Check if room exists", description = "Checks if a room exists by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Room exists"),
        @ApiResponse(responseCode = "404", description = "Room not found")
    })
    public ResponseEntity<Void> roomExists(@Parameter(description = "Room ID") @PathVariable Long id) {
        logger.debug("Checking if room exists: {}", id);
        boolean exists = roomService.roomExists(id);
        return exists ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    /**
     * Count rooms by hotel ID
     */
    @GetMapping("/hotel/{hotelId}/count")
    @Operation(summary = "Count rooms by hotel ID", description = "Counts the number of rooms for a hotel")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Count retrieved successfully")
    })
    public ResponseEntity<Long> countRoomsByHotelId(@Parameter(description = "Hotel ID") @PathVariable Long hotelId) {
        logger.debug("Counting rooms by hotel ID: {}", hotelId);
        long count = roomService.countRoomsByHotelId(hotelId);
        return ResponseEntity.ok(count);
    }

    /**
     * Count available rooms by hotel ID
     */
    @GetMapping("/hotel/{hotelId}/available/count")
    @Operation(summary = "Count available rooms by hotel ID", description = "Counts the number of available rooms for a hotel")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Count retrieved successfully")
    })
    public ResponseEntity<Long> countAvailableRoomsByHotelId(@Parameter(description = "Hotel ID") @PathVariable Long hotelId) {
        logger.debug("Counting available rooms by hotel ID: {}", hotelId);
        long count = roomService.countAvailableRoomsByHotelId(hotelId);
        return ResponseEntity.ok(count);
    }

    /**
     * Get rooms with hotel information
     */
    @GetMapping("/hotel/{hotelId}/with-hotel-info")
    @Operation(summary = "Get rooms with hotel information", description = "Retrieves rooms with hotel details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rooms with hotel info retrieved successfully")
    })
    public ResponseEntity<List<Object[]>> getRoomsWithHotelInfo(@Parameter(description = "Hotel ID") @PathVariable Long hotelId) {
        logger.debug("Getting rooms with hotel info for hotel ID: {}", hotelId);
        List<Object[]> roomsWithHotelInfo = roomService.getRoomsWithHotelInfo(hotelId);
        return ResponseEntity.ok(roomsWithHotelInfo);
    }

    /**
     * Get average room price by hotel ID
     */
    @GetMapping("/hotel/{hotelId}/average-price")
    @Operation(summary = "Get average room price by hotel ID", description = "Retrieves the average room price for a hotel")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Average price retrieved successfully")
    })
    public ResponseEntity<BigDecimal> getAverageRoomPriceByHotelId(@Parameter(description = "Hotel ID") @PathVariable Long hotelId) {
        logger.debug("Getting average room price for hotel ID: {}", hotelId);
        BigDecimal averagePrice = roomService.getAverageRoomPriceByHotelId(hotelId);
        return ResponseEntity.ok(averagePrice);
    }

    /**
     * Get rooms by capacity and availability
     */
    @GetMapping("/capacity/{capacity}/availability/{available}")
    @Operation(summary = "Get rooms by capacity and availability", description = "Retrieves rooms by capacity and availability status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rooms retrieved successfully",
            content = @Content(schema = @Schema(implementation = RoomDto.class)))
    })
    public ResponseEntity<List<RoomDto>> getRoomsByCapacityAndAvailability(
            @Parameter(description = "Room capacity") @PathVariable Integer capacity,
            @Parameter(description = "Availability status") @PathVariable Boolean available) {
        
        logger.debug("Getting rooms by capacity and availability: capacity={}, available={}", capacity, available);
        List<RoomDto> rooms = roomService.getRoomsByCapacityAndAvailability(capacity, available);
        return ResponseEntity.ok(rooms);
    }
} 