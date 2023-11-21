package com.bankapp.bankapp.app.service;

import com.bankapp.bankapp.app.dto.AgreementDto;
import com.bankapp.bankapp.app.dto.AgreementFullDtoUpdate;
import com.bankapp.bankapp.app.entity.Agreement;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface AgreementService {

    Optional<Agreement> getAgreementById(String id);

    List<AgreementDto> getListAgreement(@Param("id")UUID id);
    @Transactional
    Agreement updateAgreement(String id, AgreementFullDtoUpdate agreementFullDto);
}
