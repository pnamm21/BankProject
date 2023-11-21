package com.bankapp.bankapp.app.dto;

import lombok.Data;

@Data
public class CardDto {

    String cardNumber;
    String cardHolder;
    String cardType;
    String expirationDate;
    String cvv;

}