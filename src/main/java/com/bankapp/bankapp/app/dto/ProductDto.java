package com.bankapp.bankapp.app.dto;

import lombok.Data;

@Data
public class ProductDto {
    String id;
    String name;
    String status;
    String currencyCode;
    String limit;
}