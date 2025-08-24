package com.paypal.transaction_service.controller;

import com.paypal.transaction_service.entity.Transaction;
import com.paypal.transaction_service.service.transactionService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Data
@AllArgsConstructor
@RequestMapping("/api/transactions/")
public class transactionController {

    @Autowired
    private transactionService transactionService;

    @PostMapping("/createtrans")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction newTransaction = transactionService.createTransaction(transaction);
        return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
    }

    @GetMapping("/getTrans")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return new ResponseEntity<>(transactionService.getAllTransactions(), HttpStatus.OK);
    }
}
