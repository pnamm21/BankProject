package com.bankapp.bankapp.app.service;

import com.bankapp.bankapp.app.dto.TransactionDto;
import com.bankapp.bankapp.app.dto.TransactionDtoFullUpdate;
import com.bankapp.bankapp.app.entity.Transaction;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface TransactionService {
    Optional<Transaction> getTransactionById(String id);
    Transaction updateTransaction(String id, TransactionDtoFullUpdate transactionDtoFullUpdate);
}
