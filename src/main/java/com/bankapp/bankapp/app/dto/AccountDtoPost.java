package com.bankapp.bankapp.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Account Dto Post
 * @author Fam Le Duc Nam
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDtoPost {

    String id;
    String password;
    String name;
    String type;
    String status;
    String balance;
    String currencyCode;
    String clientId;
}