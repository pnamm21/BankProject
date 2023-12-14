package com.bankapp.bankapp.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Card Dto
 * @author Fam Le Duc Nam
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {

    String cardNumber;
    String cardHolder;
    String type;
    String status;
    String expirationDate;
    String cvv;

}