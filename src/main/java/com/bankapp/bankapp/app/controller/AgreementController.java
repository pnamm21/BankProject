package com.bankapp.bankapp.app.controller;


import com.bankapp.bankapp.app.dto.AgreementDto;
import com.bankapp.bankapp.app.exception.validation.annotation.IDChecker;

import com.bankapp.bankapp.app.dto.AgreementFullDtoUpdate;
import com.bankapp.bankapp.app.entity.Agreement;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import com.bankapp.bankapp.app.exception.ExceptionMessage;


import com.bankapp.bankapp.app.service.AgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/agreement")
@RequiredArgsConstructor
public class AgreementController {

    private final AgreementService agreementService;

    @GetMapping("/{id}")
    public ResponseEntity<Agreement> getAgreement(@PathVariable("id") @IDChecker String id) {
        return ResponseEntity.ok(agreementService.getAgreementById(id).orElse(null));
    }

    @GetMapping("/all-agreements")
    public ResponseEntity<List<AgreementDto>> getListAgreement(@RequestParam("id") @IDChecker String id) {

        List<AgreementDto> agreementDtos = agreementService.getListAgreement(UUID.fromString(id));
        return new ResponseEntity<>(agreementDtos, HttpStatus.OK);
    }

    @RequestMapping(value = "/update-agreement/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public ResponseEntity<Agreement> updateAgreement(@PathVariable("id") @IDChecker String id, @RequestBody AgreementFullDtoUpdate agreementFullDto) {
        return ResponseEntity.ofNullable(agreementService.updateAgreement(id, agreementFullDto));
    }

}