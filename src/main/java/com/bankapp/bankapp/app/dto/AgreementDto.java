package com.bankapp.bankapp.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Agreement Dto
 * @author Fam Le Duc Nam
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgreementDto {
    String id;
    String interestRate;
    String status;
    String sum;
    String createdAt;
    String updatedAt;
}
