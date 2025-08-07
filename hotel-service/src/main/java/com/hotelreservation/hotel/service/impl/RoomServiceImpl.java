package com.hotelreservation.hotel.service.impl;

import com.hotelreservation.hotel.dto.RoomDto;
import com.hotelreservation.hotel.entity.Hotel;
import com.hotelreservation.hotel.entity.Room;
import com.hotelreservation.hotel.exception.HotelNotFoundException;
import com.hotelreservation.hotel.exception.RoomAlreadyExistsException;
import com.hotelreservation.hotel.exception.RoomNotFoundException;
import com.hotelreservation.hotel.repository.HotelRepository;
import com.hotelreservation.hotel.repository.RoomRepository;
import com.hotelreservation.hotel.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Room Service Implementation
 *
 * Business logic implementation for Room operations.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@Service
@Transactional
public class RoomServiceImpl implements RoomService {

    private static final Logger logger = LoggerFactory.getLogger(RoomServiceImpl.class);

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository, HotelRepository hotelRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
    }

    @Override
    @Transactional
    public RoomDto createRoom(RoomDto roomDto) {
        logger.info("Creating room: {} for hotel ID: {}", roomDto.getRoomNumber(), roomDto.getHotelId());

        // Check if hotel exists
        Hotel hotel = hotelRepository.findByIdAndActiveTrue(roomDto.getHotelId())
                .orElseThrow(() -> new HotelNotFoundException(roomDto.getHotelId()));

        // Check if room already exists in the hotel
        if (roomExistsByHotelIdAndNumber(roomDto.getHotelId(), roomDto.getRoomNumber())) {
            throw new RoomAlreadyExistsException(roomDto.getHotelId(), roomDto.getRoomNumber());
        }

        // Convert DTO to entity
        Room room = convertToEntity(roomDto);
        room.setHotel(hotel);
        room.setActive(true);

        // Save room
        Room savedRoom = roomRepository.save(room);
        logger.info("Room created successfully with ID: {}", savedRoom.getId());

        return convertToDto(savedRoom);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoomDto> getRoomById(Long id) {
        logger.debug("Getting room by ID: {}", id);
        return roomRepository.findByIdAndActiveTrue(id)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDto> getAllRooms() {
        logger.debug("Getting all active rooms");
        return roomRepository.findByActiveTrue()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoomDto> getAllRooms(Pageable pageable) {
        logger.debug("Getting all active rooms with pagination");
        return roomRepository.findByActiveTrue(pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional
    public RoomDto updateRoom(Long id, RoomDto roomDto) {
        logger.info("Updating room with ID: {}", id);

        Room existingRoom = roomRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RoomNotFoundException(id));

        // Update room fields
        existingRoom.setRoomNumber(roomDto.getRoomNumber());
        existingRoom.setRoomType(roomDto.getRoomType());
        existingRoom.setCapacity(roomDto.getCapacity());
        existingRoom.setPricePerNight(roomDto.getPricePerNight());
        existingRoom.setAvailable(roomDto.getAvailable());
        existingRoom.setDescription(roomDto.getDescription());

        Room updatedRoom = roomRepository.save(existingRoom);
        logger.info("Room updated successfully with ID: {}", updatedRoom.getId());

        return convertToDto(updatedRoom);
    }

    @Override
    @Transactional
    public void deleteRoom(Long id) {
        logger.info("Deleting room with ID: {}", id);

        Room room = roomRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RoomNotFoundException(id));

        room.softDelete();
        roomRepository.save(room);
        logger.info("Room deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDto> getRoomsByHotelId(Long hotelId) {
        logger.debug("Getting rooms by hotel ID: {}", hotelId);
        return roomRepository.findByHotelIdAndActiveTrue(hotelId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoomDto> getRoomsByHotelId(Long hotelId, Pageable pageable) {
        logger.debug("Getting rooms by hotel ID: {} with pagination", hotelId);
        return roomRepository.findByHotelIdAndActiveTrue(hotelId, pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDto> getAvailableRoomsByHotelId(Long hotelId) {
        logger.debug("Getting available rooms by hotel ID: {}", hotelId);
        return roomRepository.findByHotelIdAndAvailableTrueAndActiveTrue(hotelId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDto> getRoomsByType(String roomType) {
        logger.debug("Getting rooms by type: {}", roomType);
        return roomRepository.findByRoomTypeAndActiveTrue(roomType)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDto> getRoomsByTypeAndHotelId(String roomType, Long hotelId) {
        logger.debug("Getting rooms by type: {} and hotel ID: {}", roomType, hotelId);
        return roomRepository.findByRoomTypeAndHotelIdAndActiveTrue(roomType, hotelId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDto> getRoomsByCapacityRange(Integer minCapacity, Integer maxCapacity) {
        logger.debug("Getting rooms by capacity range: {} - {}", minCapacity, maxCapacity);
        return roomRepository.findByCapacityBetweenAndActiveTrue(minCapacity, maxCapacity)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDto> getRoomsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        logger.debug("Getting rooms by price range: {} - {}", minPrice, maxPrice);
        return roomRepository.findByPricePerNightBetweenAndActiveTrue(minPrice, maxPrice)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDto> getRoomsByHotelIdAndPriceRange(Long hotelId, BigDecimal minPrice, BigDecimal maxPrice) {
        logger.debug("Getting rooms by hotel ID: {} and price range: {} - {}", hotelId, minPrice, maxPrice);
        return roomRepository.findByHotelIdAndPricePerNightBetweenAndActiveTrue(hotelId, minPrice, maxPrice)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDto> getAvailableRoomsByHotelIdAndType(Long hotelId, String roomType) {
        logger.debug("Getting available rooms by hotel ID: {} and type: {}", hotelId, roomType);
        return roomRepository.findByHotelIdAndRoomTypeAndAvailableTrueAndActiveTrue(hotelId, roomType)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoomDto> getRoomByHotelIdAndNumber(Long hotelId, String roomNumber) {
        logger.debug("Getting room by hotel ID: {} and room number: {}", hotelId, roomNumber);
        return roomRepository.findByHotelIdAndRoomNumberAndActiveTrue(hotelId, roomNumber)
                .map(this::convertToDto);
    }

    @Override
    @Transactional
    public RoomDto markRoomAsUnavailable(Long id) {
        logger.info("Marking room as unavailable with ID: {}", id);

        Room room = roomRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RoomNotFoundException(id));

        room.setAvailable(false);
        Room updatedRoom = roomRepository.save(room);
        logger.info("Room marked as unavailable with ID: {}", id);

        return convertToDto(updatedRoom);
    }

    @Override
    @Transactional
    public RoomDto markRoomAsAvailable(Long id) {
        logger.info("Marking room as available with ID: {}", id);

        Room room = roomRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RoomNotFoundException(id));

        room.setAvailable(true);
        Room updatedRoom = roomRepository.save(room);
        logger.info("Room marked as available with ID: {}", id);

        return convertToDto(updatedRoom);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean roomExists(Long id) {
        return roomRepository.findByIdAndActiveTrue(id).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean roomExistsByHotelIdAndNumber(Long hotelId, String roomNumber) {
        return roomRepository.existsByHotelIdAndRoomNumberAndActiveTrue(hotelId, roomNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public long countRoomsByHotelId(Long hotelId) {
        return roomRepository.countByHotelIdAndActiveTrue(hotelId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countAvailableRoomsByHotelId(Long hotelId) {
        return roomRepository.countByHotelIdAndAvailableTrueAndActiveTrue(hotelId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getRoomsWithHotelInfo(Long hotelId) {
        logger.debug("Getting rooms with hotel info for hotel ID: {}", hotelId);
        return roomRepository.findRoomsWithHotelInfo(hotelId);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getAverageRoomPriceByHotelId(Long hotelId) {
        logger.debug("Getting average room price for hotel ID: {}", hotelId);
        return roomRepository.findAveragePriceByHotelId(hotelId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDto> getRoomsByCapacityAndAvailability(Integer capacity, Boolean available) {
        logger.debug("Getting rooms by capacity: {} and availability: {}", capacity, available);
        return roomRepository.findByCapacityAndAvailability(capacity, available)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Convert Room entity to RoomDto
     *
     * @param room room entity
     * @return room DTO
     */
    private RoomDto convertToDto(Room room) {
        RoomDto dto = new RoomDto();
        dto.setId(room.getId());
        dto.setHotelId(room.getHotel().getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setRoomType(room.getRoomType());
        dto.setCapacity(room.getCapacity());
        dto.setPricePerNight(room.getPricePerNight());
        dto.setAvailable(room.getAvailable());
        dto.setDescription(room.getDescription());
        dto.setActive(room.getActive());
        dto.setCreatedAt(room.getCreatedAt());
        dto.setUpdatedAt(room.getUpdatedAt());
        dto.setVersion(room.getVersion());
        return dto;
    }

    /**
     * Convert RoomDto to Room entity
     *
     * @param dto room DTO
     * @return room entity
     */
    private Room convertToEntity(RoomDto dto) {
        Room room = new Room();
        room.setId(dto.getId());
        room.setRoomNumber(dto.getRoomNumber());
        room.setRoomType(dto.getRoomType());
        room.setCapacity(dto.getCapacity());
        room.setPricePerNight(dto.getPricePerNight());
        room.setAvailable(dto.getAvailable());
        room.setDescription(dto.getDescription());
        room.setActive(dto.getActive());
        room.setCreatedAt(dto.getCreatedAt());
        room.setUpdatedAt(dto.getUpdatedAt());
        room.setVersion(dto.getVersion());
        return room;
    }
} 