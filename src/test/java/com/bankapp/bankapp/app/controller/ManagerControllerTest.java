package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.*;
import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.entity.Manager;
import com.bankapp.bankapp.app.service.ClientService;
import com.bankapp.bankapp.app.service.ManagerService;
import com.bankapp.bankapp.app.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ManagerController.class)
class ManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManagerService managerService;

    @MockBean
    private ClientService clientService;

    @MockBean
    private ProductService productService;

    private static final UUID managerId = UUID.randomUUID();

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getManagerTest() {

        Manager mockManager = new Manager();
        mockManager.setId(managerId);
        Mockito.when(managerService.getManagerById(managerId.toString())).thenReturn(Optional.of(mockManager));


        String responseContent = null;
        try {
            responseContent = mockMvc.perform(MockMvcRequestBuilders.get("/manager/get/{id}", managerId))
                    .andExpect(status().isOk())  // Expect a successful response
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("Response Content: " + responseContent);

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/manager/get/{id}", managerId))
                    .andExpect(status().isOk())  // Expect a successful response
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(managerId.toString()));  // Expect the correct account ID in the JSON response
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void updateManagerTest() {

        ManagerDtoFullUpdate managerDtoFullUpdate = new ManagerDtoFullUpdate();
        managerDtoFullUpdate.setFirstName("Updated Name");

        Manager manager = new Manager();
        manager.setId(managerId);  // Set the ID in the mock response
        Mockito.when(managerService.updateManager(managerId.toString(), managerDtoFullUpdate)).thenReturn(manager);

        String responseContent = null;
        try {
            responseContent = mockMvc.perform(MockMvcRequestBuilders.put("/manager/update-manager/{id}", managerId)
                            .content(asJsonString(managerDtoFullUpdate))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("Response Content: " + responseContent);

        verify(managerService).updateManager(managerId.toString(), managerDtoFullUpdate);  // Verify that the service method was called with the correct ID and DTO

    }

    @Test
    void getClientsTest() {

        List<ClientDto> clientDtos = Collections.singletonList(new ClientDto());
        Mockito.when(clientService.getListClients(managerId)).thenReturn(clientDtos);

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/manager/get/all-clients?id={id}", managerId))
                    .andExpect(status().isOk())  // Expect a successful response
                    .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(clientDtos.size()));  // Expect the correct number of cards in the JSON response
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(clientService).getListClients(managerId);  // Verify that the service method was called with the correct ID

    }

    @Test
    void getProductsTest() {

        List<ProductDto> productDtos = Collections.singletonList(new ProductDto());
        Mockito.when(productService.getListProduct(managerId)).thenReturn(productDtos);

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/manager/get/all-products?id={id}", managerId))
                    .andExpect(status().isOk())  // Expect a successful response
                    .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(productDtos.size()));  // Expect the correct number of cards in the JSON response
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(productService).getListProduct(managerId);  // Verify that the service method was called with the correct ID

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