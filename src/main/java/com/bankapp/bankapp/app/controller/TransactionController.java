package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.entity.Transaction;
import com.bankapp.bankapp.app.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PutMapping("/update-transaction")
    public Transaction updateTransaction(String id, Transaction transaction) {
        return transactionService.updateTransaction(id, transaction);
    }
}
