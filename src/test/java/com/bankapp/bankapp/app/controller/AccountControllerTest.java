package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String accountId = "240a158e-d55b-46d3-86a2-88914dae4e68";

    @Test
    @WithMockUser(username = "nam",roles = "USER")
    void getAccount() throws Exception {

        AccountDto accountDto = new AccountDto() {{
            setId(accountId);
            setName("Hannah Taylor");
            setStatus("CLOSED");
            setBalance("250000.0");
            setCurrencyCode("EUR");
            setType("SAVING_ACCOUNT");
            setCreatedAt("2023-09-18T10:00:00");
        }};

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/account/get/{id}", accountId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("id", accountId))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        AccountDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(accountDto, actual);

    }

    @Test
    void createAccount() throws Exception {

        AccountDtoPost accountDtoPost = new AccountDtoPost() {{
            setId(accountId);
            setName("Nam");
            setStatus("CLOSED");
            setBalance("250000.0");
            setCurrencyCode("EUR");
            setType("SAVING_ACCOUNT");
            setClientId("56876795-4d60-413b-8e30-ee56f49df814");
        }};

        String json = objectMapper.writeValueAsString(accountDtoPost);

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/account/new", accountId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        AccountDtoPost actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(accountDtoPost, actual);
    }

//    @Test
//    @WithMockUser(username = "nam", roles = "USER")
//    void getCards_ValidId_ReturnsOk() throws Exception {
//
//        List<CardDto> mockCards = Collections.singletonList(new CardDto());
//        Mockito.when(cardService.getListCards(accountId)).thenReturn(mockCards);
//
//            mockMvc.perform(MockMvcRequestBuilders.get("/api/account/get-cards?id={id}", accountId))
//                    .andExpect(status().isOk())
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(mockCards.size()));
//
//        verify(cardService).getListCards(accountId);
//
//    }
//
//    // Test case for the deleteAccount endpoint
//    @Test
//    @WithMockUser(username = "nam", roles = "USER")
//    void deleteAccount_ValidId_ReturnsOk() throws Exception {
//
//        Mockito.when(accountService.deleteAccount(accountId.toString())).thenReturn("Account deleted successfully");
//
//
//            mockMvc.perform(MockMvcRequestBuilders.delete("/api/account/delete-account/{id}", accountId)
//                            .with(csrf()))
//                    .andExpect(status().isOk())  // Expect a successful response
//                    .andExpect(MockMvcResultMatchers.content().string("Account deleted successfully"));  // Expect the correct response message
//
//        verify(accountService).deleteAccount(accountId.toString());  // Verify that the service method was called with the correct ID
//
//    }
//
//    // Test case for the updateAccount endpoint
//    @Test
//    @WithMockUser(username = "nam", roles = "USER")
//    void updateAccount_ValidIdAndDto_ReturnsOk() throws Exception {
//
//        AccountDtoFullUpdate accountDto = new AccountDtoFullUpdate();
//        accountDto.setName("Updated Name");
//
//        Account mockUpdatedAccount = new Account();
//        mockUpdatedAccount.setId(accountId);  // Set the ID in the mock response
//        Mockito.when(accountService.updateAccount(accountId.toString(), accountDto)).thenReturn(mockUpdatedAccount);
//
//        String responseContent;
//
//            responseContent = mockMvc.perform(MockMvcRequestBuilders.put("/api/account/update-account/{id}", accountId)
//                            .content(asJsonString(accountDto))
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .with(csrf()))
//                    .andExpect(status().isOk())
//                    .andReturn().getResponse().getContentAsString();
//
//        System.out.println("Response Content: " + responseContent);
//
//        verify(accountService).updateAccount(accountId.toString(), accountDto);  // Verify that the service method was called with the correct ID and DTO
//
//    }

    @Test
    @WithMockUser(username = "nam", roles = "USER")
    void transferTest() throws Exception {

        TransactionDtoTransfer transactionDtoTransfer = new TransactionDtoTransfer();
        transactionDtoTransfer.setAmount("1000.0");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/account/transfer/{id}", accountId)
                        .content(asJsonString(transactionDtoTransfer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isBadRequest());

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

