package com.bankapp.bankapp.app.service;

import com.bankapp.bankapp.app.entity.Account;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {

    Account getAccountById(String id);

    Account createAccount(Account account);

    Account updateAccount(String id, Account updatedAccount);

    boolean deleteAccount(String id);
}
