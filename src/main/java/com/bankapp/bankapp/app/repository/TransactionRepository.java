package com.bankapp.bankapp.app.repository;

import com.bankapp.bankapp.app.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> getTransactionsByCreditAccountId(@Param("id") UUID id);

    List<Transaction> getTransactionsByDebitAccountId(@Param("id") UUID id);

}