package com.paypal.reward_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.reward_service.entity.Reward;
import com.paypal.reward_service.entity.Transaction;
import com.paypal.reward_service.repository.rewardRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
@Data
public class rewardConsumer {
    private rewardRepository rewardRepository;
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "txn-initiated", groupId = "reward-group")
    public void consumerTransaction(Transaction transaction) {
        try{
            if(rewardRepository.existsByTransactionId(transaction.getId())){
                System.out.println("Reward already exists for transaction ID: " + transaction.getId());
                return; //Skip processing if reward already exists
            }
            Reward reward = new Reward();
            reward.setUserId(transaction.getSenderId());
            reward.setPoints(transaction.getAmount() * 100);
            reward.setSentAt(LocalDateTime.now());
            reward.setTransactionId(transaction.getId());

            rewardRepository.save(reward);
            System.out.println("✅ Processed and saved reward for transaction: ");
    }catch (Exception e){
            System.err.println("❌ Failed to process consumed message: " + e.getMessage());
            e.printStackTrace();
        }

    }

}
