package com.bankapp.bankapp.app.dto;

import lombok.Data;

@Data

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