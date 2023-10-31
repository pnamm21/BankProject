package com.bankapp.bankapp.app.dto;

import lombok.Data;

@Data
public class AccountDtoFullUpdate {
    String id;
    String accountNumber;
    String name;
    String type;
    String status;
    String balance;
    String currencyCode;
    String createdAt;
    String updatedAt;
    String clientId;
}