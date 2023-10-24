package com.bankapp.bankapp.app.dto;

import lombok.Data;

@Data
public class TransactionDtoFullUpdate {
    String id;
    String transactionType;
    String amount;
    String description;
    String status;
    String createdAt;
    String debitAccountId;
    String creditAccountId;
}
