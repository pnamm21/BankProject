package com.bankapp.bankapp.app.mapper;

import com.bankapp.bankapp.app.dto.ClientDto;
import com.bankapp.bankapp.app.dto.ClientDtpFullUpdate;
import com.bankapp.bankapp.app.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = UUID.class)
public interface ClientMapper {
    ClientDto clientToClientDTO(Client client);

    Client clientDTOtoClient(ClientDto clientDto);

    Client mergeClient(Client from, @MappingTarget Client to);
    Client clientDtoFullToClient(ClientDtpFullUpdate clientDtpFullUpdate);
    List<ClientDto> listClientToListClientDto(List<Client> clients);

}