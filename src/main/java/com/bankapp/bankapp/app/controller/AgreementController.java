package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.AgreementDto;
import com.bankapp.bankapp.app.dto.AgreementFullDtoUpdate;
import com.bankapp.bankapp.app.entity.Agreement;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import com.bankapp.bankapp.app.exception.ExceptionMessage;
import com.bankapp.bankapp.app.mapper.AgreementMapper;
import com.bankapp.bankapp.app.service.AgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/agreement")
@RequiredArgsConstructor
public class AgreementController {

    private final AgreementService agreementService;
    private final AgreementMapper agreementMapper;

    @GetMapping("/{id}")
    public ResponseEntity<AgreementDto> getAgreement(@PathVariable("id") String id) {
        Optional<Agreement> optionalAgreement;
        try {
            optionalAgreement = agreementService.getAgreementById(id);
        } catch (Exception e) {
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }
        if (optionalAgreement.isEmpty()) {
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }
        return new ResponseEntity<>(agreementMapper.agreementToAgreementDto(optionalAgreement.get()), HttpStatus.OK);
    }

    @RequestMapping(value = "/update-agreement/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public ResponseEntity<AgreementDto> updateAgreement(@PathVariable("id") String id, @RequestBody AgreementFullDtoUpdate agreementFullDto) {
        return ResponseEntity.ofNullable(agreementMapper.agreementToAgreementDto(agreementService.updateAgreement(id, agreementFullDto)));
    }

}