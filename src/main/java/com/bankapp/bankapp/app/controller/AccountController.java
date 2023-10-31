package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.AccountDto;
import com.bankapp.bankapp.app.dto.AccountDtoFullUpdate;
import com.bankapp.bankapp.app.dto.AccountDtoPost;
import com.bankapp.bankapp.app.dto.TransactionDtoTransfer;
import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.entity.Transaction;
import com.bankapp.bankapp.app.exception.validation.annotation.IDChecker;
import com.bankapp.bankapp.app.service.AccountService;
import com.bankapp.bankapp.app.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@Validated
@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    @Autowired
    public AccountController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String getById(@IDChecker @PathVariable("id") String id) {
        return "Valid UUID " + id;
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable("id") @IDChecker String id) {
        return ResponseEntity.ok(accountService.getAccountById(id).orElse(null));
    }

    @GetMapping(value = "/all-accounts")
    public ResponseEntity<List<AccountDto>> getListAccountByClientId(@RequestParam("id") @IDChecker String id) {

        List<AccountDto> accountDtos = accountService.getListAccount(UUID.fromString(id));
        return new ResponseEntity<>(accountDtos, HttpStatus.OK);
    }

    @RequestMapping(value = "/client-accounts/new", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<Account> createAccount(@RequestBody @Valid AccountDtoPost accountDtoPost) {
        accountDtoPost.setId(UUID.randomUUID().toString());

        return new ResponseEntity<>(accountService.createAccount(accountDtoPost), OK);
    }

    @RequestMapping(value = "/delete-account/{id}", method = {RequestMethod.DELETE, RequestMethod.GET})
    public ResponseEntity<String> deleteAccount(@PathVariable("id") @IDChecker String id) {
        return ResponseEntity.ok(accountService.deleteAccount(id));
    }

    @RequestMapping(value = "/update-account/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public ResponseEntity<Account> updateAccount(@PathVariable("id") @IDChecker String id, @RequestBody @Valid AccountDtoFullUpdate accountDtoFullUpdate) {
        return ResponseEntity.ofNullable(accountService.updateAccount(id, accountDtoFullUpdate));
    }

    @RequestMapping(value = "/transfer/{id}")
    public ResponseEntity<Transaction> transfer(@PathVariable("id") @IDChecker UUID id, @RequestBody @Valid TransactionDtoTransfer transactionDtoTransfer) {

        Transaction transaction = transactionService.transfer(id, transactionDtoTransfer);
        return new ResponseEntity<>(transaction, BAD_REQUEST);
    }

}