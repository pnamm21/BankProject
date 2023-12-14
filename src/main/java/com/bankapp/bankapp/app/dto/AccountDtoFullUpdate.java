package com.bankapp.bankapp.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Account Dto Full Update
 * @author Fam Le Duc Nam
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDtoFullUpdate {
    String id;
    String name;
    String type;
    String status;
    String balance;
    String currencyCode;
    String createdAt;
    String updatedAt;
    String clientId;
}