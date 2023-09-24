package com.bankapp.bankapp.app.impl;

import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.repository.AccountRepository;
import com.bankapp.bankapp.app.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    @Override
    public Account getAccountById(String id) {
        return accountRepository.getById(UUID.fromString(id));
    }

    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account updateAccount(String id, Account updatedAccount) {
        UUID stringId = UUID.fromString(id);
        if (accountRepository.existsById(stringId)){
            updatedAccount.setId(stringId);
            return accountRepository.save(updatedAccount);
        }
        return null;
    }

    @Override
    public boolean deleteAccount(String id) {
        UUID stringId = UUID.fromString(id);
        if (accountRepository.existsById(stringId)){
            accountRepository.deleteById(stringId);
            return true;
        }
        return false;
    }
}
