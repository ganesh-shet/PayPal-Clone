package com.paypal.reward_service.repository;

import com.paypal.reward_service.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface rewardRepository extends JpaRepository<Reward, Long> {

    List<Reward> findByUserId(Long userId);
    boolean existsByTransactionId(Long transactionId);
}
