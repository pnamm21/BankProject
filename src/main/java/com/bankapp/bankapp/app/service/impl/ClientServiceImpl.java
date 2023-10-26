package com.bankapp.bankapp.app.service.impl;

import com.bankapp.bankapp.app.entity.Client;
import com.bankapp.bankapp.app.entity.enums.ClientStatus;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import com.bankapp.bankapp.app.exception.ExceptionMessage;
import com.bankapp.bankapp.app.repository.ClientRepository;
import com.bankapp.bankapp.app.service.ClientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Optional<Client> getClientById(String id) throws DataNotFoundException{
        return Optional.ofNullable(clientRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND)));
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