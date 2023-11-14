package com.bankapp.bankapp.app.dto;

import lombok.Data;

@Data
public class TransactionDtoTransfer {

    String creditAccount;
    String debitAccount;
    String transactionType;
    String amount;
    String description;
    String status;
    String card;
}