package com.hotelreservation.hotel.service.impl;

import com.hotelreservation.hotel.dto.HotelDto;
import com.hotelreservation.hotel.entity.Hotel;
import com.hotelreservation.hotel.exception.HotelAlreadyExistsException;
import com.hotelreservation.hotel.exception.HotelNotFoundException;
import com.hotelreservation.hotel.repository.HotelRepository;
import com.hotelreservation.hotel.service.HotelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Hotel Service Implementation
 *
 * Business logic implementation for Hotel operations.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@Service
@Transactional
public class HotelServiceImpl implements HotelService {

    private static final Logger logger = LoggerFactory.getLogger(HotelServiceImpl.class);

    private final HotelRepository hotelRepository;

    @Autowired
    public HotelServiceImpl(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    @Transactional
    public HotelDto createHotel(HotelDto hotelDto) {
        logger.info("Creating hotel: {}", hotelDto.getName());

        // Check if hotel already exists
        if (hotelExistsByNameAndCity(hotelDto.getName(), hotelDto.getCity())) {
            throw new HotelAlreadyExistsException(hotelDto.getName(), hotelDto.getCity());
        }

        // Convert DTO to entity
        Hotel hotel = convertToEntity(hotelDto);
        hotel.setActive(true);

        // Save hotel
        Hotel savedHotel = hotelRepository.save(hotel);
        logger.info("Hotel created successfully with ID: {}", savedHotel.getId());

        return convertToDto(savedHotel);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HotelDto> getHotelById(Long id) {
        logger.debug("Getting hotel by ID: {}", id);
        return hotelRepository.findByIdAndActiveTrue(id)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HotelDto> getAllHotels() {
        logger.debug("Getting all active hotels");
        return hotelRepository.findByActiveTrue()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HotelDto> getAllHotels(Pageable pageable) {
        logger.debug("Getting all active hotels with pagination");
        return hotelRepository.findByActiveTrue(pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional
    public HotelDto updateHotel(Long id, HotelDto hotelDto) {
        logger.info("Updating hotel with ID: {}", id);

        Hotel existingHotel = hotelRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new HotelNotFoundException(id));

        // Update hotel fields
        existingHotel.setName(hotelDto.getName());
        existingHotel.setAddress(hotelDto.getAddress());
        existingHotel.setCity(hotelDto.getCity());
        existingHotel.setPhone(hotelDto.getPhone());
        existingHotel.setEmail(hotelDto.getEmail());
        existingHotel.setDescription(hotelDto.getDescription());
        existingHotel.setRating(hotelDto.getRating());

        Hotel updatedHotel = hotelRepository.save(existingHotel);
        logger.info("Hotel updated successfully with ID: {}", updatedHotel.getId());

        return convertToDto(updatedHotel);
    }

    @Override
    @Transactional
    public void deleteHotel(Long id) {
        logger.info("Deleting hotel with ID: {}", id);

        Hotel hotel = hotelRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new HotelNotFoundException(id));

        hotel.softDelete();
        hotelRepository.save(hotel);
        logger.info("Hotel deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HotelDto> getHotelsByCity(String city) {
        logger.debug("Getting hotels by city: {}", city);
        return hotelRepository.findByCityAndActiveTrue(city)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HotelDto> getHotelsByCity(String city, Pageable pageable) {
        logger.debug("Getting hotels by city: {} with pagination", city);
        return hotelRepository.findByCityAndActiveTrue(city, pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HotelDto> getHotelsByRatingRange(Integer minRating, Integer maxRating) {
        logger.debug("Getting hotels by rating range: {} - {}", minRating, maxRating);
        return hotelRepository.findByRatingBetweenAndActiveTrue(minRating, maxRating)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HotelDto> getHotelsByName(String name) {
        logger.debug("Getting hotels by name: {}", name);
        return hotelRepository.findByNameContainingIgnoreCaseAndActiveTrue(name)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HotelDto> getHotelsByName(String name, Pageable pageable) {
        logger.debug("Getting hotels by name: {} with pagination", name);
        return hotelRepository.findByNameContainingIgnoreCaseAndActiveTrue(name, pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HotelDto> getHotelsByMinimumRating(Integer minRating) {
        logger.debug("Getting hotels with minimum rating: {}", minRating);
        return hotelRepository.findByRatingGreaterThanEqualAndActiveTrue(minRating)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hotelExists(Long id) {
        return hotelRepository.findByIdAndActiveTrue(id).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hotelExistsByNameAndCity(String name, String city) {
        return hotelRepository.existsByNameAndCityAndActiveTrue(name, city);
    }

    @Override
    @Transactional(readOnly = true)
    public long countHotelsByCity(String city) {
        return hotelRepository.countByCityAndActiveTrue(city);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getHotelsWithRoomCount() {
        logger.debug("Getting hotels with room count");
        return hotelRepository.findHotelsWithRoomCount();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getHotelsByCityWithAveragePrice(String city) {
        logger.debug("Getting hotels by city with average price: {}", city);
        return hotelRepository.findHotelsByCityWithAveragePrice(city);
    }

    /**
     * Convert Hotel entity to HotelDto
     *
     * @param hotel hotel entity
     * @return hotel DTO
     */
    private HotelDto convertToDto(Hotel hotel) {
        HotelDto dto = new HotelDto();
        dto.setId(hotel.getId());
        dto.setName(hotel.getName());
        dto.setAddress(hotel.getAddress());
        dto.setCity(hotel.getCity());
        dto.setPhone(hotel.getPhone());
        dto.setEmail(hotel.getEmail());
        dto.setDescription(hotel.getDescription());
        dto.setRating(hotel.getRating());
        dto.setActive(hotel.getActive());
        dto.setCreatedAt(hotel.getCreatedAt());
        dto.setUpdatedAt(hotel.getUpdatedAt());
        dto.setVersion(hotel.getVersion());
        return dto;
    }

    /**
     * Convert HotelDto to Hotel entity
     *
     * @param dto hotel DTO
     * @return hotel entity
     */
    private Hotel convertToEntity(HotelDto dto) {
        Hotel hotel = new Hotel();
        hotel.setId(dto.getId());
        hotel.setName(dto.getName());
        hotel.setAddress(dto.getAddress());
        hotel.setCity(dto.getCity());
        hotel.setPhone(dto.getPhone());
        hotel.setEmail(dto.getEmail());
        hotel.setDescription(dto.getDescription());
        hotel.setRating(dto.getRating());
        hotel.setActive(dto.getActive());
        hotel.setCreatedAt(dto.getCreatedAt());
        hotel.setUpdatedAt(dto.getUpdatedAt());
        hotel.setVersion(dto.getVersion());
        return hotel;
    }
} 