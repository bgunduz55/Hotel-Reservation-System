package com.hotelreservation.hotel.controller;

import com.hotelreservation.hotel.dto.HotelDto;
import com.hotelreservation.hotel.service.HotelService;
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

import java.util.List;
import java.util.Optional;

/**
 * Hotel Controller
 *
 * REST API endpoints for Hotel operations.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/hotels")
@Tag(name = "Hotel", description = "Hotel management API")
public class HotelController {

    private static final Logger logger = LoggerFactory.getLogger(HotelController.class);

    private final HotelService hotelService;

    @Autowired
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    /**
     * Create a new hotel
     */
    @PostMapping
    @Operation(summary = "Create a new hotel", description = "Creates a new hotel with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Hotel created successfully",
            content = @Content(schema = @Schema(implementation = HotelDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Hotel already exists")
    })
    public ResponseEntity<HotelDto> createHotel(@Valid @RequestBody HotelDto hotelDto) {
        logger.info("Creating hotel: {}", hotelDto.getName());
        HotelDto createdHotel = hotelService.createHotel(hotelDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHotel);
    }

    /**
     * Get hotel by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get hotel by ID", description = "Retrieves a hotel by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hotel found",
            content = @Content(schema = @Schema(implementation = HotelDto.class))),
        @ApiResponse(responseCode = "404", description = "Hotel not found")
    })
    public ResponseEntity<HotelDto> getHotelById(@Parameter(description = "Hotel ID") @PathVariable Long id) {
        logger.debug("Getting hotel by ID: {}", id);
        Optional<HotelDto> hotel = hotelService.getHotelById(id);
        return hotel.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all hotels with pagination
     */
    @GetMapping
    @Operation(summary = "Get all hotels", description = "Retrieves all hotels with pagination support")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hotels retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<HotelDto>> getAllHotels(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "ASC") String sortDir) {
        
        logger.debug("Getting all hotels with pagination: page={}, size={}, sortBy={}, sortDir={}", 
                page, size, sortBy, sortDir);
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<HotelDto> hotels = hotelService.getAllHotels(pageable);
        
        return ResponseEntity.ok(hotels);
    }

    /**
     * Update hotel
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update hotel", description = "Updates an existing hotel")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hotel updated successfully",
            content = @Content(schema = @Schema(implementation = HotelDto.class))),
        @ApiResponse(responseCode = "404", description = "Hotel not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<HotelDto> updateHotel(
            @Parameter(description = "Hotel ID") @PathVariable Long id,
            @Valid @RequestBody HotelDto hotelDto) {
        
        logger.info("Updating hotel with ID: {}", id);
        HotelDto updatedHotel = hotelService.updateHotel(id, hotelDto);
        return ResponseEntity.ok(updatedHotel);
    }

    /**
     * Delete hotel
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete hotel", description = "Soft deletes a hotel")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Hotel deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Hotel not found")
    })
    public ResponseEntity<Void> deleteHotel(@Parameter(description = "Hotel ID") @PathVariable Long id) {
        logger.info("Deleting hotel with ID: {}", id);
        hotelService.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get hotels by city
     */
    @GetMapping("/city/{city}")
    @Operation(summary = "Get hotels by city", description = "Retrieves all hotels in a specific city")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hotels retrieved successfully",
            content = @Content(schema = @Schema(implementation = HotelDto.class)))
    })
    public ResponseEntity<List<HotelDto>> getHotelsByCity(
            @Parameter(description = "City name") @PathVariable String city) {
        
        logger.debug("Getting hotels by city: {}", city);
        List<HotelDto> hotels = hotelService.getHotelsByCity(city);
        return ResponseEntity.ok(hotels);
    }

    /**
     * Get hotels by city with pagination
     */
    @GetMapping("/city/{city}/page")
    @Operation(summary = "Get hotels by city with pagination", description = "Retrieves hotels in a city with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hotels retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<HotelDto>> getHotelsByCityWithPagination(
            @Parameter(description = "City name") @PathVariable String city,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        logger.debug("Getting hotels by city with pagination: city={}, page={}, size={}", city, page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<HotelDto> hotels = hotelService.getHotelsByCity(city, pageable);
        return ResponseEntity.ok(hotels);
    }

    /**
     * Get hotels by rating range
     */
    @GetMapping("/rating")
    @Operation(summary = "Get hotels by rating range", description = "Retrieves hotels within a rating range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hotels retrieved successfully",
            content = @Content(schema = @Schema(implementation = HotelDto.class)))
    })
    public ResponseEntity<List<HotelDto>> getHotelsByRatingRange(
            @Parameter(description = "Minimum rating") @RequestParam Integer minRating,
            @Parameter(description = "Maximum rating") @RequestParam Integer maxRating) {
        
        logger.debug("Getting hotels by rating range: {} - {}", minRating, maxRating);
        List<HotelDto> hotels = hotelService.getHotelsByRatingRange(minRating, maxRating);
        return ResponseEntity.ok(hotels);
    }

    /**
     * Get hotels by name
     */
    @GetMapping("/search")
    @Operation(summary = "Search hotels by name", description = "Retrieves hotels by name (partial match)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hotels retrieved successfully",
            content = @Content(schema = @Schema(implementation = HotelDto.class)))
    })
    public ResponseEntity<List<HotelDto>> getHotelsByName(
            @Parameter(description = "Hotel name (partial match)") @RequestParam String name) {
        
        logger.debug("Getting hotels by name: {}", name);
        List<HotelDto> hotels = hotelService.getHotelsByName(name);
        return ResponseEntity.ok(hotels);
    }

    /**
     * Get hotels by name with pagination
     */
    @GetMapping("/search/page")
    @Operation(summary = "Search hotels by name with pagination", description = "Retrieves hotels by name with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hotels retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<HotelDto>> getHotelsByNameWithPagination(
            @Parameter(description = "Hotel name (partial match)") @RequestParam String name,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        logger.debug("Getting hotels by name with pagination: name={}, page={}, size={}", name, page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<HotelDto> hotels = hotelService.getHotelsByName(name, pageable);
        return ResponseEntity.ok(hotels);
    }

    /**
     * Get hotels with minimum rating
     */
    @GetMapping("/rating/min")
    @Operation(summary = "Get hotels with minimum rating", description = "Retrieves hotels with minimum rating")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hotels retrieved successfully",
            content = @Content(schema = @Schema(implementation = HotelDto.class)))
    })
    public ResponseEntity<List<HotelDto>> getHotelsByMinimumRating(
            @Parameter(description = "Minimum rating") @RequestParam Integer minRating) {
        
        logger.debug("Getting hotels with minimum rating: {}", minRating);
        List<HotelDto> hotels = hotelService.getHotelsByMinimumRating(minRating);
        return ResponseEntity.ok(hotels);
    }

    /**
     * Check if hotel exists
     */
    @GetMapping("/{id}/exists")
    @Operation(summary = "Check if hotel exists", description = "Checks if a hotel exists by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hotel exists"),
        @ApiResponse(responseCode = "404", description = "Hotel not found")
    })
    public ResponseEntity<Void> hotelExists(@Parameter(description = "Hotel ID") @PathVariable Long id) {
        logger.debug("Checking if hotel exists: {}", id);
        boolean exists = hotelService.hotelExists(id);
        return exists ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    /**
     * Count hotels by city
     */
    @GetMapping("/city/{city}/count")
    @Operation(summary = "Count hotels by city", description = "Counts the number of hotels in a city")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Count retrieved successfully")
    })
    public ResponseEntity<Long> countHotelsByCity(@Parameter(description = "City name") @PathVariable String city) {
        logger.debug("Counting hotels by city: {}", city);
        long count = hotelService.countHotelsByCity(city);
        return ResponseEntity.ok(count);
    }

    /**
     * Get hotels with room count
     */
    @GetMapping("/with-room-count")
    @Operation(summary = "Get hotels with room count", description = "Retrieves hotels with their room count")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hotels with room count retrieved successfully")
    })
    public ResponseEntity<List<Object[]>> getHotelsWithRoomCount() {
        logger.debug("Getting hotels with room count");
        List<Object[]> hotelsWithRoomCount = hotelService.getHotelsWithRoomCount();
        return ResponseEntity.ok(hotelsWithRoomCount);
    }

    /**
     * Get hotels by city with average room price
     */
    @GetMapping("/city/{city}/average-price")
    @Operation(summary = "Get hotels by city with average price", description = "Retrieves hotels in a city with average room price")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hotels with average price retrieved successfully")
    })
    public ResponseEntity<List<Object[]>> getHotelsByCityWithAveragePrice(
            @Parameter(description = "City name") @PathVariable String city) {
        
        logger.debug("Getting hotels by city with average price: {}", city);
        List<Object[]> hotelsWithAveragePrice = hotelService.getHotelsByCityWithAveragePrice(city);
        return ResponseEntity.ok(hotelsWithAveragePrice);
    }
} 