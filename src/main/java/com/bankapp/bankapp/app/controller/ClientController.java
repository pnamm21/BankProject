package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.entity.Client;
import com.bankapp.bankapp.app.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/{id}")
    public Client getClient(@PathVariable("id") String id) {
        Optional<Client> optionalClient = clientService.getClientById(id);
        if (optionalClient.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
        }
        return optionalClient.get();
    }

    @PostMapping("/delete-client/{id}")
    public boolean deleteClient(@PathVariable("id") String id) {
        return clientService.deleteClient(id);
    }
}
