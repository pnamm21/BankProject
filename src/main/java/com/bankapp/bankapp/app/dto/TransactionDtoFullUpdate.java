package com.bankapp.bankapp.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Transaction Dto Full Update
 * @author Fam Le Duc Nam
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
