package com.bankapp.bankapp.app.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;


/**
 * Account Dto
 * @author Fam Le Duc Nam
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    String id;
    String password;
    String name;
    String type;
    String status;
    String balance;
    String currencyCode;
    String createdAt;
}