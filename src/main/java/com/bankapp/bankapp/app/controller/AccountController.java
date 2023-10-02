package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping(value = "/{id}")
    public Account getAccount(@PathVariable("id") String id) {
        Optional<Account> optionalAccount = accountService.getAccountById(id);
        if (optionalAccount.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
        }
        return optionalAccount.get();
    }

    @PostMapping("/create-account")
    public Account createAccount(@RequestBody Account account) {
        return accountService.createAccount(account);
    }

    @PostMapping("/delete-account/{id}")
    public boolean deleteAccount(@PathVariable("id") String id) {
        return accountService.deleteAccount(id);
    }

    @PutMapping("/update-account/{id}")
    public Account updateAccount(@PathVariable("id") String id, Account account) {
        return accountService.updateAccount(id, account);
    }


}