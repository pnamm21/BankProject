package com.bankapp.bankapp.app.service.impl;

import com.bankapp.bankapp.app.dto.TransactionDtoFullUpdate;
import com.bankapp.bankapp.app.dto.TransactionDtoTransfer;
import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.entity.Transaction;
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

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository, TransactionMapper transactionMapper, AccountRepository accountRepository, AccountService accountService, AccountMapper accountMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    @Override
    public Optional<Transaction> getTransactionById(String id) throws NoSuchElementException {
        return transactionRepository.findById(UUID.fromString(id));
    }

    @Override
    @Transactional
    public Transaction updateTransaction(String id, TransactionDtoFullUpdate transactionDtoFullUpdate) {
        UUID stringId = UUID.fromString(id);
        if (transactionRepository.existsById(stringId)) {
            transactionDtoFullUpdate.setDebitAccountId(null);
            transactionDtoFullUpdate.setCreditAccountId(null);
            transactionDtoFullUpdate.setId(id);
            Transaction transaction = transactionMapper.transactionFUllDtoTotransaction(transactionDtoFullUpdate);
            Transaction original = transactionRepository.findById(stringId).orElseThrow();
            transaction.setCreditAccount(original.getCreditAccount());
            transaction.setDebitAccount(original.getDebitAccount());
            Transaction updated = transactionMapper.mergeTransaction(transaction, original);
            return transactionRepository.save(updated);
        }
        return null;
    }

    @Override
    @Transactional
    public Transaction transfer(UUID accountId, TransactionDtoTransfer transactionDtoTransfer) {

        Account fromAccount = accountRepository.findById(accountId).orElseThrow();
        Account toAccount = accountService.getAccountByAccountNumber(transactionDtoTransfer.getCreditAccount());

        if (toAccount == null){
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }

        double amount = Double.parseDouble(transactionDtoTransfer.getAmount());

        if (fromAccount.getBalance() >= amount) {

            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);

            Transaction transaction = new Transaction();
            transaction.setDebitAccount(fromAccount);
            transaction.setCreditAccount(toAccount);
            transaction.setAmount(amount);

            return transactionRepository.save(transaction);
        }else{
            throw new BalanceIsEmptyException(ExceptionMessage.BALANCE_IS_EMPTY);
        }
    }

}