package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.TransactionDto;
import com.bankapp.bankapp.app.dto.TransactionDtoFullUpdate;
import com.bankapp.bankapp.app.entity.Transaction;
import com.bankapp.bankapp.app.mapper.TransactionMapper;
import com.bankapp.bankapp.app.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @GetMapping("/get/{id}")
    public TransactionDto getTransactionById(@PathVariable("id")String id){
        Optional<Transaction> optTransaction;
        try{
            optTransaction = transactionService.getTransactionById(id);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"not found!");
        }
        if (optTransaction.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"not found!");
        }
        return transactionMapper.transactionToTransactionDto(optTransaction.get());
    }

    @RequestMapping(value = "/update-transaction/{id}",method = {RequestMethod.PUT,RequestMethod.GET})
    public void updateTransaction(@PathVariable("id") String id, @RequestBody TransactionDtoFullUpdate transactionDtoFullUpdate) {
        transactionService.updateTransaction(id, transactionDtoFullUpdate);
    }

}
