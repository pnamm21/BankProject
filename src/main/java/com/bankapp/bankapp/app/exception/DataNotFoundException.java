package com.bankapp.bankapp.app.exception;

public class DataNotFoundException extends  RuntimeException{
    public DataNotFoundException(String e) {
        super(e);
    }
}