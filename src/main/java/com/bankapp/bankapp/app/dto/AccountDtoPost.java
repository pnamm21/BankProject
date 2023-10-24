package com.bankapp.bankapp.app.dto;

import lombok.Data;

@Data
public class AccountDtoPost {

    String id;
    String name;
    String type;
    String status;
    String balance;
    String currencyCode;
    String clientId;
}