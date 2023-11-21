package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.AccountDtoPost;
import com.bankapp.bankapp.app.dto.CardDtoPost;
import com.bankapp.bankapp.app.dto.TransactionDtoTransferCard;
import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.entity.Card;
import com.bankapp.bankapp.app.service.AccountService;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;
import java.util.UUID;

import static java.lang.reflect.Array.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService cardService;

    private static final UUID cardId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCardTest(){

        Card card = new Card();
        card.setId(cardId);
        Mockito.when(cardService.getCardById(cardId.toString())).thenReturn(Optional.of(card));

        String responseContent = null;
        try {
            responseContent = mockMvc.perform(MockMvcRequestBuilders.get("/api/card/get/{id}", cardId))
                    .andExpect(status().isOk())  // Expect a successful response
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("Response Content: " + responseContent);

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/card/get/{id}", cardId))
                    .andExpect(status().isOk())  // Expect a successful response
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(cardId.toString()));  // Expect the correct account ID in the JSON response
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void createMasterCardTest(){

        CardDtoPost cardDtoPost = new CardDtoPost();
        cardDtoPost.setType("MASTERCARD");

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/api/card/create-master-card")
                            .content(asJsonString(cardDtoPost))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());  // Expect a successful response
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void createVisaCardTest(){

        CardDtoPost cardDtoPost = new CardDtoPost();
        cardDtoPost.setType("VISA");

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/api/card/create-visa-card")
                            .content(asJsonString(cardDtoPost))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());  // Expect a successful response
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void transferByCardNumberTest() {

        TransactionDtoTransferCard transactionDtoTransferCard = new TransactionDtoTransferCard();
        transactionDtoTransferCard.setFrom("123");
        transactionDtoTransferCard.setTo("123");

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/api/card/card-transfer")
                            .content(asJsonString(transactionDtoTransferCard))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
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