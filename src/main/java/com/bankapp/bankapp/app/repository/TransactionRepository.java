package com.bankapp.bankapp.app.repository;

import com.bankapp.bankapp.app.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
}
