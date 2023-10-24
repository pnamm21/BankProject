package com.bankapp.bankapp.app.dto;

import lombok.Data;


@Data
public class ClientDto {
    String id;
    String status;
    String firstName;
    String lastName;
    String email;
    String address;
    String phone;
}
