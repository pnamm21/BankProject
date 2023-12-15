package com.bankapp.bankapp.app.service;

import com.bankapp.bankapp.app.dto.ClientDto;
import com.bankapp.bankapp.app.dto.ClientDtoFullUpdate;
import com.bankapp.bankapp.app.entity.Client;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Client Service
 * @author Fam Le Duc Nam
 */
@Service
public interface ClientService {
    ClientDto getClientById(String id);
    @Transactional
    String deleteClient(String id);

    List<ClientDto> getListClients(@Param("id") UUID id);

    @Transactional
    ClientDto updateClient(String id, ClientDtoFullUpdate clientDtoFullUpdate);
}