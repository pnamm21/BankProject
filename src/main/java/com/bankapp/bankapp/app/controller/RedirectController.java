package com.bankapp.bankapp.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Redirect Controller
 * @author Fam Le Duc Nam
 */
@Controller
public class RedirectController {

    @GetMapping("/main")
    public String redirectToMain(){
        return "redirect:/bootstrap";
    }

    @GetMapping("/")
    public String redirectToBootstrapHome(){
        return "manager/bootstrap";
    }

    @GetMapping("/account")
    public String redirectToAccount(){
        return "manager/accounts";
    }

    @GetMapping("/transaction")
    public String redirectToTransaction(){
        return "manager/account-transaction";
    }

    @GetMapping("/login")
    public String redirectToLogin(){
        return "manager/login";
    }

    @GetMapping("/reset")
    public String redirectToReset(){
        return "manager/reset";
    }

    @GetMapping("/register")
    public String redirectToRegister(){
        return "manager/register";
    }

    @GetMapping("/transfer")
    public String redirectToTransfer(){
        return "manager/transfer";
    }

    @GetMapping("/aboutMe")
    public String redirectToAboutMe(){
        return "manager/aboutMe";
    }

    @GetMapping("/swagger")
    public String redirectToSwagger(){
        return "redirect:/swagger-ui/index.html";
    }




}