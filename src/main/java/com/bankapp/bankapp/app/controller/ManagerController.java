package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.ManagerDto;
import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.entity.Client;
import com.bankapp.bankapp.app.entity.Manager;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import com.bankapp.bankapp.app.exception.ExceptionMessage;
import com.bankapp.bankapp.app.mapper.ManagerMapper;
import com.bankapp.bankapp.app.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;
    private final ManagerMapper managerMapper;

    @GetMapping("/get/{id}")
    public ResponseEntity<ManagerDto> getManager(@PathVariable("id") String id) {
        Optional<Manager> optionalManager;
        try {
            optionalManager = managerService.getManagerById(id);
        } catch (Exception e) {
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }
        if (optionalManager.isEmpty()) {
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }
        return new ResponseEntity<>(managerMapper.managerToManagerDto(managerService.getManagerById(id).orElseThrow()), HttpStatus.OK);
    }

}