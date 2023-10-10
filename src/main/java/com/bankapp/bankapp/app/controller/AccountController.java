package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.AccountDto;
import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.mapper.AccountMapper;
import com.bankapp.bankapp.app.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping(value = "/{id}")
    public Account getAccountById(@PathVariable("id") String id) {
        Optional<Account> optionalAccount = accountService.getAccountById(id);
        if (optionalAccount.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
        }
        return optionalAccount.get();
    }

    @GetMapping(value = "/get/{id}")
    public AccountDto getAccount(@PathVariable("id") String id) {
        Optional<Account> optAccount;
        try {
            optAccount = accountService.getAccountById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
        }
        if (optAccount.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
        }
        return accountMapper.accountToAccountDTO(optAccount.get());
    }

    @GetMapping(value = "/get-accounts")
    public List<AccountDto> getListAccountByClientId(@RequestParam("id") String id) {
        return accountMapper.ListAccountToListAccountDto(accountService.getListAccount(UUID.fromString(id)));
    }

    @PostMapping("/client-accounts/new")
    public Account createAccount(@PathVariable("id") String id, @RequestBody AccountDto accountDto) {
        return accountService.createAccount(id, accountDto);
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