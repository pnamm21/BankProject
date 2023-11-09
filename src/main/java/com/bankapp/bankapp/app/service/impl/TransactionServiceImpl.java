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
import com.bankapp.bankapp.app.mapper.AccountMapper;
import com.bankapp.bankapp.app.mapper.TransactionMapper;
import com.bankapp.bankapp.app.repository.AccountRepository;
import com.bankapp.bankapp.app.repository.TransactionRepository;
import com.bankapp.bankapp.app.service.AccountService;
import com.bankapp.bankapp.app.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, TransactionMapper transactionMapper, AccountRepository accountRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    @Override
    public Optional<Transaction> getTransactionById(String id) throws DataNotFoundException {
        return Optional.ofNullable(transactionRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND)));
    }

    @Override
    public List<TransactionDto> getListTransactionByCreditAccountId(UUID id) {
        List<Transaction> transactions = transactionRepository.getListTransactionsByCreditAccountId(id);
        return transactionMapper.listTransactionToListTransactionDto(transactions);
    }

    @Override
    public List<TransactionDto> getListTransactionByDebitAccountId(UUID id) {
        List<Transaction> transactions = transactionRepository.getListTransactionsByDebitAccountId(id);
        return transactionMapper.listTransactionToListTransactionDto(transactions);
    }

    @Override
    @Transactional
    public Transaction updateTransaction(String id, TransactionDtoFullUpdate transactionDtoFullUpdate) {
        UUID stringId = UUID.fromString(id);
        if (transactionRepository.existsById(stringId)) {
            transactionDtoFullUpdate.setId(id);
            transactionDtoFullUpdate.setDebitAccountId(transactionDtoFullUpdate.getDebitAccountId());
            transactionDtoFullUpdate.setCreditAccountId(transactionDtoFullUpdate.getCreditAccountId());
            Transaction transaction = transactionMapper.transactionFUllDtoTotransaction(transactionDtoFullUpdate);
            Transaction original = transactionRepository.findById(stringId)
                    .orElseThrow(() -> new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND));
            transaction.setDebitAccount(original.getDebitAccount());
            transaction.setCreditAccount(original.getCreditAccount());
            Transaction updated = transactionMapper.mergeTransaction(transaction, original);
            return transactionRepository.save(updated);
        } else {
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public Transaction transfer(UUID accountId, TransactionDtoTransfer transactionDtoTransfer) {

        Account fromAccount = accountRepository.findById(accountId).orElseThrow();
        Account toAccount = accountService.getAccountByAccountNumber(transactionDtoTransfer.getCreditAccount());

        double amount = Double.parseDouble(transactionDtoTransfer.getAmount());

        if (fromAccount.getBalance() >= amount) {

            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);

            Transaction transaction = new Transaction();
            transaction.setDebitAccount(fromAccount);
            transaction.setCreditAccount(toAccount);
            transaction.setTransactionType(TransactionType.valueOf(transactionDtoTransfer.getTransactionType()));
            transaction.setAmount(amount);
            transaction.setDescription(transactionDtoTransfer.getDescription());
            transaction.setStatus(TransactionStatus.valueOf(transactionDtoTransfer.getStatus()));
            transaction.setCreatedAt(LocalDateTime.now());

            return transactionRepository.save(transaction);
        } else {
            throw new BalanceIsEmptyException(ExceptionMessage.BALANCE_IS_EMPTY);
        }
    }

    @Override
    @Transactional
    public String deleteTransaction(String id) {

        UUID stringId = UUID.fromString(id);
        if (transactionRepository.existsById(stringId)) {
            Optional<Transaction> transaction = transactionRepository.findById(stringId);
            Transaction getTransaction = transaction.get();
            getTransaction.setStatus(TransactionStatus.CANCELED);
            transactionRepository.save(getTransaction);
            return "Transaction has been CANCELED";
        } else {
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }
    }

}