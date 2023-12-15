package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.TransactionDto;
import com.bankapp.bankapp.app.dto.TransactionDtoFullUpdate;
import com.bankapp.bankapp.app.entity.Transaction;
import com.bankapp.bankapp.app.exception.validation.annotation.IDChecker;
import com.bankapp.bankapp.app.service.util.TransactionService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Transaction Controller
 * @author Fam Le Duc Nam
 */
@Validated
@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping(value = "/creditAccount/transactions")
    public List<TransactionDto> getListTransactionByCreditAccountId(@RequestParam("id") @IDChecker UUID id) {
        return transactionService.getListTransactionByCreditAccountId(id);
    }

    @GetMapping(value = "/debitAccount/transactions")
    public List<TransactionDto> getListTransactionByDebitAccountId(@RequestParam("id") @IDChecker UUID id) {
        return  transactionService.getListTransactionByDebitAccountId(id);
    }

    @RequestMapping(value = "/update/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public TransactionDto updateTransaction(@PathVariable("id") @IDChecker String id, @RequestBody @Valid TransactionDtoFullUpdate transactionDtoFullUpdate) {
        return transactionService.updateTransaction(id, transactionDtoFullUpdate);
    }

    @RequestMapping(value = "/delete/{id}", method = {RequestMethod.DELETE, RequestMethod.GET})
    public String deleteTransaction(@PathVariable("id") @IDChecker String id) {
        return transactionService.deleteTransaction(id);
    }
}