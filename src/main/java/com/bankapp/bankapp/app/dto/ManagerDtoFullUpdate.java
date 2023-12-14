package com.bankapp.bankapp.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Manager Dto Full Update
 * @author Fam Le Duc Nam
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerDtoFullUpdate {

    String id;
    String firstName;
    String lastName;
    String status;
    String createdAt;
    String updatedAt;
}