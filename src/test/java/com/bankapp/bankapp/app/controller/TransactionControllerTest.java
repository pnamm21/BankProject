package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.TransactionDto;
import com.bankapp.bankapp.app.dto.TransactionDtoFullUpdate;
import com.bankapp.bankapp.app.entity.Transaction;
import com.bankapp.bankapp.app.service.util.TransactionService;
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
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    private static final UUID transactionId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getListTransactionByCreditAccountIdTest() {

        List<TransactionDto> transactionDtos = Collections.singletonList(new TransactionDto());
        Mockito.when(transactionService.getListTransactionByCreditAccountId(transactionId)).thenReturn(transactionDtos);

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/transaction/creditAccount/all-transactions?id={id}", transactionId.toString()))
                    .andExpect(status().isOk())  // Expect a successful response
                    .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(transactionDtos.size()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(transactionService).getListTransactionByCreditAccountId(transactionId);  // Verify that the service method was called with the correct ID

    }

    @Test
    void getListTransactionByDebitAccountIdTest() {

        List<TransactionDto> transactionDtos = Collections.singletonList(new TransactionDto());
        Mockito.when(transactionService.getListTransactionByDebitAccountId(transactionId)).thenReturn(transactionDtos);

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/transaction/debitAccount/all-transactions?id={id}", transactionId.toString()))
                    .andExpect(status().isOk())  // Expect a successful response
                    .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(transactionDtos.size()));  // Expect the correct number of cards in the JSON response
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(transactionService).getListTransactionByDebitAccountId(transactionId);  // Verify that the service method was called with the correct ID

    }

    @Test
    void updateTransactionTest() {

        TransactionDtoFullUpdate transactionDtoFullUpdate = new TransactionDtoFullUpdate();
        transactionDtoFullUpdate.setAmount("1000.0");

        Transaction transaction = new Transaction();
        transaction.setId(transactionId);  // Set the ID in the mock response
        Mockito.when(transactionService.updateTransaction(transactionId.toString(), transactionDtoFullUpdate)).thenReturn(transaction);

        String responseContent;
        try {
            responseContent = mockMvc.perform(MockMvcRequestBuilders.put("/api/transaction/update-transaction/{id}", transactionId)
                            .content(asJsonString(transactionDtoFullUpdate))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("Response Content: " + responseContent);

        verify(transactionService).updateTransaction(transactionId.toString(), transactionDtoFullUpdate);  // Verify that the service method was called with the correct ID and DTO

    }

    @Test
    void deleteTransactionTest() {

        Mockito.when(transactionService.deleteTransaction(transactionId.toString())).thenReturn("Transaction deleted successfully");

        try {
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/transaction/delete-transaction/{id}", transactionId))
                    .andExpect(status().isOk())  // Expect a successful response
                    .andExpect(MockMvcResultMatchers.content().string("Transaction deleted successfully"));  // Expect the correct response message
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(transactionService).deleteTransaction(transactionId.toString());  // Verify that the service method was called with the correct ID

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