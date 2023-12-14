package com.bankapp.bankapp.app.dto;

import lombok.*;

/**
 * Product Dto Post
 * @author Fam Le Duc Nam
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDtoPost {
    String id;
    String name;
    String status;
    String currencyCode;
    String interestRate;
    String limit;
    String managerId;
}