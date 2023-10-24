package com.bankapp.bankapp.app.dto;

import lombok.Data;

@Data
public class ClientDtoPost {
    String id;
    String firstName;
    String lastName;
    String status;
    String phone;
    String createdAt;
}