package com.paypal.transaction_service.repository;

import com.paypal.transaction_service.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface transactionRepository extends JpaRepository<Transaction, Long> {
}
