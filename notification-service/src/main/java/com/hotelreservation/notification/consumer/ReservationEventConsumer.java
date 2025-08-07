package com.hotelreservation.notification.consumer;

import com.hotelreservation.notification.event.ReservationCreatedEvent;
import com.hotelreservation.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Reservation Event Consumer
 *
 * Kafka consumer for handling reservation events.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@Component
public class ReservationEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ReservationEventConsumer.class);

    private final NotificationService notificationService;

    @Autowired
    public ReservationEventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Consume reservation created events
     *
     * @param event the reservation created event
     * @param key the message key
     * @param topic the topic name
     * @param partition the partition number
     * @param offset the message offset
     */
    @KafkaListener(
            topics = "${kafka.topic.reservation-created:reservation-created-events}",
            groupId = "${spring.kafka.consumer.group-id:notification-service-group}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeReservationCreatedEvent(
            @Payload ReservationCreatedEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.OFFSET) long offset) {

        logger.info("Received reservation created event - Topic: {}, Offset: {}", 
                topic, offset);
        logger.info("Event details - Reservation ID: {}, Guest: {}, Email: {}", 
                event.getReservationId(), event.getGuestName(), event.getGuestEmail());

        try {
            // Process the event
            notificationService.processReservationCreatedEvent(event);
            
            logger.info("Successfully processed reservation created event for reservation ID: {}", 
                    event.getReservationId());

        } catch (Exception e) {
            logger.error("Error processing reservation created event for reservation ID: {}. Error: {}", 
                    event.getReservationId(), e.getMessage(), e);
            
            // In a production environment, you might want to:
            // 1. Send the message to a dead letter queue
            // 2. Retry the processing
            // 3. Send an alert to monitoring system
            // 4. Log the error for manual investigation
            
            throw new RuntimeException("Failed to process reservation created event", e);
        }
    }

    /**
     * Handle any other reservation events (for future extensibility)
     *
     * @param event the generic event object
     * @param key the message key
     * @param topic the topic name
     */
    @KafkaListener(
            topics = "${kafka.topic.reservation-events:reservation-events}",
            groupId = "${spring.kafka.consumer.group-id:notification-service-group}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeGenericReservationEvent(
            @Payload Object event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        logger.info("Received generic reservation event - Topic: {}", topic);
        logger.debug("Event payload: {}", event);

        // Handle different event types based on event structure
        // This can be extended for other reservation events like:
        // - ReservationCancelledEvent
        // - ReservationUpdatedEvent
        // - ReservationConfirmedEvent
        // etc.
    }
} 