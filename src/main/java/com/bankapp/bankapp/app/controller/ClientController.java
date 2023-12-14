package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.AccountDto;
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
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final AccountService accountService;

 @GetMapping(value = "/get/{id}")
    public ResponseEntity<Client> getClient( @PathVariable("id") @IDChecker String id) {
        return ResponseEntity.ok(clientService.getClientById(id).orElse(null));
    }

    @RequestMapping(value = "/update-client/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public ResponseEntity<Client> updateClient(@PathVariable("id") @IDChecker String id, @RequestBody ClientDtoFullUpdate clientDtpFullUpdate) {
        return ResponseEntity.ofNullable(clientService.updateClient(id, clientDtpFullUpdate));
    }

    @RequestMapping(value = "/delete-client/{id}", method = {RequestMethod.DELETE, RequestMethod.GET})
    public ResponseEntity<String> deleteClient(@PathVariable("id") @IDChecker String id) {
        return ResponseEntity.ok(clientService.deleteClient(id));
    }

    @GetMapping(value = "/all-accounts")
    public ResponseEntity<List<AccountDto>> getListAccountByClientId(@RequestParam("id") @IDChecker String id) {

        List<AccountDto> accountDtos = accountService.getListAccount(UUID.fromString(id));
        return new ResponseEntity<>(accountDtos, HttpStatus.OK);
    }

}