package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.ClientDto;
import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.entity.Client;
import com.bankapp.bankapp.app.mapper.ClientMapper;
import com.bankapp.bankapp.app.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final ClientMapper clientMapper;

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<ClientDto> getClient(@PathVariable("id") String id) {
        Optional<Client> optionalClient;
        try {
            optionalClient = clientService.getClientById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
        }
        if (optionalClient.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
        }
        return new ResponseEntity<>(clientMapper.clientToClientDTO(clientService.getClientById(id).orElseThrow()), HttpStatus.OK);
    }

}
