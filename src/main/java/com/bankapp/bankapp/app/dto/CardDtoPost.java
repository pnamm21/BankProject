package com.bankapp.bankapp.app.dto;

import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Card Dto Post
 * @author Fam Le Duc Nam
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDtoPost {

    String cardNumber;
    String cardHolder;
    String cvv;
    String type;
    String expirationDate;
    String createdAt;

}