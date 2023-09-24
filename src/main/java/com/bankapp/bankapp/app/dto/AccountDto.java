package com.bankapp.bankapp.app.dto;

import com.bankapp.bankapp.app.enums.StatusAccount;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Value
public class AccountDto {

    UUID id;
    String name;
    Integer type;
    StatusAccount statusAccount;
    BigDecimal balance;
    Integer currencyCode;
    LocalDate createdAt;
    LocalDate updatedAt;

}
