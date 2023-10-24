package com.bankapp.bankapp.app.service.impl;

import com.bankapp.bankapp.app.dto.AgreementFullDtoUpdate;
import com.bankapp.bankapp.app.entity.Agreement;
import com.bankapp.bankapp.app.mapper.AgreementMapper;
import com.bankapp.bankapp.app.repository.AgreementRepository;
import com.bankapp.bankapp.app.service.AgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class AgreementServiceImpl implements AgreementService {

    private final AgreementRepository agreementRepository;
    private final AgreementMapper agreementMapper;

    public AgreementServiceImpl(AgreementRepository agreementRepository, AgreementMapper agreementMapper) {
        this.agreementRepository = agreementRepository;
        this.agreementMapper = agreementMapper;
    }

    @Override
    public Optional<Agreement> getAgreementById(String id)throws NoSuchElementException{
        return agreementRepository.findById(UUID.fromString(id));
    }

    @Override
    @Transactional
    public Agreement updateAgreement(String id, AgreementFullDtoUpdate agreementFullDtoUpdate) {
        UUID stringId = UUID.fromString(id);
        if (agreementRepository.existsById(stringId)) {
            agreementFullDtoUpdate.setAccountId(agreementFullDtoUpdate.getAccountId());
            agreementFullDtoUpdate.setProductId(agreementFullDtoUpdate.getProductId());
            agreementFullDtoUpdate.setId(id);
            Agreement agreement = agreementMapper.agreementFullDtoToAgreement(agreementFullDtoUpdate);
            Agreement original = agreementRepository.findById(stringId).orElseThrow();
            agreement.setAccount(original.getAccount());
            agreement.setProduct(original.getProduct());
            Agreement updated = agreementMapper.mergeAgreement(agreement,original);
            return agreementRepository.save(updated);
        }
        return null;
    }
}