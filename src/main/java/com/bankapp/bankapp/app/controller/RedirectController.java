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
        return "home/homePage";
    }

    @GetMapping("/")
    public String redirectToHome(){
        return "home/homePage";
    }

    @GetMapping("/account")
    public String redirectToAccount(){
        return "account/accounts";
    }

    @GetMapping("/transaction")
    public String redirectToTransaction(){
        return "transaction/account-transaction";
    }

    @GetMapping("/login")
    public String redirectToMain1(){
        return "login/custom-login";
    }

    @GetMapping("/my-account")
    public String redirectToMyAccount(){
        return "jfdsbhiejbwfcws";
    }

    @GetMapping("/swagger")
    public String redirectToSwagger(){
        return "redirect:/swagger-ui/index.html";
    }


}