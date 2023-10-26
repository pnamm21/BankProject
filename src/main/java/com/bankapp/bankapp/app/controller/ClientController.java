package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.entity.Client;
import com.bankapp.bankapp.app.exception.ExceptionMessage;
import com.bankapp.bankapp.app.exception.InvalidUUIDException;
import com.bankapp.bankapp.app.exception.validation.annotation.IDChecker;
import com.bankapp.bankapp.app.mapper.ClientMapper;
import com.bankapp.bankapp.app.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Validated
@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<Client> getClient( @PathVariable("id") @IDChecker String id) {
        return ResponseEntity.ok(clientService.getClientById(id).orElse(null));
    }

    @RequestMapping(value = "/delete-client/{id}", method = {RequestMethod.DELETE, RequestMethod.GET})
    public ResponseEntity<HttpStatus> deleteClient(@PathVariable("id") @IDChecker String id) {
        return ResponseEntity.status(clientService.deleteClient(id) ? OK : NOT_FOUND).build();
    }

}