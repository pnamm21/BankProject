package com.bankapp.bankapp.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Transaction Dto Transfer
 * @author Fam Le Duc Nam
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDtoTransfer {

    String creditAccount;
    String debitAccount;
    String transactionType;
    String amount;
    String description;
    String status;
    String card;
}