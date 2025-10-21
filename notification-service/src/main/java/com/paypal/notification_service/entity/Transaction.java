package com.paypal.notification_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="sender_user_id" , nullable = false)
    private Long senderId;

    @Column(name = "receiver_user_id", nullable = false)
    private Long receiverId;

    @Column(name = "amount", nullable = false)
    //@Positive(message = " Amount must be positive")
    private Double amount;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String status;

    @PrePersist
    public void prePersist(){
        if(timestamp == null){
            timestamp = LocalDateTime.now();
        }
        if(status == null){
            status = "PENDING";
        }
    }
}