package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.entity.Manager;
import com.bankapp.bankapp.app.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @GetMapping ("/{id}")
    public Manager getManagerById(@PathVariable("id") String id){
        System.out.println(id);
        Optional<Manager> optManager = managerService.getManagerById(id);
        if (optManager.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
        }
        return optManager.get();
    }
}
