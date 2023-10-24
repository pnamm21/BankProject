package com.bankapp.bankapp.app.service;


import com.bankapp.bankapp.app.dto.AccountDto;
import com.bankapp.bankapp.app.dto.AccountDtoFullUpdate;

import com.bankapp.bankapp.app.dto.AccountDtoPost;
import com.bankapp.bankapp.app.dto.TransactionDtoTransfer;
import com.bankapp.bankapp.app.entity.Account;

import com.bankapp.bankapp.app.entity.Transaction;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface AccountService {

    Optional<Account> getAccountById(String id);

    List<AccountDto> getListAccount(@Param("id") UUID id);

    @Transactional
    Account  createAccount(AccountDtoPost accountDtoPost);

    @Transactional
    Account updateAccount(String id, AccountDtoFullUpdate accountDtoFullUpdate);

    @Transactional
    boolean deleteAccount(String id);

    Account getAccountByAccountNumber(String accountNumber);

}