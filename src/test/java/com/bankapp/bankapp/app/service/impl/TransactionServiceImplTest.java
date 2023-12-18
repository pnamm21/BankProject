package com.bankapp.bankapp.app.service.impl;

import com.bankapp.bankapp.app.dto.AccountDtoFullUpdate;
import com.bankapp.bankapp.app.dto.TransactionDto;
import com.bankapp.bankapp.app.dto.TransactionDtoFullUpdate;
import com.bankapp.bankapp.app.dto.TransactionDtoTransfer;
import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.entity.Transaction;
import com.bankapp.bankapp.app.entity.enums.TransactionStatus;
import com.bankapp.bankapp.app.entity.enums.TransactionType;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import com.bankapp.bankapp.app.mapper.TransactionMapper;
import com.bankapp.bankapp.app.repository.AccountRepository;
import com.bankapp.bankapp.app.repository.TransactionRepository;
import com.bankapp.bankapp.app.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private static final UUID transactionId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTransactionByIdTest() {

        Transaction transaction = new Transaction();
        TransactionDto expectedTransactionDto = new TransactionDto();

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(transactionMapper.transactionToTransactionDto(transaction)).thenReturn(expectedTransactionDto);

        TransactionDto result = transactionService.getTransactionById(transactionId.toString());

        assertNotNull(result);

        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionMapper, times(1)).transactionToTransactionDto(transaction);

    }

    @Test
    void getTransactionByIdNotFoundTest() {
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> transactionService.getTransactionById(transactionId.toString()));

        verify(transactionRepository, times(1)).findById(transactionId);
    }

    @Test
    void getListTransactionByCreditAccountIdTest() {

        List<Transaction> mockTransaction = Arrays.asList(new Transaction(), new Transaction());

        when(transactionRepository.getTransactionsByCreditAccountId(transactionId)).thenReturn(mockTransaction);
        when(transactionMapper.listTransactionToListTransactionDto(mockTransaction)).thenReturn(Arrays.asList(new TransactionDto(), new TransactionDto()));

        List<TransactionDto> result = transactionService.getListTransactionByCreditAccountId(transactionId);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(transactionRepository, times(1)).getTransactionsByCreditAccountId(transactionId);
        verify(transactionMapper, times(1)).listTransactionToListTransactionDto(mockTransaction);
    }

    @Test
    void getListTransactionByDebitAccountIdTest() {

        List<Transaction> mockTransaction = Arrays.asList(new Transaction(), new Transaction());

        when(transactionRepository.getTransactionsByDebitAccountId(transactionId)).thenReturn(mockTransaction);
        when(transactionMapper.listTransactionToListTransactionDto(mockTransaction)).thenReturn(Arrays.asList(new TransactionDto(), new TransactionDto()));

        List<TransactionDto> result = transactionService.getListTransactionByDebitAccountId(transactionId);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(transactionRepository, times(1)).getTransactionsByDebitAccountId(transactionId);
        verify(transactionMapper, times(1)).listTransactionToListTransactionDto(mockTransaction);
    }

    @Test
    void updateTransactionTest() {

        TransactionDtoFullUpdate transactionDtoFullUpdate = new TransactionDtoFullUpdate();
        Transaction originalTransaction = new Transaction();

        when(transactionRepository.existsById(transactionId)).thenReturn(true);
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(originalTransaction));
        when(transactionMapper.transactionFUllDtoTotransaction(transactionDtoFullUpdate)).thenReturn(originalTransaction);
        when(transactionRepository.save(any())).thenReturn(originalTransaction);
        when(transactionMapper.transactionToTransactionDto(originalTransaction)).thenReturn(new TransactionDto());

        TransactionDto result = transactionService.updateTransaction(transactionId.toString(), transactionDtoFullUpdate);

        assertNotNull(result);

        verify(transactionRepository, times(1)).save(any());
    }

    @Test
    void updateTransactionNotFoundTest() {

        TransactionDtoFullUpdate transactionDtoFullUpdate = new TransactionDtoFullUpdate();

        when(transactionRepository.existsById(transactionId)).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> {
            transactionService.updateTransaction(transactionId.toString(), transactionDtoFullUpdate);
        });

        verify(transactionRepository, times(1)).existsById(transactionId);
        verify(transactionRepository, never()).findById(transactionId);
        verify(transactionMapper, never()).transactionFUllDtoTotransaction(transactionDtoFullUpdate);
        verify(transactionMapper, never()).mergeTransaction(any(), any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void transferTest() {

        TransactionDtoTransfer transactionDtoTransfer = new TransactionDtoTransfer();
        transactionDtoTransfer.setCreditAccount("12345");
        transactionDtoTransfer.setAmount("500");
        transactionDtoTransfer.setTransactionType("TRANSFERS");
        transactionDtoTransfer.setDescription("Transfer description");
        transactionDtoTransfer.setStatus("APPROVED");

        Account fromAccount = new Account();
        fromAccount.setId(transactionId);
        fromAccount.setBalance(1000.0);

        Account toAccount = new Account();
        toAccount.setId(transactionId);
        toAccount.setBalance(2000.0);

        when(accountRepository.findById(transactionId)).thenReturn(Optional.of(fromAccount));
        when(accountService.getAccountByAccountNumber(transactionDtoTransfer.getCreditAccount())).thenReturn(toAccount);
        when(transactionRepository.save(any())).thenReturn(new Transaction());

        assertDoesNotThrow(() -> transactionService.transfer(transactionId, transactionDtoTransfer));

        verify(accountRepository, times(1)).findById(transactionId);
        verify(accountService, times(1)).getAccountByAccountNumber(transactionDtoTransfer.getCreditAccount());
        verify(transactionRepository, times(1)).save(any());
    }

    @Test
    void deleteTransactionTest() {

        Transaction mockTransaction = new Transaction();
        mockTransaction.setId(transactionId);
        mockTransaction.setStatus(TransactionStatus.CANCELED);

        when(transactionRepository.existsById(transactionId)).thenReturn(true);
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(mockTransaction));

        String result = transactionService.deleteTransaction(transactionId.toString());

        assertEquals("Transaction has been CANCELED!", result);
        assertEquals(TransactionStatus.CANCELED, mockTransaction.getStatus());

        verify(transactionRepository, times(1)).existsById(transactionId);
        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, times(1)).save(mockTransaction);
    }

    @Test
    void deleteTransactionNitFoundTest() {

        when(transactionRepository.existsById(transactionId)).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> {
            transactionService.deleteTransaction(transactionId.toString());
        });

        verify(transactionRepository, times(1)).existsById(transactionId);
        verify(transactionRepository, never()).findById(transactionId);
        verify(transactionRepository, never()).save(any());
    }

}