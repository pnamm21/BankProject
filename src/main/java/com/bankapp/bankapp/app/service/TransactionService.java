package com.bankapp.bankapp.app.service;

import com.bankapp.bankapp.app.entity.Transaction;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {
    Transaction updateTransaction(String id,Transaction transaction);
}
