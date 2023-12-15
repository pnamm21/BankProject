package com.bankapp.bankapp.app.service.impl;

import com.bankapp.bankapp.app.dto.AgreementDto;
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

    private static final UUID agreementId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAgreementByIdTest() {

        Agreement expectedAgreement = new Agreement();
        AgreementDto expectedDto = new AgreementDto();

        when(agreementRepository.findById(agreementId)).thenReturn(Optional.of(expectedAgreement));
        when(agreementMapper.agreementToAgreementDto(expectedAgreement)).thenReturn(expectedDto);

        AgreementDto resultDto = agreementService.getAgreementById(agreementId.toString());

        assertNotNull(resultDto);
        assertSame(expectedDto, resultDto);

        verify(agreementRepository, times(1)).findById(agreementId);
        verify(agreementMapper, times(1)).agreementToAgreementDto(expectedAgreement);
    }

    @Test
    void getAgreementByIdNotFoundTest() {

        when(agreementRepository.findById(agreementId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> {
            agreementService.getAgreementById(agreementId.toString());
        });

        verify(agreementRepository, times(1)).findById(agreementId);
    }

    @Test
    void updateAgreementTest() {

        AgreementFullDtoUpdate agreementFullDtoUpdate = new AgreementFullDtoUpdate();
        Agreement originalAgreement = new Agreement();
        Agreement updatedAgreement = new Agreement();

        when(agreementRepository.existsById(agreementId)).thenReturn(true);
        when(agreementRepository.findById(agreementId)).thenReturn(Optional.of(originalAgreement));
        when(agreementMapper.agreementFullDtoToAgreement(agreementFullDtoUpdate)).thenReturn(updatedAgreement);
        when(agreementMapper.mergeAgreement(updatedAgreement, originalAgreement)).thenReturn(updatedAgreement);
        when(agreementMapper.agreementToAgreementDto(updatedAgreement)).thenReturn(new AgreementDto());

        AgreementDto result = agreementService.updateAgreement(agreementId.toString(), agreementFullDtoUpdate);

        assertNotNull(result);

        verify(agreementRepository, times(1)).existsById(agreementId);
        verify(agreementRepository, times(1)).findById(agreementId);
        verify(agreementRepository, times(1)).save(updatedAgreement);
    }

    @Test
    void updateAgreementNotFoundTest() {

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