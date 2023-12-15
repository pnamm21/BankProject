package com.bankapp.bankapp.app.mapper;

import com.bankapp.bankapp.app.dto.TransactionDto;
import com.bankapp.bankapp.app.dto.TransactionDtoFullUpdate;
import com.bankapp.bankapp.app.dto.TransactionDtoTransfer;
import com.bankapp.bankapp.app.dto.TransactionDtoTransferCard;
import com.bankapp.bankapp.app.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.UUID;

/**
 * Transaction Mapper
 * @author Fam Le Duc Nam
 */
@Mapper(componentModel = "spring", uses = UUID.class, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransactionMapper {

    Transaction transactionFUllDtoTotransaction(TransactionDtoFullUpdate transactionDtoFullUpdate);

    Transaction transactionDtoFullupdateToTransaction(TransactionDtoFullUpdate transactionDtoFullUpdate);
    Transaction transactionDtoTransferCardToTransaction(TransactionDtoTransferCard transactionDtoTransferCard);

    TransactionDto transactionToTransactionDto(Transaction transaction);

    List<TransactionDto> listTransactionToListTransactionDto(List<Transaction> transactions);

    Transaction mergeTransaction(Transaction from, @MappingTarget Transaction to);

    TransactionDtoFullUpdate transactionToTransactionDtoFullUpdate(Transaction updatedTransaction);
}