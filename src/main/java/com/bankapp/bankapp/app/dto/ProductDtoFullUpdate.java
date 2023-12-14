package com.bankapp.bankapp.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Product Dto Full Update
 * @author Fam Le Duc Nam
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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