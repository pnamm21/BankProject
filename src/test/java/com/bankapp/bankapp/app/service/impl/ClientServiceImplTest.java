package com.bankapp.bankapp.app.service.impl;

import com.bankapp.bankapp.app.dto.ClientDto;
import com.bankapp.bankapp.app.entity.Client;
import com.bankapp.bankapp.app.entity.enums.ClientStatus;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import com.bankapp.bankapp.app.mapper.ClientMapper;
import com.bankapp.bankapp.app.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Array;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;
    @InjectMocks
    private ClientServiceImpl clientService;

    private static final UUID clientId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getClientByIdTest() {

        Client client = new Client();
        client.setId(clientId);
        client.setStatus(ClientStatus.ACTIVE);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        Optional<Client> result = clientService.getClientById(clientId.toString());

        assertTrue(result.isPresent());
        assertEquals(client, result.get());

        verify(clientRepository, times(1)).findById(clientId);
    }

    @Test
    void getClientByIdNotFoundTest() {

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> {
            clientService.getClientById(clientId.toString());
        });

        verify(clientRepository, times(1)).findById(clientId);
    }

    @Test
    void deleteClientTest() {

        Client client = new Client();
        client.setId(clientId);
        client.setStatus(ClientStatus.ACTIVE);

        when(clientRepository.existsById(clientId)).thenReturn(true);
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        String result = clientService.deleteClient(clientId.toString());

        assertEquals("Client has been DELETED!", result);
        assertEquals(ClientStatus.CLOSED, client.getStatus());

        verify(clientRepository, times(1)).existsById(clientId);
        verify(clientRepository, times(1)).findById(clientId);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void deleteClientNotFoundTest() {

        when(clientRepository.existsById(clientId)).thenReturn(false);

        assertThrows(DataNotFoundException.class,()->{
           clientService.deleteClient(clientId.toString());
        });

        verify(clientRepository, times(1)).existsById(clientId);
        verify(clientRepository, never()).findById(clientId);
        verify(clientRepository, never()).save(any());
    }

    @Test
    void getListClientTest(){

        List<Client> mockClients = Arrays.asList(new Client(),new Client());

        when(clientRepository.getListClient(clientId)).thenReturn(mockClients);
        when(clientMapper.listClientToListClientDto(mockClients)).thenReturn(Arrays.asList(new ClientDto(),new ClientDto()));

        List<ClientDto> result = clientService.getListClients(clientId);

        assertNotNull(result);
        assertEquals(2,result.size());

        verify(clientRepository, times(1)).getListClient(clientId);
        verify(clientMapper,times(1)).listClientToListClientDto(mockClients);
    }

}