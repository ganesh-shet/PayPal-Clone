package com.paypal.notification_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.notification_service.entity.Notification;
import com.paypal.notification_service.entity.Transaction;
import com.paypal.notification_service.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class NotificationConsumer {
    // Kafka consumer logic would go here
    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;


    @KafkaListener(topics = "txn-initiated", groupId = "notification-service-group")
    public void consumeTransaction(Transaction transaction) {
        try {
            System.out.println("Received transaction message: " + transaction);

            Notification notification = new Notification();
            notification.setUserId(transaction.getSenderId());

            String notify = "Transaction of amount $" + transaction.getAmount() + " from User " + transaction.getSenderId() + " to User " + transaction.getReceiverId() + " was successful.";
            notification.setMessage(notify);
            LocalDateTime timestamp = LocalDateTime.now();
            notification.setSentAt(timestamp);

            notificationRepository.save(notification);
            System.out.println("✅ Processed and saved notification for transaction: " + notification);

        } catch (Exception e) {
            System.err.println("❌ Failed to process consumed message: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
