package com.bankapp.bankapp.app.service;

import com.bankapp.bankapp.app.dto.AgreementFullDtoUpdate;
import com.bankapp.bankapp.app.entity.Agreement;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface AgreementService {

    Optional<Agreement> getAgreementById(String id);
    Agreement updateAgreement(String id, AgreementFullDtoUpdate agreementFullDto);
}
