package com.hotelreservation.notification.service.impl;

import com.hotelreservation.notification.event.ReservationCreatedEvent;
import com.hotelreservation.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

/**
 * Notification Service Implementation
 *
 * Implementation of notification service for handling reservation events.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Autowired
    public NotificationServiceImpl() {
    }

    @Override
    public void processReservationCreatedEvent(ReservationCreatedEvent event) {
        logger.info("Processing reservation created event for reservation ID: {}", event.getReservationId());

        try {
            // Send email notification to guest
            sendEmailNotificationToGuest(event);

            // Send SMS notification to guest (if phone number is provided)
            if (event.getGuestPhone() != null && !event.getGuestPhone().trim().isEmpty()) {
                sendSmsNotificationToGuest(event);
            }

            // Log the notification event
            logNotificationEvent("RESERVATION_CREATED", 
                    event.getGuestEmail(), 
                    "Reservation confirmation sent", 
                    true);

            logger.info("Successfully processed reservation created event for reservation ID: {}", event.getReservationId());

        } catch (Exception e) {
            logger.error("Error processing reservation created event for reservation ID: {}. Error: {}", 
                    event.getReservationId(), e.getMessage(), e);
            
            logNotificationEvent("RESERVATION_CREATED", 
                    event.getGuestEmail(), 
                    "Failed to send notification: " + e.getMessage(), 
                    false);
        }
    }

    @Override
    public void sendEmailNotification(String to, String subject, String body) {
        logger.info("Sending email notification to: {}", to);
        
        // Simulate email sending
        try {
            // In a real implementation, this would use an email service like SendGrid, AWS SES, etc.
            Thread.sleep(100); // Simulate network delay
            
            logger.info("Email notification sent successfully to: {}", to);
            logger.debug("Email subject: {}", subject);
            logger.debug("Email body: {}", body);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Email notification interrupted for: {}", to, e);
            throw new RuntimeException("Email notification interrupted", e);
        } catch (Exception e) {
            logger.error("Failed to send email notification to: {}. Error: {}", to, e.getMessage(), e);
            throw new RuntimeException("Failed to send email notification", e);
        }
    }

    @Override
    public void sendSmsNotification(String phoneNumber, String message) {
        logger.info("Sending SMS notification to: {}", phoneNumber);
        
        // Simulate SMS sending
        try {
            // In a real implementation, this would use an SMS service like Twilio, AWS SNS, etc.
            Thread.sleep(50); // Simulate network delay
            
            logger.info("SMS notification sent successfully to: {}", phoneNumber);
            logger.debug("SMS message: {}", message);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("SMS notification interrupted for: {}", phoneNumber, e);
            throw new RuntimeException("SMS notification interrupted", e);
        } catch (Exception e) {
            logger.error("Failed to send SMS notification to: {}. Error: {}", phoneNumber, e.getMessage(), e);
            throw new RuntimeException("Failed to send SMS notification", e);
        }
    }

    @Override
    public void logNotificationEvent(String eventType, String recipient, String message, boolean success) {
        if (success) {
            logger.info("Notification event logged - Type: {}, Recipient: {}, Message: {}", 
                    eventType, recipient, message);
        } else {
            logger.error("Notification event failed - Type: {}, Recipient: {}, Message: {}", 
                    eventType, recipient, message);
        }
    }

    /**
     * Send email notification to guest
     *
     * @param event the reservation created event
     */
    private void sendEmailNotificationToGuest(ReservationCreatedEvent event) {
        String subject = "Reservation Confirmation - Hotel Reservation System";
        String body = buildEmailBody(event);
        
        sendEmailNotification(event.getGuestEmail(), subject, body);
    }

    /**
     * Send SMS notification to guest
     *
     * @param event the reservation created event
     */
    private void sendSmsNotificationToGuest(ReservationCreatedEvent event) {
        String message = buildSmsMessage(event);
        
        sendSmsNotification(event.getGuestPhone(), message);
    }

    /**
     * Build email body for reservation confirmation
     *
     * @param event the reservation created event
     * @return the email body
     */
    private String buildEmailBody(ReservationCreatedEvent event) {
        StringBuilder body = new StringBuilder();
        body.append("Dear ").append(event.getGuestName()).append(",\n\n");
        body.append("Thank you for your reservation with Hotel Reservation System.\n\n");
        body.append("Reservation Details:\n");
        body.append("- Reservation ID: ").append(event.getReservationId()).append("\n");
        body.append("- Hotel ID: ").append(event.getHotelId()).append("\n");
        body.append("- Room ID: ").append(event.getRoomId()).append("\n");
        body.append("- Check-in Date: ").append(event.getCheckInDate().format(DATE_FORMATTER)).append("\n");
        body.append("- Check-out Date: ").append(event.getCheckOutDate().format(DATE_FORMATTER)).append("\n");
        body.append("- Number of Guests: ").append(event.getNumberOfGuests()).append("\n");
        body.append("- Total Price: $").append(event.getTotalPrice()).append("\n");
        body.append("- Status: ").append(event.getStatus()).append("\n");
        
        if (event.getSpecialRequests() != null && !event.getSpecialRequests().trim().isEmpty()) {
            body.append("- Special Requests: ").append(event.getSpecialRequests()).append("\n");
        }
        
        body.append("\nWe look forward to welcoming you!\n\n");
        body.append("Best regards,\n");
        body.append("Hotel Reservation System Team");
        
        return body.toString();
    }

    /**
     * Build SMS message for reservation confirmation
     *
     * @param event the reservation created event
     * @return the SMS message
     */
    private String buildSmsMessage(ReservationCreatedEvent event) {
        return String.format("Reservation confirmed! ID: %d, Check-in: %s, Check-out: %s, Total: $%s. " +
                "Thank you for choosing Hotel Reservation System.", 
                event.getReservationId(),
                event.getCheckInDate().format(DATE_FORMATTER),
                event.getCheckOutDate().format(DATE_FORMATTER),
                event.getTotalPrice());
    }
} 