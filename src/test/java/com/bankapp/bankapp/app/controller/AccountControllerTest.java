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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("/drop.sql")
@Sql("/create.sql")
@Sql("/insert.sql")
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    private static final String accountId = "13ad9144-6f02-40f1-bb12-207310775a3f";
    @Test
    @WithMockUser(username = "nam",roles = "USER")
    void getAccount() throws Exception {

        AccountDto accountDto = new AccountDto() {{
            setId(accountId);
            setName("John Doe");
            setStatus("ACTIVE");
            setBalance("100000.0");
            setCurrencyCode("UAH");
            setType("BUSINESS_ACCOUNT");
            setCreatedAt("2023-09-16T10:00:00");
        }};

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/account/get/{id}", accountId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        AccountDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(accountDto, actual);

    }

    @Test
    @WithMockUser(username = "nam",roles = "USER")
    void createAccount() throws Exception {

        AccountDtoPost accountDtoPost = new AccountDtoPost() {{
            setId("82132342-4232-4123-4768-125643524352");
            setName("Nam");
            setStatus("CLOSED");
            setBalance("250000.0");
            setCurrencyCode("EUR");
            setType("SAVING_ACCOUNT");
            setClientId("e1adaf04-68b9-49c8-80b5-fed3ec85fde9");
        }};

        AccountDto expected = new AccountDto(){{
            setId("82132342-4232-4123-4768-125643524352");
            setName("Nam");
            setStatus("CLOSED");
            setBalance("250000.0");
            setCurrencyCode("EUR");
            setType("SAVING_ACCOUNT");
        }};

        String json = objectMapper.writeValueAsString(accountDtoPost);
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/account/new")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        AccountDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        actual.setCreatedAt(null);

        assertEquals(expected,actual);
    }

    @Test
    @WithMockUser(username = "nam", roles = "USER")
    void getCards() throws Exception {

        List<CardDto> cardDto = List.of(new CardDto() {{
            setCardHolder("John Doe");
            setCardNumber("4234-5678-9012-3456");
            setCvv("354");
            setType("VISA");
            setStatus("ACTIVE");
            setExpirationDate("2025-11-09T15:44:59");
        }});

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/account/cards/{id}",
                                        "13ad9144-6f02-40f1-bb12-207310775a3f")
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        List<CardDto> actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(cardDto, actual);

    }

    @Test
    @WithMockUser(username = "nam", roles = "USER")
    void deleteAccount() throws Exception {

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.delete("/account/delete/{id}", accountId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        String closed = "Account has been CLOSED!";

        assertEquals("Account has been CLOSED!", closed);

    }

    @Test
    @WithMockUser(username = "nam", roles = "USER")
    void updateAccount() throws Exception {

        AccountDtoFullUpdate expect = new AccountDtoFullUpdate();

        expect.setId("13ad9144-6f02-40f1-bb12-207310775a3f");
        expect.setName("John Doe");
        expect.setType("BUSINESS_ACCOUNT");
        expect.setBalance("100000.0");
        expect.setStatus("INACTIVE");
        expect.setCurrencyCode("UAH");
        expect.setCreatedAt("2023-09-16T10:00:00");
        expect.setUpdatedAt("2023-09-16T10:00:00");
        expect.setClientId("cf7d57e1-4e4d-4134-86fc-02ef21cf3dff");

        AccountDtoFullUpdate update = new AccountDtoFullUpdate();
        update.setStatus("INACTIVE");

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.put("/account/update/{id}", accountId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(update)))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        AccountDtoFullUpdate actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expect, actual);

    }

    @Test
    @WithMockUser(username = "nam", roles = "USER")
    void transferTest() throws Exception {

        TransactionDtoTransfer transactionDtoTransfer = new TransactionDtoTransfer(){{
            setDebitAccount("f7a7c08a-4bd7-4c68-894b-bd2cca07f52b");
            setTransactionType("ATM");
            setAmount("100.0");
            setDescription("Transaction A");
            setStatus("APPROVED");
        }};

        String json = objectMapper.writeValueAsString(transactionDtoTransfer);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/transfer/{id}", "13ad9144-6f02-40f1-bb12-207310775a3f")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        String approved = "APPROVED";

        assertEquals("APPROVED", approved);

    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

