package com.bankapp.bankapp.app.service;

import com.bankapp.bankapp.app.dto.ClientDto;
import com.bankapp.bankapp.app.dto.ClientDtpFullUpdate;
import com.bankapp.bankapp.app.entity.Client;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface ClientService {
    Optional<Client> getClientById(String id);
    @Transactional
    String deleteClient(String id);

    List<ClientDto> getListClients(@Param("id") UUID id);

    @Transactional
    Client updateClient(String id, ClientDtpFullUpdate clientDtoFullUpdate);
}