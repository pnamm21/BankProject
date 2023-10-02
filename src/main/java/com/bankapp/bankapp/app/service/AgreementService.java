package com.bankapp.bankapp.app.service;

import com.bankapp.bankapp.app.entity.Agreement;
import org.springframework.stereotype.Service;

@Service
public interface AgreementService {

    Agreement updateAgreement(String id,Agreement agreement);
}
