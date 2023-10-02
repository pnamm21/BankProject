package com.bankapp.bankapp.app.dto;

import lombok.Value;

@Value
public class AccountDto {

    String id;
    String name;
    String type;
    String statusAccount;
    String balance;
    String currencyCode;
    String createdAt;
    String updatedAt;

}
