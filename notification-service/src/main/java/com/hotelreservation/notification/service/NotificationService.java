package com.hotelreservation.notification.service;

import com.hotelreservation.notification.event.ReservationCreatedEvent;

/**
 * Notification Service Interface
 *
 * Service interface for handling notifications.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
public interface NotificationService {

    /**
     * Process reservation created event
     *
     * @param event the reservation created event
     */
    void processReservationCreatedEvent(ReservationCreatedEvent event);

    /**
     * Send email notification
     *
     * @param to recipient email
     * @param subject email subject
     * @param body email body
     */
    void sendEmailNotification(String to, String subject, String body);

    /**
     * Send SMS notification
     *
     * @param phoneNumber recipient phone number
     * @param message SMS message
     */
    void sendSmsNotification(String phoneNumber, String message);

    /**
     * Log notification event
     *
     * @param eventType type of event
     * @param recipient recipient information
     * @param message notification message
     * @param success whether the notification was sent successfully
     */
    void logNotificationEvent(String eventType, String recipient, String message, boolean success);
} 