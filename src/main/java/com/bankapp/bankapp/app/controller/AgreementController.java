package com.bankapp.bankapp.app.controller;


import com.bankapp.bankapp.app.dto.AgreementDto;
import com.bankapp.bankapp.app.exception.validation.annotation.IDChecker;

import com.bankapp.bankapp.app.dto.AgreementFullDtoUpdate;
import com.bankapp.bankapp.app.entity.Agreement;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import com.bankapp.bankapp.app.exception.ExceptionMessage;


import com.bankapp.bankapp.app.service.AgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Agreement Controller
 * @author Fam Le Duc Nam
 */
@Validated
@RestController
@RequestMapping("/api/agreement")
@RequiredArgsConstructor
public class AgreementController {

    private final AgreementService agreementService;

    @GetMapping("/get/{id}")
    public AgreementDto getAgreement(@PathVariable("id") @IDChecker String id) {
        return agreementService.getAgreementById(id);
    }

    @GetMapping("/all-agreements")
    public List<AgreementDto> getListAgreement(@RequestParam("id") @IDChecker String id) {
        return agreementService.getListAgreement(UUID.fromString(id));
    }

    @RequestMapping(value = "/update/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public AgreementDto updateAgreement(@PathVariable("id") @IDChecker String id, @RequestBody AgreementFullDtoUpdate agreementFullDto) {
        return agreementService.updateAgreement(id, agreementFullDto);
    }

}