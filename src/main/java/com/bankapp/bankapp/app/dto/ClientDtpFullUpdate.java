package com.bankapp.bankapp.app.dto;

import lombok.Data;

@Data
public class ClientDtpFullUpdate {

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