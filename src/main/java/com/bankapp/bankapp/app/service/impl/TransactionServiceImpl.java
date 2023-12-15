package com.bankapp.bankapp.app.service.impl;

import com.bankapp.bankapp.app.dto.TransactionDto;
import com.bankapp.bankapp.app.dto.TransactionDtoFullUpdate;
import com.bankapp.bankapp.app.dto.TransactionDtoTransfer;
import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.entity.Transaction;
import com.bankapp.bankapp.app.entity.enums.TransactionStatus;
import com.bankapp.bankapp.app.entity.enums.TransactionType;
import com.bankapp.bankapp.app.exception.BalanceIsEmptyException;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import com.bankapp.bankapp.app.exception.ExceptionMessage;
import com.bankapp.bankapp.app.exception.InsufficientBalanceException;
import com.bankapp.bankapp.app.mapper.TransactionMapper;
import com.bankapp.bankapp.app.repository.AccountRepository;
import com.bankapp.bankapp.app.repository.TransactionRepository;
import com.bankapp.bankapp.app.service.AccountService;
import com.bankapp.bankapp.app.service.util.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Transaction Service
 * @author ffam5
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    /**
     * @param accountRepository Account Repository
     * @param accountService Account Service
     * @param transactionMapper Transaction Mapper
     * @param transactionRepository Transaction Repository
     */
    public TransactionServiceImpl(TransactionRepository transactionRepository, TransactionMapper transactionMapper, AccountRepository accountRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    /**
     * Find Transaction by ID
     * @param id TransactionID
     * @return TransactionDto or throw DataNotFoundException
     */
    @Override
    public TransactionDto getTransactionById(String id) throws DataNotFoundException {
        return transactionMapper.transactionToTransactionDto(transactionRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND)));
    }

    /**
     * Find List<Transaction> by CreditAccountID
     * @param id CreditAccountID
     * @return List<TransactionDto>
     */
    @Override
    public List<TransactionDto> getListTransactionByCreditAccountId(UUID id) {
        List<Transaction> transactions = transactionRepository.getTransactionsByCreditAccountId(id);
        return transactionMapper.listTransactionToListTransactionDto(transactions);
    }

    /**
     * Find List<Transaction> by DebitAccountID
     * @param id DebitAccountID
     * @return List<TransactionDto>
     */
    @Override
    public List<TransactionDto> getListTransactionByDebitAccountId(UUID id) {
        List<Transaction> transactions = transactionRepository.getTransactionsByDebitAccountId(id);
        return transactionMapper.listTransactionToListTransactionDto(transactions);
    }

    /**
     * Update Transaction
     * @param id TransactionID
     * @param transactionDtoFullUpdate TransactionDtoFullUpdate
     * @return TransactionDto or throw DataNotFoundException
     */
    @Override
    @Transactional
    public TransactionDto updateTransaction(String id, TransactionDtoFullUpdate transactionDtoFullUpdate) {

        UUID stringId = UUID.fromString(id);
        if (transactionRepository.existsById(stringId)) {
            Transaction original = transactionRepository.findById(stringId)
                    .orElseThrow(() -> new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND));

            transactionDtoFullUpdate.setId(id);
            transactionDtoFullUpdate.setDebitAccountId(transactionDtoFullUpdate.getDebitAccountId());
            transactionDtoFullUpdate.setCreditAccountId(transactionDtoFullUpdate.getCreditAccountId());

            Transaction updatedTransaction = transactionMapper.transactionFUllDtoTotransaction(transactionDtoFullUpdate);
            updatedTransaction.setDebitAccount(original.getDebitAccount());
            updatedTransaction.setCreditAccount(original.getCreditAccount());

           transactionRepository.save(updatedTransaction);
            return transactionMapper.transactionToTransactionDto(updatedTransaction);
        } else {
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }

    }

    /**
     * Transfer Money from Account to Account
     * @param accountId AccountID
     * @param transactionDtoTransfer TransactionDtoTransfer
     * @return Approved or throw DataNotFoundException
     */
    @Override
    @Transactional
    public String transfer(UUID accountId, TransactionDtoTransfer transactionDtoTransfer) {

        Account fromAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new DataNotFoundException("Source account not found"));
        Account toAccount = accountService.getAccountByAccountNumber(transactionDtoTransfer.getCreditAccount());

        double amount = Double.parseDouble(transactionDtoTransfer.getAmount());

        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be a positive value");
        }

        if (fromAccount.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance in the source account");
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.valueOf(transactionDtoTransfer.getTransactionType()));
        transaction.setAmount(amount);
        transaction.setDescription(transactionDtoTransfer.getDescription());
        transaction.setStatus(TransactionStatus.valueOf(transactionDtoTransfer.getStatus()));
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setDebitAccount(fromAccount);
        transaction.setCreditAccount(toAccount);

        transactionRepository.save(transaction);
        return "APPROVED";
    }

    /**
     * Delete Transaction
     * @param id TransactionID
     * @return "Transaction has been CANCELED!"
     */
    @Override
    @Transactional
    public String deleteTransaction(String id) {

        UUID stringId = UUID.fromString(id);
        if (transactionRepository.existsById(stringId)) {
            Optional<Transaction> transaction = transactionRepository.findById(stringId);
            Transaction getTransaction = transaction.get();
            getTransaction.setStatus(TransactionStatus.CANCELED);
            transactionRepository.save(getTransaction);
            return "Transaction has been CANCELED!";
        } else {
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }
    }

}