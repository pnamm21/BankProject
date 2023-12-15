package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.AccountDto;
import com.bankapp.bankapp.app.dto.ClientDtoFullUpdate;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

//    @Test
//    @WithMockUser(username = "nam",roles = "USER")
//    void getClientTest() {
//
//        Client mockClient = new Client();
//        mockClient.setId(clientId);
//        Mockito.when(clientService.getClientById(clientId.toString())).thenReturn(Optional.of(mockClient));
//
//        String responseContent = null;
//
//        try {
//            responseContent = mockMvc.perform(MockMvcRequestBuilders.get("/client/get/{id}", clientId))
//                    .andExpect(status().isOk())
//                    .andReturn().getResponse().getContentAsString();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        System.out.println("Response Content: " + responseContent);
//
//        try {
//            mockMvc.perform(MockMvcRequestBuilders.get("/client/get/{id}",clientId))
//                    .andExpect(status().isOk())
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(clientId.toString()));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//    }

//    @Test
//    @WithMockUser(username = "nam",roles = "USER")
//    void updateClientTest() {
//
//        ClientDtoFullUpdate clientDtpFullUpdate = new ClientDtoFullUpdate();
//        clientDtpFullUpdate.setFirstName("Updated Name");
//
//        Client mockClient = new Client();
//        mockClient.setId(clientId);
//        Mockito.when(clientService.updateClient(clientId.toString(), clientDtpFullUpdate)).thenReturn(mockClient);
//
//        String responseContent = null;
//        try {
//            responseContent = mockMvc.perform(MockMvcRequestBuilders.put("/client/update-client/{id}", clientId)
//                            .content(asJsonString(clientDtpFullUpdate))
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andReturn().getResponse().getContentAsString();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        System.out.println("Response Content: " + responseContent);
//
//        verify(clientService).updateClient(clientId.toString(), clientDtpFullUpdate);
//
//    }

    @Test
    @WithMockUser(username = "nam",roles = "USER")
    void deleteClientTest() {

        Mockito.when(clientService.deleteClient(clientId.toString())).thenReturn("Client deleted successfully");

        try {
            mockMvc.perform(MockMvcRequestBuilders.delete("/client/delete-client/{id}", clientId))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string("Client deleted successfully"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(clientService).deleteClient(clientId.toString());

    }

    @Test
    @WithMockUser(username = "nam",roles = "USER")
    void getListAccountByClientIdTest() {

        List<AccountDto> mockClients = Collections.singletonList(new AccountDto());
        Mockito.when(accountService.getListAccount(clientId)).thenReturn(mockClients);

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/client/all-accounts?id={id}", clientId))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(mockClients.size()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(accountService).getListAccount(clientId);

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