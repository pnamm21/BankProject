package com.bankapp.bankapp.app.exception;

public class BalanceIsEmptyException extends RuntimeException{

    public BalanceIsEmptyException(String e) {
        super(e);
    }
}