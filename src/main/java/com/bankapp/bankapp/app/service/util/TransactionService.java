package com.bankapp.bankapp.app.service.util;

import com.bankapp.bankapp.app.dto.TransactionDto;
import com.bankapp.bankapp.app.dto.TransactionDtoFullUpdate;
import com.bankapp.bankapp.app.dto.TransactionDtoTransfer;
import com.bankapp.bankapp.app.entity.Transaction;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Transaction Service
 * @author Fam Le Duc Nam
 */
@Service
public interface TransactionService {
    TransactionDto getTransactionById(String id);

    List<TransactionDto> getListTransactionByCreditAccountId(@Param("id") UUID id);

    List<TransactionDto> getListTransactionByDebitAccountId(@Param("id") UUID id);

    @Transactional
    TransactionDto updateTransaction(String id, TransactionDtoFullUpdate transactionDtoFullUpdate);

    @Transactional
    String transfer(UUID accountId, TransactionDtoTransfer transactionDtoTransfer);

    @Transactional
    String deleteTransaction(String id);


}
