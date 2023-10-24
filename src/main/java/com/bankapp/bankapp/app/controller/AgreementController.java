package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.AgreementDto;
import com.bankapp.bankapp.app.validation.UUIDValidator;
import com.bankapp.bankapp.app.dto.AgreementFullDtoUpdate;
import com.bankapp.bankapp.app.entity.Agreement;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import com.bankapp.bankapp.app.exception.ExceptionMessage;
import com.bankapp.bankapp.app.exception.InvalidUUIDException;
import com.bankapp.bankapp.app.mapper.AgreementMapper;
import com.bankapp.bankapp.app.service.AgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/agreement")
@RequiredArgsConstructor
public class AgreementController {

    private final AgreementService agreementService;

    @GetMapping("/{id}")
    public Optional<ResponseEntity<Agreement>> getAgreement(@PathVariable("id") String id) {

        try {
            Optional<Agreement> optionalAgreement = agreementService.getAgreementById(id);
            return optionalAgreement.map(agreement -> new ResponseEntity<>(agreement,HttpStatus.OK));
        } catch (Exception e) {
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }
    }

    @RequestMapping(value = "/update-agreement/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public ResponseEntity<Agreement> updateAgreement(@PathVariable("id") String id, @RequestBody AgreementFullDtoUpdate agreementFullDto) {

        if (!UUIDValidator.isValid(id)) {
            throw new InvalidUUIDException(ExceptionMessage.UUID_INVALID);
        }

        return ResponseEntity.ofNullable(agreementService.updateAgreement(id, agreementFullDto));
    }

}