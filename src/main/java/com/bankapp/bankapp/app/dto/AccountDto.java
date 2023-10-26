package com.bankapp.bankapp.app.dto;

import com.bankapp.bankapp.app.entity.Account;
import lombok.Data;


@Data
public class AccountDto {

    String id;
    String name;
    String type;
    String status;
    String balance;
    String currencyCode;
    String createdAt;
}