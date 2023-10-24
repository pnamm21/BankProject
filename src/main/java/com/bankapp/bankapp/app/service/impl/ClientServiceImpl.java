package com.bankapp.bankapp.app.service.impl;

import com.bankapp.bankapp.app.entity.Client;
import com.bankapp.bankapp.app.entity.enums.ClientStatus;
import com.bankapp.bankapp.app.repository.ClientRepository;
import com.bankapp.bankapp.app.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public Optional<Client> getClientById(String id) {
        try {
            return clientRepository.findById(UUID.fromString(id));
        } catch (Exception e) {
            throw new NoSuchElementException("Client with this id not found");
        }
    }

    @Override
    @Transactional
    public boolean deleteClient(String id) {
        UUID stringId = UUID.fromString(id);
        if (clientRepository.existsById(stringId)) {
            Optional<Client> client = clientRepository.findById(stringId);
            Client getClient = client.get();
            getClient.setStatus(ClientStatus.CLOSED);
            clientRepository.save(getClient);
            return true;
        }
        return false;
    }
}
