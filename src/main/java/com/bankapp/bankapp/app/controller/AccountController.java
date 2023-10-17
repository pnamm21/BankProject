package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.AccountDto;
import com.bankapp.bankapp.app.dto.AccountDtoFullUpdate;
import com.bankapp.bankapp.app.dto.AccountDtoPost;
import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import com.bankapp.bankapp.app.exception.ExceptionMessage;
import com.bankapp.bankapp.app.exception.InvalidUUIDException;
import com.bankapp.bankapp.app.mapper.AccountMapper;
import com.bankapp.bankapp.app.service.AccountService;
import com.bankapp.bankapp.app.validation.UUIDValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable("id") String id) {
        Optional<Account> optAccount;
        try {
            optAccount = accountService.getAccountById(id);
        } catch (Exception e) {
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }
        if (optAccount.isEmpty()) {
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }
        return new ResponseEntity<>(accountMapper.accountToAccountDTO(optAccount.get()), HttpStatus.OK);
    }

    @GetMapping(value = "/all-accounts")
    public ResponseEntity<List<AccountDto>> getListAccountByClientId(@RequestParam("id") String id) {

        if (!UUIDValidator.isValid(id)) {
            throw new InvalidUUIDException(ExceptionMessage.UUID_INVALID);
        }

        return new ResponseEntity<>(accountMapper.ListAccountToListAccountDto(accountService.getListAccount(UUID.fromString(id))), HttpStatus.OK);
    }

    @RequestMapping(value = "/client-accounts/new", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDtoPost accountDtoPost) {
        accountDtoPost.setId(UUID.randomUUID().toString());
        Account account = accountMapper.accountDtoPostToAccount(accountDtoPost);
        accountService.createAccount(account);
        return new ResponseEntity<>(accountMapper.accountToAccountDTO(account), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete-account/{id}", method = {RequestMethod.DELETE, RequestMethod.GET})
    public ResponseEntity<HttpStatus> deleteAccount(@PathVariable("id") String id) {
        return ResponseEntity.status(accountService.deleteAccount(id) ? HttpStatus.OK : HttpStatus.NOT_FOUND).build();
    }

    @RequestMapping(value = "/update-account/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public ResponseEntity<Account> updateAccount(@PathVariable("id") String id, @RequestBody AccountDtoFullUpdate accountDtoFullUpdate) {
        return ResponseEntity.ofNullable(accountService.updateAccount(id, accountDtoFullUpdate));
    }

}