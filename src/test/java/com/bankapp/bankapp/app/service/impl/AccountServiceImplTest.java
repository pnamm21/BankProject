package com.bankapp.bankapp.app.service.impl;

import com.bankapp.bankapp.app.dto.AccountDto;
import com.bankapp.bankapp.app.dto.AccountDtoFullUpdate;
import com.bankapp.bankapp.app.dto.AccountDtoPost;
import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.entity.Client;
import com.bankapp.bankapp.app.entity.enums.AccountStatus;
import com.bankapp.bankapp.app.entity.enums.AccountType;
import com.bankapp.bankapp.app.entity.enums.CurrencyCodeType;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import com.bankapp.bankapp.app.mapper.AccountMapper;
import com.bankapp.bankapp.app.repository.AccountRepository;
import com.bankapp.bankapp.app.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

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
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        AccountDto mockAccountDto = new AccountDto();
        when(accountMapper.accountToAccountDTO(mockAccount)).thenReturn(mockAccountDto);

        AccountDto result = accountService.getAccountById(accountId.toString());

        assertNotNull(result);
        assertEquals(mockAccountDto, result);

        verify(accountRepository).findById(accountId);
        verify(accountMapper).accountToAccountDTO(mockAccount);
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

        when(accountRepository.getAccountsByClientId(accountId)).thenReturn(mockAccounts);
        when(accountMapper.ListAccountToListAccountDto(mockAccounts)).thenReturn(Arrays.asList(new AccountDto(), new AccountDto()));

        List<AccountDto> result = accountService.getListAccount(accountId);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(accountRepository, times(1)).getAccountsByClientId(accountId);
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
        when(accountMapper.accountDtoPostToAccount(accountDtoPost)).thenReturn(mockAccount);
        when(accountMapper.accountToAccountDTO(mockAccount)).thenReturn(new AccountDto());

        AccountDto result = accountService.createAccount(accountDtoPost);

        assertNotNull(result);

        verify(accountMapper).accountDtoPostToAccount(accountDtoPost);
        verify(accountMapper).accountToAccountDTO(mockAccount);
        verify(accountRepository).save(mockAccount);
    }

    @Test
    void updateAccountTest() {

        AccountDtoFullUpdate fullUpdate = new AccountDtoFullUpdate();
        fullUpdate.setName("Updated Account");
        fullUpdate.setType("MAKE_MONEY_ACCOUNT");
        fullUpdate.setStatus("ACTIVE");
        fullUpdate.setBalance("2000.0");
        fullUpdate.setCurrencyCode("EUR");
        fullUpdate.setClientId(String.valueOf(UUID.randomUUID()));

        Account originalAccount = new Account();
        when(accountRepository.existsById(accountId)).thenReturn(true);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(originalAccount));

        AccountDtoFullUpdate accountDtoFullUpdate = new AccountDtoFullUpdate();
        Account updatedAccount = new Account();
        when(accountMapper.accountDtoFullToAccount(accountDtoFullUpdate)).thenReturn(updatedAccount);
        when(accountMapper.mergeAccounts(updatedAccount, originalAccount)).thenReturn(updatedAccount);
        when(accountMapper.accountToAccountFullDto(updatedAccount)).thenReturn(new AccountDtoFullUpdate());

        AccountDtoFullUpdate result = accountService.updateAccount(accountId.toString(), accountDtoFullUpdate);

        assertNotNull(result);

        verify(accountRepository).existsById(accountId);
        verify(accountRepository).findById(accountId);
        verify(accountRepository).save(updatedAccount);

        verify(accountMapper).accountDtoFullToAccount(accountDtoFullUpdate);
        verify(accountMapper).mergeAccounts(updatedAccount, originalAccount);
        verify(accountMapper).accountToAccountFullDto(updatedAccount);

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