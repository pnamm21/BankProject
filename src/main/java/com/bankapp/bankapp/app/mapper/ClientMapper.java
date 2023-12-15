package com.bankapp.bankapp.app.mapper;

import com.bankapp.bankapp.app.dto.ClientDto;
import com.bankapp.bankapp.app.dto.ClientDtoFullUpdate;
import com.bankapp.bankapp.app.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Client Mapper
 * @author Fam Le Duc Nam
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ClientMapper {
    ClientDto clientToClientDTO(Client client);

    Client clientDTOtoClient(ClientDto clientDto);

    Client mergeClient(Client from, @MappingTarget Client to);
    Client clientDtoFullToClient(ClientDtoFullUpdate clientDtpFullUpdate);
    List<ClientDto> listClientToListClientDto(List<Client> clients);

}