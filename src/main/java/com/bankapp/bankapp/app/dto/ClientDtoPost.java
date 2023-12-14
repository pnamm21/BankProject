package com.bankapp.bankapp.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Client Dto Post
 * @author Fam Le Duc Nam
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDtoPost {
    String id;
    String firstName;
    String lastName;
    String status;
    String email;
    String address;
    String phone;
    String createdAt;
}