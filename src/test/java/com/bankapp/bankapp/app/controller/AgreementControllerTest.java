package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.AccountDtoFullUpdate;
import com.bankapp.bankapp.app.dto.AgreementDto;
import com.bankapp.bankapp.app.dto.AgreementFullDtoUpdate;
import com.bankapp.bankapp.app.dto.ClientDtoFullUpdate;
import com.bankapp.bankapp.app.entity.Agreement;
import com.bankapp.bankapp.app.entity.Client;
import com.bankapp.bankapp.app.service.AgreementService;
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
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("/drop.sql")
@Sql("/create.sql")
@Sql("/insert.sql")
class AgreementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgreementService agreementService;

    private static final String agreementId = "7a7f43ee-2850-4e92-9bd6-28fd3cf77884";

    @Test
    @WithMockUser(username = "nam", roles = "USER")
    void getAgreementTest() throws Exception {

        AgreementDto agreement = new AgreementDto();
        agreement.setId(agreementId);
        Mockito.when(agreementService.getAgreementById(agreementId)).thenReturn(agreement);

        String responseContent = null;

            responseContent = mockMvc.perform(MockMvcRequestBuilders.get("/api/agreement/get/{id}", agreementId)
                            .secure(true))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

        System.out.println("Response Content: " + responseContent);


            mockMvc.perform(MockMvcRequestBuilders.get("/api/agreement/get/{id}", agreementId))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(agreementId.toString()));

    }

    @Test
    @WithMockUser(username = "nam", roles = "USER")
    void getListAgreementDtoTest() {

        List<AgreementDto> agreementDtos = Collections.singletonList(new AgreementDto());
        Mockito.when(agreementService.getListAgreement(UUID.fromString(agreementId))).thenReturn(agreementDtos);

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/agreement/all-agreements?id={id}", agreementId))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(agreementDtos.size()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(agreementService).getListAgreement(UUID.fromString(agreementId));

    }

    @Test
    @WithMockUser(username = "nam", roles = "USER")
    void updateAgreementTest() throws Exception {

            AgreementFullDtoUpdate agreementFullDtoUpdate = new AgreementFullDtoUpdate();
            agreementFullDtoUpdate.setSum("100.0");

            Agreement agreement = new Agreement();
            agreement.setId(UUID.fromString(agreementId));

            String responseContent = null;

            responseContent = mockMvc.perform(MockMvcRequestBuilders.put("/api/agreement/update/{id}", agreementId)
                            .content(asJsonString(agreementFullDtoUpdate))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            System.out.println("Response Content: " + responseContent);

            verify(agreementService).updateAgreement(agreementId, agreementFullDtoUpdate);

    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}