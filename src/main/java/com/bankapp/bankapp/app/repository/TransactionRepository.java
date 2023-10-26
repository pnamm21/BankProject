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

    @Query("select t from Transaction t where t.creditAccount.id = :id")
    List<Transaction> getListTransactionsByCreditAccountId(@Param("id") UUID id);

    @Query("select t from Transaction t where t.debitAccount.id = :id")
    List<Transaction> getListTransactionsByDebitAccountId(@Param("id") UUID id);

}