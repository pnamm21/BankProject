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

    private static final UUID accountId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAccountByIdTest() {

        Account mockAccount = new Account();
        mockAccount.setId(accountId);

        // When the accountRepository.findById() method is called with the `accountId` argument,
        // return an Optional containing the `mockAccount` object.
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        // Call the getAccountById() method on the `accountService` object, passing in the `accountId` argument.
        Optional<Account> result = accountService.getAccountById(accountId.toString());

        // Assert that the result is not empty.
        assertTrue(result.isPresent());

        // Assert that the `id` of the Account object returned by the `getAccountById()` method is equal to the `accountId`.
        assertEquals(accountId, result.get().getId());

        // Verify that the `findById()` method on the `accountRepository` object was called once with the `accountId` argument.
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void getAccountByIdNotFoundTest() {

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> {
            accountService.getAccountById(accountId.toString());
        });

        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void getListAccountTest() {

        List<Account> mockAccounts = Arrays.asList(new Account(), new Account());

        when(accountRepository.getListAccount(accountId)).thenReturn(mockAccounts);
        when(accountMapper.ListAccountToListAccountDto(mockAccounts)).thenReturn(Arrays.asList(new AccountDto(), new AccountDto()));

        List<AccountDto> result = accountService.getListAccount(accountId);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(accountRepository, times(1)).getListAccount(accountId);
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

        AccountDtoFullUpdate accountDtoFullUpdate = new AccountDtoFullUpdate();
        accountDtoFullUpdate.setName("Updated Account");
        accountDtoFullUpdate.setType("MAKE_MONEY_ACCOUNT");
        accountDtoFullUpdate.setStatus("ACTIVE");
        accountDtoFullUpdate.setBalance("2000.0");
        accountDtoFullUpdate.setCurrencyCode("EUR");
        accountDtoFullUpdate.setClientId(String.valueOf(UUID.randomUUID()));

        Account originalAccount = new Account();
        originalAccount.setId(accountId);

        Account updatedAccount = new Account();
        updatedAccount.setId(accountId);
        updatedAccount.setName(accountDtoFullUpdate.getName());
        updatedAccount.setType(AccountType.valueOf(accountDtoFullUpdate.getType()));
        updatedAccount.setStatus(AccountStatus.valueOf(accountDtoFullUpdate.getStatus()));
        updatedAccount.setBalance(Double.valueOf(accountDtoFullUpdate.getBalance()));
        updatedAccount.setCurrencyCode(CurrencyCodeType.valueOf(accountDtoFullUpdate.getCurrencyCode()));

        when(accountRepository.existsById(accountId)).thenReturn(true);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(originalAccount));
        when(accountMapper.accountDtoFullToAccount(accountDtoFullUpdate)).thenReturn(updatedAccount);
        when(accountMapper.mergeAccounts(updatedAccount, originalAccount)).thenReturn(updatedAccount);
        when(accountRepository.save(updatedAccount)).thenReturn(updatedAccount);

        Account result = accountService.updateAccount(accountId.toString(), accountDtoFullUpdate);

        assertNotNull(result);
        assertEquals(accountId, result.getId());
        assertEquals(accountDtoFullUpdate.getName(), result.getName());
        assertEquals(accountDtoFullUpdate.getType(), result.getType().toString());
        assertEquals(accountDtoFullUpdate.getStatus(), result.getStatus().toString());
        assertEquals(Double.parseDouble(accountDtoFullUpdate.getBalance()), result.getBalance());
        assertEquals(accountDtoFullUpdate.getCurrencyCode(), result.getCurrencyCode().toString());

        verify(accountRepository, times(1)).existsById(accountId);
        verify(accountRepository, times(1)).findById(accountId);
        verify(accountMapper, times(1)).accountDtoFullToAccount(accountDtoFullUpdate);
        verify(accountMapper, times(1)).mergeAccounts(updatedAccount, originalAccount);
        verify(accountRepository, times(1)).save(updatedAccount);
    }

    @Test
    void updateAccountNotFound() {

        AccountDtoFullUpdate accountDtoFullUpdate = new AccountDtoFullUpdate();

        when(accountRepository.existsById(accountId)).thenReturn(false);

        assertThrows(DataNotFoundException.class, ()->{
           accountService.updateAccount(accountId.toString(), accountDtoFullUpdate);
        });

        verify(accountRepository, times(1)).existsById(accountId);
        verify(accountRepository, never()).findById(accountId);
        verify(accountMapper, never()).accountDtoFullToAccount(accountDtoFullUpdate);
        verify(accountMapper, never()).mergeAccounts(any(), any());
        verify(accountRepository, never()).save(any());
    }

    @Test
    void deleteAccountTest() {

        Account mockAccount = new Account();
        mockAccount.setId(accountId);
        mockAccount.setStatus(AccountStatus.ACTIVE);

        when(accountRepository.existsById(accountId)).thenReturn(true);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

       String result = accountService.deleteAccount(accountId.toString());

       assertEquals("Account has been CLOSED!",result);
       assertEquals(AccountStatus.CLOSED, mockAccount.getStatus());

        verify(accountRepository, times(1)).existsById(accountId);
        verify(accountRepository, times(1)).findById(accountId);
        verify(accountRepository, times(1)).save(mockAccount);
    }

    @Test
    void deleteAccountNotFoundTest() {

        when(accountRepository.existsById(accountId)).thenReturn(false);

        assertThrows(DataNotFoundException.class, ()->{
           accountService.deleteAccount(accountId.toString());
        });

        verify(accountRepository, times(1)).existsById(accountId);
        verify(accountRepository, never()).findById(accountId);
        verify(accountRepository, never()).save(any());
    }

    @Test
    void getAccountByAccountNumberTest() {

        Account mockAccount = new Account();
        mockAccount.setId(accountId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        Account result = accountService.getAccountByAccountNumber(accountId.toString());

        assertNotNull(result);
        assertEquals(accountId, result.getId());

        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void getAccountByAccountNumberNotFound() {

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> {
            accountService.getAccountByAccountNumber(accountId.toString());
        });

        verify(accountRepository, times(1)).findById(accountId);
    }

}