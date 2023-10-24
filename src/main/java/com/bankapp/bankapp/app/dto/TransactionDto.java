package com.bankapp.bankapp.app.dto;

import lombok.Data;

@Data
public class TransactionDto {
    String id;
    String transactionType;
    String amount;
    String status;
}