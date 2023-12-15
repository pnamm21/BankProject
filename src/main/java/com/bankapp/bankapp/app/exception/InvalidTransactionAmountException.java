package com.bankapp.bankapp.app.exception;

public class InvalidTransactionAmountException extends RuntimeException{
    public InvalidTransactionAmountException(String e) {
        super(e);
    }
}