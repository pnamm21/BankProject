package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.CardDto;
import com.bankapp.bankapp.app.dto.CardDtoPost;
import com.bankapp.bankapp.app.dto.TransactionDtoTransferCard;
import com.bankapp.bankapp.app.entity.Card;
import com.bankapp.bankapp.app.service.util.CardService;
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

import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
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
    @WithMockUser(username = "nam",roles = "USER")
    void getCardTest() throws Exception {

        CardDto card = new CardDto();
        card.setId(String.valueOf(cardId));
        Mockito.when(cardService.getCardById(cardId.toString())).thenReturn(card);

        String responseContent = null;

            responseContent = mockMvc.perform(MockMvcRequestBuilders.get("/api/card/get/{id}", cardId))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();


        System.out.println("Response Content: " + responseContent);

            mockMvc.perform(MockMvcRequestBuilders.get("/api/card/get/{id}", cardId))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(cardId.toString()));

    }

    @Test
    @WithMockUser(username = "nam",roles = "USER")
    void createMasterCardTest(){

        CardDtoPost cardDtoPost = new CardDtoPost();
        cardDtoPost.setType("MASTERCARD");

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/api/card/create-master-card")
                            .content(asJsonString(cardDtoPost))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    @WithMockUser(username = "nam",roles = "USER")
    void createVisaCardTest(){

        CardDtoPost cardDtoPost = new CardDtoPost();
        cardDtoPost.setType("VISA");

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/api/card/create-visa-card")
                            .content(asJsonString(cardDtoPost))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    @WithMockUser(username = "nam",roles = "USER")
    void transferByCardNumberTest() {

        TransactionDtoTransferCard transactionDtoTransferCard = new TransactionDtoTransferCard();
        transactionDtoTransferCard.setTo("5931-8562-1234-9876");

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/api/card/card-transfer/{cardNumber}","4434-5678-9012-3456")
                            .content(asJsonString(transactionDtoTransferCard))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}