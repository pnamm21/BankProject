package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.entity.Agreement;
import com.bankapp.bankapp.app.service.AgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agreement")
@RequiredArgsConstructor
public class AgreementController {

    private final AgreementService agreementService;

    @PutMapping("/update-agreement")
    public Agreement updateAgreement(String id,Agreement agreement){
        return agreementService.updateAgreement(id,agreement);
    }

}