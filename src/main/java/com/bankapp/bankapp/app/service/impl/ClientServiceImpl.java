package com.bankapp.bankapp.app.service.impl;

import com.bankapp.bankapp.app.dto.AccountDtoFullUpdate;
import com.bankapp.bankapp.app.dto.ClientDto;
import com.bankapp.bankapp.app.dto.ClientDtpFullUpdate;
import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.entity.Client;
import com.bankapp.bankapp.app.entity.enums.ClientStatus;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import com.bankapp.bankapp.app.exception.ExceptionMessage;
import com.bankapp.bankapp.app.mapper.ClientMapper;
import com.bankapp.bankapp.app.repository.ClientRepository;
import com.bankapp.bankapp.app.service.ClientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.bankapp.bankapp.app.entity.enums.AccountStatus.CLOSED;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    public Optional<Client> getClientById(String id) throws DataNotFoundException {
        return Optional.ofNullable(clientRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND)));
    }

    @Override
    public List<ClientDto> getListClients(UUID id) {
        List<Client> clients = clientRepository.getClientsByManagerId(id);
        return clientMapper.listClientToListClientDto(clients);
    }

    @Override
    public Client updateClient(String id, ClientDtpFullUpdate clientDtoFullUpdate) {

        UUID stringId = UUID.fromString(id);
        if (clientRepository.existsById(stringId)) {
            clientDtoFullUpdate.setManagerId(clientDtoFullUpdate.getManagerId());
            clientDtoFullUpdate.setId(id);
            Client client = clientMapper.clientDtoFullToClient(clientDtoFullUpdate);
            Client original = clientRepository.findById(stringId)
                    .orElseThrow(() -> new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND));
            client.setManager(original.getManager());
            Client updated = clientMapper.mergeClient(client, original);
            return clientRepository.save(updated);
        } else {
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public String deleteClient(String id) {
        UUID stringId = UUID.fromString(id);
        if (clientRepository.existsById(stringId)) {
            Optional<Client> client = clientRepository.findById(stringId);
            Client getClient = client.get();
            getClient.setStatus(ClientStatus.CLOSED);
            clientRepository.save(getClient);
            return "Client has been DELETED!";
        } else {
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }
    }

}