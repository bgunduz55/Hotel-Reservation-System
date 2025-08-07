package com.hotelreservation.reservation.service;

import com.hotelreservation.reservation.dto.ReservationDto;
import com.hotelreservation.reservation.event.ReservationCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Event Publishing Service
 *
 * Service responsible for publishing events to Kafka.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@Service
public class EventPublishingService {

    private static final Logger logger = LoggerFactory.getLogger(EventPublishingService.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.reservation-created:reservation-created-events}")
    private String reservationCreatedTopic;

    @Autowired
    public EventPublishingService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Publish reservation created event
     *
     * @param reservationDto the reservation data
     */
    public void publishReservationCreatedEvent(ReservationDto reservationDto) {
        try {
            ReservationCreatedEvent event = createReservationCreatedEvent(reservationDto);
            
            logger.info("Publishing reservation created event for reservation ID: {}", reservationDto.getId());
            
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(
                    reservationCreatedTopic,
                    reservationDto.getId().toString(),
                    event
            );

            future.whenComplete((result, throwable) -> {
                if (throwable == null) {
                    logger.info("Reservation created event published successfully for reservation ID: {}. " +
                            "Topic: {}, Partition: {}, Offset: {}", 
                            reservationDto.getId(), 
                            result.getRecordMetadata().topic(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                } else {
                    logger.error("Failed to publish reservation created event for reservation ID: {}. Error: {}", 
                            reservationDto.getId(), throwable.getMessage(), throwable);
                }
            });

        } catch (Exception e) {
            logger.error("Error publishing reservation created event for reservation ID: {}. Error: {}", 
                    reservationDto.getId(), e.getMessage(), e);
            throw new RuntimeException("Failed to publish reservation created event", e);
        }
    }

    /**
     * Create ReservationCreatedEvent from ReservationDto
     *
     * @param reservationDto the reservation data
     * @return the event
     */
    private ReservationCreatedEvent createReservationCreatedEvent(ReservationDto reservationDto) {
        return new ReservationCreatedEvent(
                reservationDto.getId(),
                reservationDto.getHotelId(),
                reservationDto.getRoomId(),
                reservationDto.getGuestName(),
                reservationDto.getGuestEmail(),
                reservationDto.getGuestPhone(),
                reservationDto.getCheckInDate(),
                reservationDto.getCheckOutDate(),
                reservationDto.getNumberOfGuests(),
                reservationDto.getTotalPrice(),
                reservationDto.getStatus() != null ? reservationDto.getStatus().name() : "PENDING",
                reservationDto.getSpecialRequests(),
                reservationDto.getCreatedAt()
        );
    }

    /**
     * Get the reservation created topic name
     *
     * @return the topic name
     */
    public String getReservationCreatedTopic() {
        return reservationCreatedTopic;
    }
} 