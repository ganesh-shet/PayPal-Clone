package com.paypal.transaction_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.transaction_service.entity.Transaction;
import com.paypal.transaction_service.kafka.KafkaEventProducer;
import com.paypal.transaction_service.repository.transactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class transactionServiceImpl implements transactionService {

    @Autowired
    private transactionRepository transactionRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private KafkaEventProducer kafkaEventProducer;

    @Override
    public Transaction createTransaction(Transaction request) {
        System.out.println("🚀 Entered createTransaction()");

        Long senderId = request.getSenderId();
        Long receiverId = request.getReceiverId();
        Double amount = request.getAmount();

        Transaction transaction = new Transaction();
        transaction.setSenderId(senderId);
        transaction.setReceiverId(receiverId);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setStatus("SUCCESS");

        System.out.println("📥 Incoming Transaction object: " + transaction);

        Transaction saved = transactionRepository.save(transaction);
        System.out.println("💾 Saved Transaction from DB: " + saved);

        try {
                String key = String.valueOf(saved.getId());
                kafkaEventProducer.sendTransactionEvent(key, saved); // send actual object!

                System.out.println("🚀 Kafka message sent");
        } catch (Exception e) {
            System.err.println("❌ Failed to send Kafka event: " + e.getMessage());
            e.printStackTrace();
        }

        return saved;
    }


    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
