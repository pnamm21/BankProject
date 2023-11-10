package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.*;
import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.entity.Card;
import com.bankapp.bankapp.app.entity.Transaction;
import com.bankapp.bankapp.app.exception.validation.annotation.IDChecker;
import com.bankapp.bankapp.app.service.AccountService;
import com.bankapp.bankapp.app.service.TransactionService;
import com.bankapp.bankapp.app.service.util.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final CardService cardService;

    @Autowired
    public AccountController(AccountService accountService, TransactionService transactionService, CardService cardService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.cardService = cardService;
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

    @RequestMapping(value = "/client-accounts/new", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<Account> createAccount(@RequestBody AccountDtoPost accountDtoPost) {
        accountDtoPost.setId(UUID.randomUUID().toString());

        return new ResponseEntity<>(accountService.createAccount(accountDtoPost), OK);
    }

    // check this method
    @RequestMapping("/get-cards")
    public ResponseEntity<List<CardDto>> getCards(@RequestParam("id") @IDChecker String id) {
        List<CardDto> cardDtos = cardService.getListCards(UUID.fromString(id));
        return new ResponseEntity<>(cardDtos, OK);
    }

    @RequestMapping(value = "/delete-account/{id}", method = {RequestMethod.DELETE, RequestMethod.GET})
    public ResponseEntity<String> deleteAccount(@PathVariable("id") @IDChecker String id) {
        return ResponseEntity.ok(accountService.deleteAccount(id));
    }

    @RequestMapping(value = "/update-account/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public ResponseEntity<Account> updateAccount(@PathVariable("id") @IDChecker String id, @RequestBody AccountDtoFullUpdate accountDtoFullUpdate) {
        return ResponseEntity.ofNullable(accountService.updateAccount(id, accountDtoFullUpdate));
    }

    @RequestMapping(value = "/transfer/{id}")
    public ResponseEntity<Transaction> transfer(@PathVariable("id") @IDChecker UUID id, @RequestBody TransactionDtoTransfer transactionDtoTransfer) {

        Transaction transaction = transactionService.transfer(id, transactionDtoTransfer);
        return new ResponseEntity<>(transaction, BAD_REQUEST);
    }

}