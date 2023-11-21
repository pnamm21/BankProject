package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.AccountDtoFullUpdate;
import com.bankapp.bankapp.app.dto.AccountDtoPost;
import com.bankapp.bankapp.app.dto.CardDto;
import com.bankapp.bankapp.app.dto.TransactionDtoTransfer;
import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.service.AccountService;
import com.bankapp.bankapp.app.service.util.TransactionService;
import com.bankapp.bankapp.app.service.util.CardService;
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

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private CardService cardService;

    private static final UUID accountId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test case for the getById endpoint
    @Test
    void getById_ValidId_ReturnsOk() {

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/account/{id}", accountId.toString()))
                    .andExpect(status().isOk());  // Expect a successful response (status code 200 OK)
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // Test case for the getAccount endpoint
    @Test
    void getAccount_ValidId_ReturnsOk() {

        Account mockAccount = new Account();
        mockAccount.setId(accountId);
        Mockito.when(accountService.getAccountById(accountId.toString())).thenReturn(Optional.of(mockAccount));


        String responseContent;
        try {
            responseContent = mockMvc.perform(MockMvcRequestBuilders.get("/api/account/get/{id}", accountId))
                    .andExpect(status().isOk())  // Expect a successful response
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("Response Content: " + responseContent);

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/account/get/{id}", accountId))
                    .andExpect(status().isOk())  // Expect a successful response
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(accountId.toString()));  // Expect the correct account ID in the JSON response
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    // Test case for the createAccount endpoint
    @Test
    void createAccount_ValidAccountDto_ReturnsOk() {

        AccountDtoPost accountDto = new AccountDtoPost();
        accountDto.setName("John Doe");

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/api/account/client-accounts/new")
                            .content(asJsonString(accountDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());  // Expect a successful response
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // Test case for the getCards endpoint
    @Test
    void getCards_ValidId_ReturnsOk() {

        List<CardDto> mockCards = Collections.singletonList(new CardDto());
        Mockito.when(cardService.getListCards(accountId)).thenReturn(mockCards);

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/account/get-cards?id={id}", accountId))
                    .andExpect(status().isOk())  // Expect a successful response
                    .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(mockCards.size()));  // Expect the correct number of cards in the JSON response
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(cardService).getListCards(accountId);  // Verify that the service method was called with the correct ID

    }

    // Test case for the deleteAccount endpoint
    @Test
    void deleteAccount_ValidId_ReturnsOk() {

        Mockito.when(accountService.deleteAccount(accountId.toString())).thenReturn("Account deleted successfully");

        try {
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/account/delete-account/{id}", accountId))
                    .andExpect(status().isOk())  // Expect a successful response
                    .andExpect(MockMvcResultMatchers.content().string("Account deleted successfully"));  // Expect the correct response message
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(accountService).deleteAccount(accountId.toString());  // Verify that the service method was called with the correct ID

    }

    // Test case for the updateAccount endpoint
    @Test
    void updateAccount_ValidIdAndDto_ReturnsOk() {

        AccountDtoFullUpdate accountDto = new AccountDtoFullUpdate();
        accountDto.setName("Updated Name");

        Account mockUpdatedAccount = new Account();
        mockUpdatedAccount.setId(accountId);  // Set the ID in the mock response
        Mockito.when(accountService.updateAccount(accountId.toString(), accountDto)).thenReturn(mockUpdatedAccount);

        String responseContent;
        try {
            responseContent = mockMvc.perform(MockMvcRequestBuilders.put("/api/account/update-account/{id}", accountId)
                            .content(asJsonString(accountDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("Response Content: " + responseContent);

        verify(accountService).updateAccount(accountId.toString(), accountDto);  // Verify that the service method was called with the correct ID and DTO

    }

    @Test
    void transferTest() {

        TransactionDtoTransfer transactionDtoTransfer = new TransactionDtoTransfer();
        transactionDtoTransfer.setAmount("1000.0");

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/api/account/transfer/{id}", accountId)
                            .content(asJsonString(transactionDtoTransfer))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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

