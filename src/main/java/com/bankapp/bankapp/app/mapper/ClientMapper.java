package com.bankapp.bankapp.app.mapper;

import com.bankapp.bankapp.app.dto.ClientDto;
import com.bankapp.bankapp.app.entity.Client;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = UUID.class)
public interface ClientMapper {
    ClientDto clientToClientDTO(Client client);

    Client clientDTOtoClient(ClientDto clientDto);

    List<ClientDto> listClientToListClientDto(List<Client> clients);

}
