package com.paypal.transaction_service.service;

import com.paypal.transaction_service.entity.Transaction;

import java.util.List;

public interface transactionService {

    Transaction createTransaction(Transaction transaction);
    public Transaction getTransactionById(Long id);

    public List<Transaction> getTransactionsByUser(Long userId);
}
