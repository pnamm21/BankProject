package com.bankapp.bankapp.app.service;

import com.bankapp.bankapp.app.entity.Client;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ClientService {
    Optional<Client> getClientById(String id);
    boolean deleteClient(String id);
}
