package com.bankapp.bankapp.app.dto;

import lombok.Data;

@Data
public class ProductDtoFullUpdate {

    String id;
    String name;
    String status;
    String currencyCode;
    String interestRate;
    String limit;
    String createdAt;
    String updatedAt;
    String managerId;
}