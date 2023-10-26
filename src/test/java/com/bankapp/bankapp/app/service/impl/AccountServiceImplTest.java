package com.bankapp.bankapp.app.service.impl;

import com.bankapp.bankapp.app.dto.AccountDto;
import com.bankapp.bankapp.app.dto.AccountDtoFullUpdate;
import com.bankapp.bankapp.app.dto.AccountDtoPost;
import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.entity.enums.AccountStatus;
import com.bankapp.bankapp.app.entity.enums.AccountType;
import com.bankapp.bankapp.app.entity.enums.CurrencyCodeType;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import com.bankapp.bankapp.app.mapper.AccountMapper;
import com.bankapp.bankapp.app.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


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
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAccountByIdTest() {

        UUID accountId = UUID.randomUUID();
        Account mockAccount = new Account();
        mockAccount.setId(accountId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        Optional<Account> result = accountService.getAccountById(accountId.toString());

        assertTrue(result.isPresent());
        assertEquals(accountId, result.get().getId());

        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void getAccountByIdNotFoundTest() {
        UUID accountId = UUID.randomUUID();

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> {
            accountService.getAccountById(accountId.toString());
        });

        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void getListAccountTest() {

        UUID clientId = UUID.randomUUID();
        List<Account> mockAccounts = Arrays.asList(new Account(), new Account());

        when(accountRepository.getListAccount(clientId)).thenReturn(mockAccounts);
        when(accountMapper.ListAccountToListAccountDto(mockAccounts)).thenReturn(Arrays.asList(new AccountDto(), new AccountDto()));

        List<AccountDto> result = accountService.getListAccount(clientId);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(accountRepository, times(1)).getListAccount(clientId);
        verify(accountMapper, times(1)).ListAccountToListAccountDto(mockAccounts);
    }

    @Test
    void createAccountTest() {

        AccountDtoPost accountDtoPost = new AccountDtoPost();
        accountDtoPost.setName("Test Account");
        accountDtoPost.setType("SAVING_ACCOUNT");
        accountDtoPost.setStatus("ACTIVE");
        accountDtoPost.setBalance("1000.0");
        accountDtoPost.setCurrencyCode("USD");
        accountDtoPost.setClientId(String.valueOf(UUID.randomUUID()));

        Account mockAccount = new Account();
        mockAccount.setId(UUID.randomUUID());

        when(accountMapper.accountDtoPostToAccount(accountDtoPost)).thenReturn(mockAccount);
        when(accountRepository.save(mockAccount)).thenReturn(mockAccount);

        Account result = accountService.createAccount(accountDtoPost);

        assertNotNull(result);
        assertNotNull(result.getId());

        verify(accountMapper, times(1)).accountDtoPostToAccount(accountDtoPost);
        verify(accountRepository, times(1)).save(mockAccount);
    }

    @Test
    void updateAccountTest() {

        UUID accountID = UUID.randomUUID();
        AccountDtoFullUpdate accountDtoFullUpdate = new AccountDtoFullUpdate();
        accountDtoFullUpdate.setName("Updated Account");
        accountDtoFullUpdate.setType("MAKE_MONEY_ACCOUNT");
        accountDtoFullUpdate.setStatus("ACTIVE");
        accountDtoFullUpdate.setBalance("2000.0");
        accountDtoFullUpdate.setCurrencyCode("EUR");
        accountDtoFullUpdate.setClientId(String.valueOf(UUID.randomUUID()));

        Account originalAccount = new Account();
        originalAccount.setId(accountID);

        Account updatedAccount = new Account();
        updatedAccount.setId(accountID);
        updatedAccount.setName(accountDtoFullUpdate.getName());
        updatedAccount.setType(AccountType.valueOf(accountDtoFullUpdate.getType()));
        updatedAccount.setStatus(AccountStatus.valueOf(accountDtoFullUpdate.getStatus()));
        updatedAccount.setBalance(Double.valueOf(accountDtoFullUpdate.getBalance()));
        updatedAccount.setCurrencyCode(CurrencyCodeType.valueOf(accountDtoFullUpdate.getCurrencyCode()));

        when(accountRepository.existsById(accountID)).thenReturn(true);
        when(accountRepository.findById(accountID)).thenReturn(Optional.of(originalAccount));
        when(accountMapper.accountDtoFullToAccount(accountDtoFullUpdate)).thenReturn(updatedAccount);
        when(accountMapper.mergeAccounts(updatedAccount, originalAccount)).thenReturn(updatedAccount);
        when(accountRepository.save(updatedAccount)).thenReturn(updatedAccount);

        Account result = accountService.updateAccount(accountID.toString(), accountDtoFullUpdate);

        assertNotNull(result);
        assertEquals(accountID, result.getId());
        assertEquals(accountDtoFullUpdate.getName(), result.getName());
        assertEquals(accountDtoFullUpdate.getType(), result.getType().toString());
        assertEquals(accountDtoFullUpdate.getStatus(), result.getStatus().toString());
        assertEquals(Double.parseDouble(accountDtoFullUpdate.getBalance()), result.getBalance());
        assertEquals(accountDtoFullUpdate.getCurrencyCode(), result.getCurrencyCode().toString());

        verify(accountRepository, times(1)).existsById(accountID);
        verify(accountRepository, times(1)).findById(accountID);
        verify(accountMapper, times(1)).accountDtoFullToAccount(accountDtoFullUpdate);
        verify(accountMapper, times(1)).mergeAccounts(updatedAccount, originalAccount);
        verify(accountRepository, times(1)).save(updatedAccount);
    }

    @Test
    void updateAccountNotFound() {

        UUID accountId = UUID.randomUUID();
        AccountDtoFullUpdate accountDtoFullUpdate = new AccountDtoFullUpdate();

        when(accountRepository.existsById(accountId)).thenReturn(false);

        assertNull(accountService.updateAccount(accountId.toString(), accountDtoFullUpdate));

        verify(accountRepository, times(1)).existsById(accountId);
        verify(accountRepository, never()).findById(accountId);
        verify(accountMapper, never()).accountDtoFullToAccount(accountDtoFullUpdate);
        verify(accountMapper, never()).mergeAccounts(any(), any());
        verify(accountRepository, never()).save(any());
    }

    @Test
    void deleteAccountTest() {

        UUID accountId = UUID.randomUUID();
        Account mockAccount = new Account();
        mockAccount.setId(accountId);

        when(accountRepository.existsById(accountId)).thenReturn(true);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));
        when(accountRepository.save(mockAccount)).thenReturn(mockAccount);

        assertTrue(accountService.deleteAccount(accountId.toString()));

        verify(accountRepository, times(1)).existsById(accountId);
        verify(accountRepository, times(1)).findById(accountId);
        verify(accountRepository, times(1)).save(mockAccount);
    }

    @Test
    void deleteAccountNotFoundTest() {

        UUID accountId = UUID.randomUUID();

        when(accountRepository.existsById(accountId)).thenReturn(false);

        assertFalse(accountService.deleteAccount(accountId.toString()));

        verify(accountRepository, times(1)).existsById(accountId);
        verify(accountRepository, never()).findById(accountId);
        verify(accountRepository, never()).save(any());
    }

    @Test
    void getAccountByAccountNumberTest() {

        UUID accountNumber = UUID.randomUUID();
        Account mockAccount = new Account();
        mockAccount.setId(accountNumber);

        when(accountRepository.findById(accountNumber)).thenReturn(Optional.of(mockAccount));

        Account result = accountService.getAccountByAccountNumber(accountNumber.toString());

        assertNotNull(result);
        assertEquals(accountNumber, result.getId());

        verify(accountRepository, times(1)).findById(accountNumber);
    }

    @Test
    void getAccountByAccountNumberNotFound() {

        UUID accountNumber = UUID.randomUUID();

        when(accountRepository.findById(accountNumber)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> {
            accountService.getAccountByAccountNumber(accountNumber.toString());
        });

        verify(accountRepository, times(1)).findById(accountNumber);
    }

}