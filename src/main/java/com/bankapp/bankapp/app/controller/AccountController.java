package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.AccountDto;
import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{id}")
    public Account readAccount(@PathVariable("id") String id) {
        return accountService.getAccountById(id);
    }

    @PostMapping("/create-account")
    public ResponseEntity<Account> createAccount(Account account) {
        Account createdAccount = accountService.createAccount(account);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @PostMapping("/delete-account/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable("id") String id) {
        boolean deleted = accountService.deleteAccount(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update-account/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable("id") String id, Account account){
        Account updatedAccount = accountService.updateAccount(id,account);
        if (updatedAccount!=null){
            return new ResponseEntity<>(account,HttpStatus.GONE);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
