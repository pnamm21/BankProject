package com.bankapp.bankapp.app.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/authentication")
@RestController
public class AuthController {

    @GetMapping("/get/accountName")
    public String getAccountName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}