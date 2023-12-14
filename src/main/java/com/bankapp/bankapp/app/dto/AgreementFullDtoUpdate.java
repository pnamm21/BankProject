package com.bankapp.bankapp.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Agreement Full Dto Update
 * @author Fam Le Duc Nam
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgreementFullDtoUpdate {
    String id;
    String interestRate;
    String status;
    String sum;
    String createdAt;
    String updatedAt;
    String accountId;
    String productId;
}