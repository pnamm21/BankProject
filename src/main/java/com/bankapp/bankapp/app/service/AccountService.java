package com.bankapp.bankapp.app.service;


import com.bankapp.bankapp.app.dto.AccountDtoFullUpdate;

import com.bankapp.bankapp.app.entity.Account;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface AccountService {

    Optional<Account> getAccountById(String id);

    List<Account> getListAccount(@Param("id")UUID id);

    void createAccount(Account account);

    Account updateAccount(String id, AccountDtoFullUpdate accountDtoFullUpdate);

    boolean deleteAccount(String id);
}