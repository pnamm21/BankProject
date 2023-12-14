package com.bankapp.bankapp.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Client Dto Full Update
 * @author Fam Le Duc Nam
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDtoFullUpdate {

    String id;
    String firstName;
    String lastName;
    String email;
    String address;
    String phone;
    String status;
    String taxCode;
    String createdAt;
    String updatedAt;
    String managerId;
}