package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.*;
import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.entity.Transaction;
import com.bankapp.bankapp.app.exception.validation.annotation.IDChecker;
import com.bankapp.bankapp.app.service.AccountService;
import com.bankapp.bankapp.app.service.util.TransactionService;
import com.bankapp.bankapp.app.service.util.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

/**
 * Account Controller
 * @author Fam Le Duc Nam
 */
@Validated
@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final CardService cardService;

    @Autowired
    public AccountController(AccountService accountService,
                             TransactionService transactionService, CardService cardService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.cardService = cardService;
    }

    @GetMapping(value = "/get/{id}")
    public AccountDto getAccount(@IDChecker @PathVariable("id")  String id) {
        return accountService.getAccountById(id);
    }

    @RequestMapping(value = "/new", method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDtoPost accountDtoPost) {
        return new ResponseEntity<>(accountService.createAccount(accountDtoPost), OK);
    }

    @RequestMapping("/cards/{id}")
    public List<CardDto> getCards(@IDChecker @PathVariable("id")  String id) {
        return cardService.getListCards(UUID.fromString(id));
    }

    @RequestMapping(value = "/delete/{id}", method = {RequestMethod.DELETE, RequestMethod.GET})
    public String deleteAccount(@IDChecker @PathVariable("id")  String id) {
        return accountService.deleteAccount(id);
    }

    @RequestMapping(value = "/update/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public AccountDtoFullUpdate updateAccount( @IDChecker @PathVariable("id")  String id,
                                                  @RequestBody AccountDtoFullUpdate accountDtoFullUpdate) {
        return accountService.updateAccount(id, accountDtoFullUpdate);
    }

    @RequestMapping(value = "/transfer/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String transfer(@IDChecker @PathVariable("id")  UUID id,
                                                @RequestBody TransactionDtoTransfer transactionDtoTransfer) {
        return transactionService.transfer(id, transactionDtoTransfer);
    }

}