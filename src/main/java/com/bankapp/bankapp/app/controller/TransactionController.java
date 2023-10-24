package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.TransactionDtoFullUpdate;
import com.bankapp.bankapp.app.entity.Transaction;
import com.bankapp.bankapp.app.exception.ExceptionMessage;
import com.bankapp.bankapp.app.exception.InvalidUUIDException;
import com.bankapp.bankapp.app.mapper.TransactionMapper;
import com.bankapp.bankapp.app.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import com.bankapp.bankapp.app.validation.UUIDValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/get/{id}")
    public Optional<ResponseEntity<Transaction>> getTransactionById(@PathVariable("id") String id) {

        try {
            Optional<Transaction> optTransaction = transactionService.getTransactionById(id);
            return optTransaction.map(transaction -> new ResponseEntity<>(transaction,HttpStatus.OK));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found!");
        }

    }

    @RequestMapping(value = "/update-transaction/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public ResponseEntity<Transaction> updateTransaction(@PathVariable("id") String id, @RequestBody TransactionDtoFullUpdate transactionDtoFullUpdate) {

        if (!UUIDValidator.isValid(id)) {
            throw new InvalidUUIDException(ExceptionMessage.UUID_INVALID);
        }

        return ResponseEntity.ofNullable(transactionService.updateTransaction(id, transactionDtoFullUpdate));
    }

}
