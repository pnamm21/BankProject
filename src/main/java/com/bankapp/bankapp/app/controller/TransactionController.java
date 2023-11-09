package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.TransactionDto;
import com.bankapp.bankapp.app.dto.TransactionDtoFullUpdate;
import com.bankapp.bankapp.app.entity.Transaction;
import com.bankapp.bankapp.app.exception.validation.annotation.IDChecker;
import com.bankapp.bankapp.app.service.TransactionService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping(value = "/creditAccount/all-transactions")
    public ResponseEntity<List<TransactionDto>> getListTransactionByCreditAccountId(@RequestParam("id") @IDChecker String id) {
        List<TransactionDto> transactionDtos = transactionService.getListTransactionByCreditAccountId(UUID.fromString(id));
        return new ResponseEntity<>(transactionDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/debitAccount/all-transactions")
    public ResponseEntity<List<TransactionDto>> getListTransactionByDebitAccountId(@RequestParam("id") @IDChecker String id) {
        List<TransactionDto> transactionDtos = transactionService.getListTransactionByDebitAccountId(UUID.fromString(id));
        return new ResponseEntity<>(transactionDtos, HttpStatus.OK);
    }

    @RequestMapping(value = "/update-transaction/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public ResponseEntity<Transaction> updateTransaction(@PathVariable("id") @IDChecker String id, @RequestBody @Valid TransactionDtoFullUpdate transactionDtoFullUpdate) {
        return ResponseEntity.ofNullable(transactionService.updateTransaction(id, transactionDtoFullUpdate));
    }

    @RequestMapping(value = "/delete-transaction/{id}", method = {RequestMethod.DELETE, RequestMethod.GET})
    public ResponseEntity<String> deleteTransaction(@PathVariable("id") @IDChecker String id) {
        return ResponseEntity.ok(transactionService.deleteTransaction(id));
    }
}