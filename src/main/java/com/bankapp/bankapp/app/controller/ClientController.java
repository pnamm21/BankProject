package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.entity.Client;
import com.bankapp.bankapp.app.exception.ExceptionMessage;
import com.bankapp.bankapp.app.exception.InvalidUUIDException;
import com.bankapp.bankapp.app.mapper.ClientMapper;
import com.bankapp.bankapp.app.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping(value = "/get/{id}")
    public Optional<ResponseEntity<Client>> getClient(@PathVariable("id") String id) {

        try {
            Optional<Client> optionalClient = clientService.getClientById(id);
            return optionalClient.map(client -> new ResponseEntity<>(client,HttpStatus.OK));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
        }

    }

    @RequestMapping(value = "/delete-client/{id}", method = {RequestMethod.DELETE, RequestMethod.GET})
    public ResponseEntity<HttpStatus> deleteClient(@PathVariable("id") String id) {

        if (!com.bankapp.bankapp.app.validation.UUIDValidator.isValid(id)) {
            throw new InvalidUUIDException(ExceptionMessage.UUID_INVALID);
        }

        return ResponseEntity.status(clientService.deleteClient(id) ? OK : NOT_FOUND).build();
    }

}