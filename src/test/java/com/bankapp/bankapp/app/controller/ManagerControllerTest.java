package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.*;
import com.bankapp.bankapp.app.entity.Client;
import com.bankapp.bankapp.app.entity.Manager;
import com.bankapp.bankapp.app.entity.Product;
import com.bankapp.bankapp.app.service.ClientService;
import com.bankapp.bankapp.app.service.ManagerService;
import com.bankapp.bankapp.app.service.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    @WithMockUser(username = "nam",roles = "USER")
    void getManagerTest() throws Exception {

        ManagerDto managerDto = new ManagerDto();
        managerDto.setId(String.valueOf(managerId));

        String responseContent = null;
        String json = objectMapper.writeValueAsString(managerDto);

        responseContent = mockMvc.perform(MockMvcRequestBuilders.get("/api/manager/get/{id}", managerId)
                        .secure(true))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println("Response Content: " + responseContent);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/manager/get/{id}", managerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "nam",roles = "USER")
    void updateManagerTest() throws Exception {

        ManagerDtoFullUpdate managerDtoFullUpdate = new ManagerDtoFullUpdate();
        managerDtoFullUpdate.setId("7a88b870-5c81-41ea-b064-897bd13a7d78");
        managerDtoFullUpdate.setFirstName("Alice");
        managerDtoFullUpdate.setLastName("Smith");
        managerDtoFullUpdate.setStatus("ON_LEAVE");
        managerDtoFullUpdate.setCreatedAt("2023-09-16T11:30:00");
        managerDtoFullUpdate.setUpdatedAt("2023-09-16T11:30:00");

        Manager mockClient = new Manager();
        mockClient.setId(managerId);

        String responseContent = null;

        responseContent = mockMvc.perform(MockMvcRequestBuilders.put("/api/manager/update/{id}", managerId)
                        .content(asJsonString(managerDtoFullUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println("Response Content: " + responseContent);

        verify(managerService).updateManager(managerId.toString(), managerDtoFullUpdate);

    }

    @Test
    @WithMockUser(username = "nam",roles = "USER")
    void getClientsTest() throws Exception {

        List<ClientDto> clientDtos = Collections.singletonList(new ClientDto());
        Mockito.when(clientService.getListClients(managerId)).thenReturn(clientDtos);

            mockMvc.perform(MockMvcRequestBuilders.get("/api/manager/clients?id={id}", managerId))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(clientDtos.size()));

        verify(clientService).getListClients(managerId);

    }

    @Test
    @WithMockUser(username = "nam",roles = "USER")
    void getProductsTest() {

        List<ProductDto> productDtos = Collections.singletonList(new ProductDto());
        Mockito.when(productService.getListProduct(managerId)).thenReturn(productDtos);

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/manager/products?id={id}", managerId))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(productDtos.size()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(productService).getListProduct(managerId);

    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}