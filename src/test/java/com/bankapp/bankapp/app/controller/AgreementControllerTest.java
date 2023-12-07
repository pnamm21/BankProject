package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.AccountDtoFullUpdate;
import com.bankapp.bankapp.app.dto.AgreementDto;
import com.bankapp.bankapp.app.dto.AgreementFullDtoUpdate;
import com.bankapp.bankapp.app.dto.CardDto;
import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.entity.Agreement;
import com.bankapp.bankapp.app.service.AgreementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AgreementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgreementService agreementService;

    private static final UUID agreementId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "nam",roles = "USER")
    void getAgreementTestTest(){

        Agreement agreement = new Agreement();
        agreement.setId(agreementId);
        Mockito.when(agreementService.getAgreementById(agreementId.toString())).thenReturn(Optional.of(agreement));

        String responseContent = null;
        try {
            responseContent = mockMvc.perform(MockMvcRequestBuilders.get("/api/agreement/get/{id}", agreementId)
                            .secure(true))
                    .andExpect(status().isOk()) // Expect a successful response
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("Response Content: " + responseContent);

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/agreement/get/{id}", agreementId))
                    .andExpect(status().isOk())  // Expect a successful response
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(agreementId.toString()));  // Expect the correct account ID in the JSON response
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    @WithMockUser(username = "nam",roles = "USER")
    void getListAgreementDtoTest(){

        List<AgreementDto> agreementDtos = Collections.singletonList(new AgreementDto());
        Mockito.when(agreementService.getListAgreement(agreementId)).thenReturn(agreementDtos);

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/agreement/all-agreements?id={id}", agreementId))
                    .andExpect(status().isOk())  // Expect a successful response
                    .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(agreementDtos.size()));  // Expect the correct number of cards in the JSON response
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(agreementService).getListAgreement(agreementId);  // Verify that the service method was called with the correct ID

    }

    @Test
    @WithMockUser(username = "nam",roles = "USER")
    void updateAgreementTest(){

        AgreementFullDtoUpdate agreementFullDtoUpdate = new AgreementFullDtoUpdate();
        agreementFullDtoUpdate.setStatus("INACTIVE");

        Agreement agreement = new Agreement();
        agreement.setId(agreementId);  // Set the ID in the mock response
        Mockito.when(agreementService.updateAgreement(agreementId.toString(), agreementFullDtoUpdate)).thenReturn(agreement);

        String responseContent = null;
        try {
            responseContent = mockMvc.perform(MockMvcRequestBuilders.put("/api/agreement/update-agreement/{id}", agreementId)
                            .content(asJsonString(agreementFullDtoUpdate))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("Response Content: " + responseContent);

        verify(agreementService).updateAgreement(agreementId.toString(), agreementFullDtoUpdate);  // Verify that the service method was called with the correct ID and DTO

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