package com.bankapp.bankapp.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Manager Dto
 * @author Fam Le Duc Nam
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerDto {
    String id;
    String firstName;
    String lastName;
    String status;
}
