package com.bankapp.bankapp.app.service.impl;

import com.bankapp.bankapp.app.dto.AccountDto;
import com.bankapp.bankapp.app.dto.AccountDtoFullUpdate;
import com.bankapp.bankapp.app.dto.AccountDtoPost;
import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import com.bankapp.bankapp.app.exception.ExceptionMessage;
import com.bankapp.bankapp.app.mapper.AccountMapper;
import com.bankapp.bankapp.app.repository.AccountRepository;
import com.bankapp.bankapp.app.repository.ClientRepository;
import com.bankapp.bankapp.app.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.bankapp.bankapp.app.entity.enums.AccountStatus.*;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final AccountMapper accountMapper;

    public AccountServiceImpl(AccountRepository accountRepository, ClientRepository clientRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public AccountDto getAccountById(String id) throws DataNotFoundException {
        return accountMapper.accountToAccountDTO(accountRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND)));
    }

    @Override
    public List<AccountDto> getListAccount(UUID id) {
        List<Account> accounts = accountRepository.getAccountsByClientId(id);
        return accountMapper.ListAccountToListAccountDto(accounts);
    }

    @Override
    @Transactional
    public AccountDto createAccount(AccountDtoPost accountDtoPost) {

        Account account = accountMapper.accountDtoPostToAccount(accountDtoPost);

        account.setClient(clientRepository.findById(UUID.fromString(accountDtoPost.getClientId()))
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND)));

        accountRepository.save(account);
        return accountMapper.accountToAccountDTO(account);
    }

    @Override
    @Transactional
    public Account updateAccount(String id, AccountDtoFullUpdate accountDtoFullUpdate) {
        UUID stringId = UUID.fromString(id);
        if (accountRepository.existsById(stringId)) {
            accountDtoFullUpdate.setClientId(accountDtoFullUpdate.getClientId());
            accountDtoFullUpdate.setId(id);
            Account account = accountMapper.accountDtoFullToAccount(accountDtoFullUpdate);
            Account original = accountRepository.findById(stringId)
                    .orElseThrow(() -> new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND));
            account.setClient(original.getClient());
            Account updated = accountMapper.mergeAccounts(account, original);
            return accountRepository.save(updated);
        } else {
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public String deleteAccount(String id) {
        UUID stringId = UUID.fromString(id);
        if (accountRepository.existsById(stringId)) {
            Optional<Account> account = accountRepository.findById(stringId);
            Account getAccount = account.get();
            getAccount.setStatus(CLOSED);
            accountRepository.save(getAccount);
            return "Account has been CLOSED!";
        } else {
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }
    }

    @Override
    public Account getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findById(UUID.fromString(accountNumber))
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND));
    }




}