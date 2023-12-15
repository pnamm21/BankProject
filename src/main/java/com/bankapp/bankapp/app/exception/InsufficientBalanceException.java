package com.bankapp.bankapp.app.exception;

public class InsufficientBalanceException extends RuntimeException{
    public InsufficientBalanceException(String e) {
        super(e);
    }
}