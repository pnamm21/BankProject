package com.bankapp.bankapp.app.service.impl;

import com.bankapp.bankapp.app.dto.AccountDto;
import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.mapper.AccountMapper;
import com.bankapp.bankapp.app.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    private static final UUID id = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAccountById() {

        Account account = new Account();
        account.setId(id);
        when(accountRepository.findById(id)).thenReturn(Optional.of(account));

        Optional<Account> result = accountService.getAccountById(id.toString());
        assertTrue(result.isPresent());
        assertEquals(id,result.get().getId());

    }

//    @Test
//    void get

    @Test
    void getListAccount() {



    }

    @Test
    void createAccount() {
    }

    @Test
    void updateAccount() {
    }

    @Test
    void deleteAccount() {
    }
}