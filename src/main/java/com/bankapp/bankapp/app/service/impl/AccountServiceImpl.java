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


/**
 * Account Service
 *
 * @author ffam5
 */
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final AccountMapper accountMapper;

    /**
     * @param accountRepository Account Repository
     * @param clientRepository  Client Repository
     * @param accountMapper     Account Mapper
     */
    public AccountServiceImpl(AccountRepository accountRepository, ClientRepository clientRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.accountMapper = accountMapper;
    }

    /**
     * Find Account by ID
     *
     * @param id AccountID
     * @return AccountDto or throw DataNotFoundException
     */
    @Override
    public AccountDto getAccountById(String id) throws DataNotFoundException {
        return accountMapper.accountToAccountDTO(accountRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND)));
    }

    /**
     * Find List<Account> by ClientID
     *
     * @param id ClientID
     * @return List<AccountDto>
     */
    @Override
    public List<AccountDto> getListAccount(UUID id) {
        List<Account> accounts = accountRepository.getAccountsByClientId(id);
        return accountMapper.ListAccountToListAccountDto(accounts);
    }

    /**
     * Create Account
     *
     * @param accountDtoPost AccountDtoPost
     * @return AccountDto or throw DataNotFoundException
     */
    @Override
    @Transactional
    public AccountDto createAccount(AccountDtoPost accountDtoPost) {

        Account account = accountMapper.accountDtoPostToAccount(accountDtoPost);

        accountRepository.save(account);
        return accountMapper.accountToAccountDTO(account);
    }

    /**
     * Update Account
     *
     * @param id                   AccountID
     * @param accountDtoFullUpdate AccountDtoFullUpdate
     * @return AccountDtoFullUpdate or throw DataNotFoundException
     */
    @Override
    @Transactional
    public AccountDtoFullUpdate updateAccount(String id, AccountDtoFullUpdate accountDtoFullUpdate) {
        UUID stringId = UUID.fromString(id);
        if (accountRepository.existsById(stringId)) {
            accountDtoFullUpdate.setClientId(accountDtoFullUpdate.getClientId());
            accountDtoFullUpdate.setId(id);
            Account account = accountMapper.accountDtoFullToAccount(accountDtoFullUpdate);
            Account original = accountRepository.findById(stringId)
                    .orElseThrow(() -> new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND));
            account.setClient(original.getClient());
            Account updated = accountMapper.mergeAccounts(account, original);
            accountRepository.save(updated);
            return accountMapper.accountToAccountFullDto(updated);
        } else {
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }
    }

    /**
     * Delete Account
     *
     * @param id AccountID
     * @return "Account has been CLOSED!"
     */
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
    public boolean existByPasswordAndAccountName(String password, String email) {
        return accountRepository.findAccountByPasswordAndName(password, email) != null;
    }

    @Override
    public Account findByPasswordAndAccountName(String password, String email) {

        if (accountRepository.findAccountByPasswordAndName(password, email) != null) {
            return accountRepository.findAccountByPasswordAndName(password, email);
        } else {
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }

    }

    @Override
    public boolean existByAccountName(String accountName) {
        return accountRepository.findAccountByName(accountName) != null;
    }

    @Override
    public Account findByAccountName(String accountName) {

        if (accountRepository.findAccountByName(accountName) != null) {
            return accountRepository.findAccountByName(accountName);
        } else {
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }

    }

    @Override
    public AccountDto findAccountDtoByAccountName(String accountName) {
        return accountMapper.accountToAccountDTO(findByAccountName(accountName));
    }

    @Override
    public Account getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findById(UUID.fromString(accountNumber))
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND));
    }

}