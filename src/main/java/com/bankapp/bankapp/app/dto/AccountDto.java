package com.bankapp.bankapp.app.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    String id;
    String name;
    String type;
    String status;
    String balance;
    String currencyCode;
    String createdAt;
}