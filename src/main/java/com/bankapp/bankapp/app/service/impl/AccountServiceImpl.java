package com.bankapp.bankapp.app.service.impl;

import com.bankapp.bankapp.app.dto.AccountDto;
import com.bankapp.bankapp.app.dto.AccountDtoFullUpdate;
import com.bankapp.bankapp.app.dto.AccountDtoPost;
import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.entity.enums.AccountStatus;
import com.bankapp.bankapp.app.entity.enums.AccountType;
import com.bankapp.bankapp.app.entity.enums.CurrencyCodeType;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import com.bankapp.bankapp.app.exception.ExceptionMessage;
import com.bankapp.bankapp.app.mapper.AccountMapper;
import com.bankapp.bankapp.app.repository.AccountRepository;
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
    private final AccountMapper accountMapper;

    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public Optional<Account> getAccountById(String id) throws DataNotFoundException {
        return Optional.ofNullable(accountRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND)));
    }

    @Override
    public List<AccountDto> getListAccount(UUID id) {
        List<Account> accounts = accountRepository.getListAccount(id);
        return accountMapper.ListAccountToListAccountDto(accounts);
    }

    @Override
    @Transactional
    public Account createAccount(AccountDtoPost accountDtoPost) {

        Account account = accountMapper.accountDtoPostToAccount(accountDtoPost);

        account.setName(accountDtoPost.getName());
        account.setType(AccountType.valueOf(accountDtoPost.getType()));
        account.setStatus(AccountStatus.valueOf(accountDtoPost.getStatus()));
        account.setBalance(Double.valueOf(accountDtoPost.getBalance()));
        account.setCurrencyCode(CurrencyCodeType.valueOf(accountDtoPost.getCurrencyCode()));

        return accountRepository.save(account);
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
                    .orElseThrow(()->new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND));
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