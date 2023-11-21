package com.bankapp.bankapp.app.dto;

import lombok.Data;

@Data
public class CardDtoPost {

    String cardNumber;
    String cardHolder;
    String cvv;
    String type;
    String expirationDate;
    String createdAt;

}