package com.bankapp.bankapp.app.service.impl;

import com.bankapp.bankapp.app.dto.AgreementFullDtoUpdate;
import com.bankapp.bankapp.app.entity.Agreement;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import com.bankapp.bankapp.app.mapper.AgreementMapper;
import com.bankapp.bankapp.app.repository.AgreementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgreementServiceImplTest {

    @Mock
    private AgreementRepository agreementRepository;

    @Mock
    private AgreementMapper agreementMapper;

    @InjectMocks
    private AgreementServiceImpl agreementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAgreementByIdTest() {
        UUID agreementId = UUID.randomUUID();
        Agreement agreement = new Agreement();
        agreement.setId(agreementId);

        when(agreementRepository.findById(agreementId)).thenReturn(Optional.of(agreement));

        Optional<Agreement> result = agreementService.getAgreementById(agreementId.toString());

        assertTrue(result.isPresent());
        assertEquals(agreement, result.get());

        verify(agreementRepository, times(1)).findById(agreementId);
    }

    @Test
    void getAgreementByIdNotFoundTest() {
        UUID agreementId = UUID.randomUUID();

        when(agreementRepository.findById(agreementId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> {
            agreementService.getAgreementById(agreementId.toString());
        });

        verify(agreementRepository, times(1)).findById(agreementId);
    }

    @Test
    void updateAgreement() {

        UUID agreementId = UUID.randomUUID();
        AgreementFullDtoUpdate agreementFullDtoUpdate = new AgreementFullDtoUpdate();
        agreementFullDtoUpdate.setAccountId(String.valueOf(UUID.randomUUID()));
        agreementFullDtoUpdate.setProductId(String.valueOf(UUID.randomUUID()));

        Agreement originalAgreement = new Agreement();
        originalAgreement.setId(agreementId);

        Agreement updatedAgreement = new Agreement();
        updatedAgreement.setId(agreementId);

        when(agreementRepository.existsById(agreementId)).thenReturn(true);
        when(agreementRepository.findById(agreementId)).thenReturn(Optional.of(originalAgreement));
        when(agreementMapper.agreementFullDtoToAgreement(agreementFullDtoUpdate)).thenReturn(updatedAgreement);
        when(agreementMapper.mergeAgreement(updatedAgreement, originalAgreement)).thenReturn(updatedAgreement);
        when(agreementRepository.save(updatedAgreement)).thenReturn(updatedAgreement);

        Agreement result = agreementService.updateAgreement(agreementId.toString(), agreementFullDtoUpdate);

        assertNotNull(result);
        assertEquals(agreementId, result.getId());

        verify(agreementRepository, times(1)).existsById(agreementId);
        verify(agreementRepository, times(1)).findById(agreementId);
        verify(agreementMapper, times(1)).agreementFullDtoToAgreement(agreementFullDtoUpdate);
        verify(agreementMapper, times(1)).mergeAgreement(updatedAgreement, originalAgreement);
        verify(agreementRepository, times(1)).save(updatedAgreement);

    }

    @Test
    void updateAgreementNotFoundTest() {
        UUID agreementId = UUID.randomUUID();
        AgreementFullDtoUpdate agreementFullDtoUpdate = new AgreementFullDtoUpdate();

        when(agreementRepository.existsById(agreementId)).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> {
            agreementService.updateAgreement(agreementId.toString(), agreementFullDtoUpdate);
        });

        verify(agreementRepository, times(1)).existsById(agreementId);
        verify(agreementRepository, never()).findById(agreementId);
        verify(agreementMapper, never()).agreementFullDtoToAgreement(agreementFullDtoUpdate);
        verify(agreementMapper, never()).mergeAgreement(any(), any());
        verify(agreementRepository, never()).save(any());
    }
}