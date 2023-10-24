package com.bankapp.bankapp.app.controller;

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

import java.util.Optional;

@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @GetMapping("/get/{id}")
    public Optional<ResponseEntity<Manager>> getManager(@PathVariable("id") String id) {

        try {
            Optional<Manager> optionalManager = managerService.getManagerById(id);
            return optionalManager.map(manager -> new ResponseEntity<>(manager,HttpStatus.OK));
        } catch (Exception e) {
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }
    }
}