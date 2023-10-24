package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.AccountDto;
import com.bankapp.bankapp.app.dto.AccountDtoFullUpdate;
import com.bankapp.bankapp.app.dto.AccountDtoPost;
import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import com.bankapp.bankapp.app.exception.ExceptionMessage;
import com.bankapp.bankapp.app.exception.InvalidUUIDException;
import com.bankapp.bankapp.app.service.AccountService;
import com.bankapp.bankapp.app.service.TransactionService;
import com.bankapp.bankapp.app.validation.UUIDValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping(value = "/get/{id}")
    public Optional<ResponseEntity<Account>> getAccount(@PathVariable("id") String id) {

        try {
            Optional<Account> optAccount = accountService.getAccountById(id);
            return optAccount.map(account -> new ResponseEntity<>(account, OK));
        } catch (Exception e) {
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }
    }

    @GetMapping(value = "/all-accounts")
    public ResponseEntity<List<AccountDto>> getListAccountByClientId(@RequestParam("id") String id) {

        if (!UUIDValidator.isValid(id)) {
            throw new InvalidUUIDException(ExceptionMessage.UUID_INVALID);
        }
        List<AccountDto> accountDtos = accountService.getListAccount(UUID.fromString(id));
        return new ResponseEntity<>(accountDtos, HttpStatus.OK);
    }

    @RequestMapping(value = "/client-accounts/new", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<Account> createAccount(@RequestBody AccountDtoPost accountDtoPost) {
        accountDtoPost.setId(UUID.randomUUID().toString());

        return new ResponseEntity<>(accountService.createAccount(accountDtoPost), OK);
    }

    @RequestMapping(value = "/delete-account/{id}", method = {RequestMethod.DELETE, RequestMethod.GET})
    public ResponseEntity<HttpStatus> deleteAccount(@PathVariable("id") String id) {

        if (!UUIDValidator.isValid(id)) {
            throw new InvalidUUIDException(ExceptionMessage.UUID_INVALID);
        }

        return ResponseEntity.status(accountService.deleteAccount(id) ? OK : NOT_FOUND).build();
    }

    @RequestMapping(value = "/update-account/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public ResponseEntity<Account> updateAccount(@PathVariable("id") String id, @RequestBody AccountDtoFullUpdate accountDtoFullUpdate) {

        if (!UUIDValidator.isValid(id)) {
            throw new InvalidUUIDException(ExceptionMessage.UUID_INVALID);
        }

        return ResponseEntity.ofNullable(accountService.updateAccount(id, accountDtoFullUpdate));
    }

    @RequestMapping(value = "/transfer/{id}")
    public ResponseEntity<Transaction> transfer(@PathVariable("id") UUID id, @RequestBody TransactionDtoTransfer transactionDtoTransfer) {

        Transaction transaction = transactionService.transfer(id, transactionDtoTransfer);

        return new ResponseEntity<>(transaction, BAD_REQUEST);
    }

}