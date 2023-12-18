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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @WithMockUser(username = "nam",roles = "USER")
    void getListTransactionByCreditAccountIdTest() throws Exception {

        List<TransactionDto> transactionDtos = Collections.singletonList(new TransactionDto());
        Mockito.when(transactionService.getListTransactionByCreditAccountId(transactionId)).thenReturn(transactionDtos);

            mockMvc.perform(MockMvcRequestBuilders.get("/api/transaction/creditAccount/transactions/{id}", transactionId.toString()))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(transactionDtos.size()));

        verify(transactionService).getListTransactionByCreditAccountId(transactionId);

    }

    @Test
    @WithMockUser(username = "nam",roles = "USER")
    void getListTransactionByDebitAccountIdTest() throws Exception {

        List<TransactionDto> transactionDtos = Collections.singletonList(new TransactionDto());
        Mockito.when(transactionService.getListTransactionByDebitAccountId(transactionId)).thenReturn(transactionDtos);

            mockMvc.perform(MockMvcRequestBuilders.get("/api/transaction/debitAccount/transactions/{id}", transactionId.toString()))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(transactionDtos.size()));

        verify(transactionService).getListTransactionByDebitAccountId(transactionId);

    }

    @Test
    @WithMockUser(username = "nam",roles = "USER")
    void updateTransactionTest() throws Exception {

        TransactionDtoFullUpdate transactionDtoFullUpdate = new TransactionDtoFullUpdate();
        transactionDtoFullUpdate.setAmount("1000.0");

        Transaction transaction = new Transaction();
        transaction.setId(transactionId);

        String responseContent;

            responseContent = mockMvc.perform(MockMvcRequestBuilders.put("/api/transaction/update/{id}", transactionId)
                            .content(asJsonString(transactionDtoFullUpdate))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();


        System.out.println("Response Content: " + responseContent);

        verify(transactionService).updateTransaction(transactionId.toString(), transactionDtoFullUpdate);

    }

    @Test
    @WithMockUser(username = "nam",roles = "USER")
    void deleteTransactionTest() throws Exception {

        Mockito.when(transactionService.deleteTransaction(transactionId.toString())).thenReturn("Transaction deleted successfully");

            mockMvc.perform(MockMvcRequestBuilders.delete("/api/transaction/delete/{id}", transactionId))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string("Transaction deleted successfully"));

        verify(transactionService).deleteTransaction(transactionId.toString());

    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}