package com.bankapp.bankapp.app.dto;

import lombok.Data;

@Data
public class AgreementDto {
    String id;
    String interestRate;
    String status;
    String sum;
    String createdAt;
    String updatedAt;
}
