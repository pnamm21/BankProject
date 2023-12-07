package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.AccountDto;
import com.bankapp.bankapp.app.dto.AccountDtoFullUpdate;
import com.bankapp.bankapp.app.dto.CardDto;
import com.bankapp.bankapp.app.dto.ClientDtpFullUpdate;
import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.entity.Client;
import com.bankapp.bankapp.app.service.AccountService;
import com.bankapp.bankapp.app.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @MockBean
    private AccountService accountService;

    private static final UUID clientId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getClientTest() {

        Client mockClient = new Client();
        mockClient.setId(clientId);
        Mockito.when(clientService.getClientById(clientId.toString())).thenReturn(Optional.of(mockClient));

        String responseContent = null;

        try {
            responseContent = mockMvc.perform(MockMvcRequestBuilders.get("/client/get/{id}", clientId))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("Response Content: " + responseContent);

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/client/get/{id}",clientId))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(clientId.toString()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void updateClientTest() {

        ClientDtpFullUpdate clientDtpFullUpdate = new ClientDtpFullUpdate();
        clientDtpFullUpdate.setFirstName("Updated Name");

        Client mockClient = new Client();
        mockClient.setId(clientId);  // Set the ID in the mock response
        Mockito.when(clientService.updateClient(clientId.toString(), clientDtpFullUpdate)).thenReturn(mockClient);

        String responseContent = null;
        try {
            responseContent = mockMvc.perform(MockMvcRequestBuilders.put("/client/update-client/{id}", clientId)
                            .content(asJsonString(clientDtpFullUpdate))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("Response Content: " + responseContent);

        verify(clientService).updateClient(clientId.toString(), clientDtpFullUpdate);  // Verify that the service method was called with the correct ID and DTO


    }

    @Test
    void deleteClientTest() {

        Mockito.when(clientService.deleteClient(clientId.toString())).thenReturn("Client deleted successfully");

        try {
            mockMvc.perform(MockMvcRequestBuilders.delete("/client/delete-client/{id}", clientId))
                    .andExpect(status().isOk())  // Expect a successful response
                    .andExpect(MockMvcResultMatchers.content().string("Client deleted successfully"));  // Expect the correct response message
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(clientService).deleteClient(clientId.toString());  // Verify that the service method was called with the correct ID

    }

    @Test
    void getListAccountByClientIdTest() {

        List<AccountDto> mockClients = Collections.singletonList(new AccountDto());
        Mockito.when(accountService.getListAccount(clientId)).thenReturn(mockClients);

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/client/all-accounts?id={id}", clientId))
                    .andExpect(status().isOk())  // Expect a successful response
                    .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(mockClients.size()));  // Expect the correct number of cards in the JSON response
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(accountService).getListAccount(clientId);  // Verify that the service method was called with the correct ID

    }

    // Helper method to convert objects to JSON string
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}