package com.bankapp.bankapp.app.dto;

import lombok.*;

@Data
public class ProductDtoPost {
    String id;
    String name;
    String status;
    String currencyCode;
    String interestRate;
    String limit;
    String managerId;
}