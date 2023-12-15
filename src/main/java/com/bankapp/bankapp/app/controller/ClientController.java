package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.AccountDto;
import com.bankapp.bankapp.app.dto.ClientDto;
import com.bankapp.bankapp.app.dto.ClientDtoFullUpdate;
import com.bankapp.bankapp.app.entity.Client;
import com.bankapp.bankapp.app.exception.validation.annotation.IDChecker;
import com.bankapp.bankapp.app.service.AccountService;
import com.bankapp.bankapp.app.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Client Controller
 * @author Fam Le Duc Nam
 */
@Validated
@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final AccountService accountService;

 @GetMapping(value = "/get/{id}")
    public ClientDto getClient(@PathVariable("id") @IDChecker String id) {
        return clientService.getClientById(id);
    }

    @RequestMapping(value = "/update/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public ClientDto updateClient(@PathVariable("id") @IDChecker String id, @RequestBody ClientDtoFullUpdate clientDtpFullUpdate) {
        return clientService.updateClient(id, clientDtpFullUpdate);
    }

    @RequestMapping(value = "/delete/{id}", method = {RequestMethod.DELETE, RequestMethod.GET})
    public String deleteClient(@PathVariable("id") @IDChecker String id) {
        return clientService.deleteClient(id);
    }

    @GetMapping(value = "/all-accounts")
    public List<AccountDto> getListAccountByClientId(@RequestParam("id") @IDChecker String id) {
        return accountService.getListAccount(UUID.fromString(id));
    }

}