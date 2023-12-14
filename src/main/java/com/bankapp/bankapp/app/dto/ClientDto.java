package com.bankapp.bankapp.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Client Dto
 * @author Fam Le Duc Nam
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    String id;
    String status;
    String firstName;
    String lastName;
    String email;
    String address;
    String phone;
}
