package com.bankapp.bankapp.app.dto;

import lombok.Data;

@Data
public class TransactionDtoTransferCard {

    String from;
    String to;
    String transactionType;
    String amount;
    String description;
    String status;

}