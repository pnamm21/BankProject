package com.bankapp.bankapp.app.config.SecurityRegistration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class SecurityInputValues {
    private String login;
    private String email;
}