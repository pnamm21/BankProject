package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.exception.BalanceIsEmptyException;
import com.bankapp.bankapp.app.exception.DataNotCreatedException;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import com.bankapp.bankapp.app.exception.DataNotUpdatedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorResponse> dataNotFoundException(Throwable ex) {

        ErrorResponse response = ErrorResponse.builder(ex, HttpStatus.NOT_FOUND, ex.getMessage()).build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataNotCreatedException.class)
    public ResponseEntity<ErrorResponse> dataNotCreatedException(Throwable ex) {

        ErrorResponse response = ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, ex.getMessage()).build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataNotUpdatedException.class)
    public ResponseEntity<ErrorResponse> dataNotUpdatedException(Throwable ex) {

        ErrorResponse response = ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, ex.getMessage()).build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BalanceIsEmptyException.class)
    public ResponseEntity<ErrorResponse> balanceIsEmptyException(Throwable ex) {

        ErrorResponse response = ErrorResponse.builder(ex, HttpStatus.NOT_FOUND, ex.getMessage()).build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

    }

}